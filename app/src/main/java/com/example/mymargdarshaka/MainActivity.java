package com.example.mymargdarshaka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.StringReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button studentButton,mentorButton;
    ImageButton rightButton, leftButton;
    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "login";
    private static final String TYPE="userType";
    //private static final String PHONE="userPhone";
    private static final String USER_ID = "userId";

    ImageView imageView;

    private DatabaseReference rootRef;

    static int count=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rootRef = FirebaseDatabase.getInstance().getReference();
        studentButton=(Button) findViewById(R.id.studentLoginButton);
        mentorButton=(Button) findViewById(R.id.mentorLoginButton);
        rightButton=(ImageButton) findViewById(R.id.rightButton);
        leftButton=(ImageButton) findViewById(R.id.leftButton);

        //carousel of images on mainpage
        imageView = (ImageView) findViewById(R.id.slideView);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count!=6)
                    count++;
                switch(count)
                {
                    case 1:imageView.setImageResource(R.drawable.image1);break;
                    case 2:imageView.setImageResource(R.drawable.image2);break;
                    case 3:imageView.setImageResource(R.drawable.image3);break;
                    case 4:imageView.setImageResource(R.drawable.image4);break;
                    case 5:imageView.setImageResource(R.drawable.image5);break;
                    case 6:imageView.setImageResource(R.drawable.image6);break;
                }
            }
        });
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count!=1)
                    count--;
                switch(count)
                {
                    case 1:imageView.setImageResource(R.drawable.image1);break;
                    case 2:imageView.setImageResource(R.drawable.image2);break;
                    case 3:imageView.setImageResource(R.drawable.image3);break;
                    case 4:imageView.setImageResource(R.drawable.image4);break;
                    case 5:imageView.setImageResource(R.drawable.image5);break;
                    case 6:imageView.setImageResource(R.drawable.image6);break;
                }
            }
        });

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        String type=sharedPreferences.getString(TYPE,null);
        String userId=sharedPreferences.getString(USER_ID,null);

//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        FirebaseAuth.getInstance().signOut();
//        editor.clear();
//        editor.apply();

        // if already logged in, students and mentors will be redirected to MyMentors and MyStudents respectively.
        if(type!=null){
            Toast.makeText(MainActivity.this,"You are already logged in",Toast.LENGTH_SHORT).show();
            Intent i1;

            Log.e("type: ",type);

            if(type.equals("student")) {
                i1 = new Intent(MainActivity.this, MyMentors.class);
                i1.putExtra("studentId",userId);
                i1.putExtra("firstTime",false);
                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i1);
                return;
            }
            else{
                // check noTest for this mentor
                rootRef.child("mentors").orderByChild(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot child:snapshot.getChildren()){
                            MentorDetails details = child.getValue(MentorDetails.class);
                            int noTests=details.getNoTests();
                            if(noTests==-1){
                                Intent i2 = new Intent(MainActivity.this, MyStudents.class);
                                i2.putExtra("mentorId", userId);
                                i2.putExtra("firstTime",false);
                                i2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i2);
                            }
                            else if(noTests>=5){
                                Toast.makeText(MainActivity.this,"You are not eligible",Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                FirebaseAuth.getInstance().signOut();
                                editor.clear();
                                editor.apply();
                            }
                            else{
                                rootRef.child("mentors").child(userId).child("noTests").setValue(noTests+1);
                                Intent i2 = new Intent(MainActivity.this, Test.class);
                                i2.putExtra("mentorId", userId);
                                i2.putExtra("firstTime",false);
                                i2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i2);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                return;
            }
        }

        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,AuthLogin.class);
                i.putExtra("userType","student");
                startActivity(i);
            }
        });

        mentorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,AuthLogin.class);
                i.putExtra("userType","mentor");
                startActivity(i);
            }
        });

    }
}
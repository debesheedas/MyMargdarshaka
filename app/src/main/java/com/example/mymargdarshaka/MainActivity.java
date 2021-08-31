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

    static int count=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //To make the default Action Bar on top with the App name disappear
        //need to add this on every page
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        studentButton=(Button) findViewById(R.id.studentLoginButton);
        mentorButton=(Button) findViewById(R.id.mentorLoginButton);
        rightButton=(ImageButton) findViewById(R.id.rightButton);
        leftButton=(ImageButton) findViewById(R.id.leftButton);

        //imageSwitcher = (ImageSwitcher)findViewById(R.id.slideView);
        imageView = (ImageView) findViewById(R.id.slideView);
//        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
//                                     public View makeView() {
//                                         ImageView myView = new ImageView(getApplicationContext());
//                                         return myView;
//                                     }
//                                 });
//        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
//        imageSwitcher.setInAnimation(in);
//        imageSwitcher.setOutAnimation(out);

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
                //imageView.setImageResource(R.drawable.image1);
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
                //imageView.setImageResource(R.drawable.image1);
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
                //i1.putExtra("phone",phone);
                i1.putExtra("studentId",userId);
                i1.putExtra("firstTime",false);
                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i1);
                return;
            }
            else{
                i1 = new Intent(MainActivity.this, MyStudents.class);
                //i1.putExtra("phone",phone);
                i1.putExtra("mentorId", userId);
                i1.putExtra("firstTime",false);
                // check noTest for this mentor
                // if -1 send to MyStudents
                // if <5 send to after increasing Test
                // if >=5 Toast saying not eligible
                i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i1);
                return;
            }
        }

        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i=new Intent(MainActivity.this,GuidelinesForMentors.class);
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
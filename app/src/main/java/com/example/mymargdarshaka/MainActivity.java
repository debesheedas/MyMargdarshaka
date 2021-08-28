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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.StringReader;

public class MainActivity extends AppCompatActivity {

    Button studentButton,mentorButton;
    ImageButton rightButton, leftButton;
    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "myMargdarshaka";
    private static final String TYPE="userType";

    ImageSwitcher imageSwitcher;
    ImageView imageView;

    static int count=1;

//    FirebaseDatabase firebaseDatabase;
//    DatabaseReference databaseReference;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Gson g = new Gson();

        rootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference mentorsRef = rootRef.child("mentors");
        mentorsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot child: snapshot.getChildren()){
                    Log.e("Mentor key", child.getKey());
                    Log.e("Mentor val", child.getValue().toString());
//                    System.out.println(child.getValue(MentorSchema.class).getDetails().phone);
                    System.out.println(child.getValue().toString());


//                    JsonReader reader = new JsonReader(new StringReader(child.getValue().toString()));
//                    reader.setLenient(true);

//                    MentorSchema m=g.fromJson(child.getValue().toString().trim(), MentorSchema.class);
//                    System.out.println(m.getDetails().phone);

                    try {
//                        JSONArray array = new JSONArray(String.valueOf(child.getValue().getJSONObject()));
//                        for(int i=0;i<array.length();i++){
                            JSONObject object = new JSONObject(String.valueOf(child.getValue().getJSONObject()));
                            System.out.println(object.getString("phone"));
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








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

        // if already logged in, users and mentors will be redirected to MyMentors and MyStudents repectively.
        if(type!=null){
            Toast.makeText(MainActivity.this,"You are already logged in",Toast.LENGTH_SHORT).show();
            Intent i;

            Log.e("type: ",type);

            if(type.equals("student")) {
                i = new Intent(MainActivity.this, MyMentors.class);
            }
            else{
                i = new Intent(MainActivity.this, MyStudents.class);
            }
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }

        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,AuthLogin.class);
                i.putExtra("userType","student");
                startActivity(i);
            }
        });

//        studentButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i=new Intent(MainActivity.this,GuidelinesForStudents.class);
//                i.putExtra("type","student");
//                startActivity(i);
//            }
//        });

        mentorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,AuthLogin.class);
                i.putExtra("userType","mentor");
                startActivity(i);
            }
        });
//        mentorButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i=new Intent(MainActivity.this,AuthSignupMentors1.class);
//                i.putExtra("type","mentor");
//                startActivity(i);
//            }
//        });
    }
}
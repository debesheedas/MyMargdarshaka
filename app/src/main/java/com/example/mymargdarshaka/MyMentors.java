package com.example.mymargdarshaka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyMentors extends AppCompatActivity {

    Button logout;

    SharedPreferences sharedPreferences;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    private static final String SHARED_PREF_NAME = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mymentors);

        logout=(Button) findViewById(R.id.logout_mentor);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                Intent i = new Intent(MyMentors.this,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

//                firebaseDatabase=FirebaseDatabase.getInstance();
//                databaseReference=firebaseDatabase.getReference("users");
//                String key=databaseReference.push().getKey();
//                databaseReference=databaseReference.child(key);
//
//                ArrayList<String> intr=new ArrayList<>();
//                intr.add("Maths");
//                intr.add("Science");
//
//                ArrayList<String> times=new ArrayList<>();
//                times.add("10:30");
//
//                ArrayList<Pair<String,String>> reg=new ArrayList<>();
//                reg.add(new Pair<String,String>("Maths","mentorid1"));
//                reg.add(new Pair<String,String>("Science","mentorid2"));
//
//                UserDetails userSchema = new UserDetails("Somename","abc@gmail.com","9898989888","6","Hindi",intr,times,reg);
//                databaseReference.setValue(userSchema);

//                firebaseDatabase=FirebaseDatabase.getInstance();
//                databaseReference=firebaseDatabase.getReference("mentors");
//                String key=databaseReference.push().getKey();
//                databaseReference=databaseReference.child(key);
//
//                ArrayList<String> prefLangs=new ArrayList<>();
//                prefLangs.add("English");
//                prefLangs.add("Hindi");
//
//                ArrayList<String> classes=new ArrayList<>();
//                classes.add("8");
//                classes.add("9");
//                classes.add("10");
//
//                ArrayList<String> times=new ArrayList<>();
//                times.add("10:30");
//
//                ArrayList<String> teachSubjects=new ArrayList<>();
//                teachSubjects.add("Maths");
//                teachSubjects.add("Science");
//
//                ArrayList<String> regStudents=new ArrayList<>();
//                regStudents.add("student id 1");
//                regStudents.add("student id 2");
//
//                MentorDetails mentorSchema = new MentorDetails("MyName","abc@gmail.com","9898989888",classes,prefLangs,times,regStudents,teachSubjects);
//                databaseReference.setValue(mentorSchema);

            }
        });

    }
}
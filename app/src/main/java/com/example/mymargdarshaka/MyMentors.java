package com.example.mymargdarshaka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyMentors extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private DatabaseReference rootRef;

    com.google.android.material.appbar.MaterialToolbar topAppBar;
    androidx.drawerlayout.widget.DrawerLayout drawerLayout;
    NavigationView navigationView;

    private static final String SHARED_PREF_NAME = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_mentors);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        topAppBar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Toast.makeText(getApplicationContext(), "menu item selected "+item.toString(), Toast.LENGTH_SHORT).show();

                String choice = item.toString();
                if(choice.equals("Guidelines"))
                {
                    Intent i = new Intent(MyMentors.this,GuidelinesForStudents.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
                else if(choice.equals("My Mentors"))
                {
                    //code to shift to Mentor Details Page
                }
                else if(choice.equals("Feedback"))
                {
                    //code for Feedback
                }
                else if(choice.equals("Logout"))
                {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    Intent i = new Intent(MyMentors.this,MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }

                drawerLayout.closeDrawer(Gravity.START, true);
                return true;
            }
        });



//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.clear();
//                editor.commit();
//
//                Intent i = new Intent(MyMentors.this,MainActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);

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

//            }
//        });

    }
}
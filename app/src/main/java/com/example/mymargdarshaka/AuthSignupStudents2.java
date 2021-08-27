package com.example.mymargdarshaka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AuthSignupStudents2 extends AppCompatActivity {

    AutoCompleteTextView a;

    CheckBox english, math, hindi, telugu, physics, chemistry, biology, history, geography, science, social;
    Button submit_button;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_signup_students2);

        a = AuthSignupStudents1.text_view_class;
        String s = a.getEditableText().toString();

        english = (CheckBox) findViewById(R.id.check_english);
        math = (CheckBox) findViewById(R.id.check_math);
        hindi = (CheckBox) findViewById(R.id.check_hindi);
        telugu = (CheckBox) findViewById(R.id.check_telugu);
        physics = (CheckBox) findViewById(R.id.check_physics);
        chemistry = (CheckBox) findViewById(R.id.check_chemistry);
        biology = (CheckBox) findViewById(R.id.check_biology);
        science = (CheckBox) findViewById(R.id.check_science);
        social = (CheckBox) findViewById(R.id.check_social);
        history = (CheckBox) findViewById(R.id.check_history);
        geography = (CheckBox) findViewById(R.id.check_geography);

        english.setVisibility(View.VISIBLE);
        hindi.setVisibility(View.VISIBLE);
        math.setVisibility(View.VISIBLE);
        telugu.setVisibility(View.VISIBLE);

        if(s.equals("Class 11")||s.equals("Class 12"))
        {
            physics.setVisibility(View.VISIBLE);
            chemistry.setVisibility(View.VISIBLE);
            biology.setVisibility(View.VISIBLE);
            history.setVisibility(View.VISIBLE);
            geography.setVisibility(View.VISIBLE);
        }
        else
        {
            science.setVisibility(View.VISIBLE);
            social.setVisibility(View.VISIBLE);
        }


        submit_button=(Button) findViewById(R.id.studentSignupButton2);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle extras = getIntent().getExtras();
                String name  = extras.getString("name");
                String email  = extras.getString("email");
                String class_selected = extras.getString("class_selected");
                String language_selected  = extras.getString("language_selected");
                String phone = extras.getString("phone");
                String time_selected  = extras.getString("time_selected");
                ArrayList<String> subjects = new ArrayList<>();
                ArrayList<Pair<String, String>> regSub = new ArrayList<>();

                if(english.isChecked()){
                    subjects.add("english");
                    regSub.add(new Pair<>("english", "mentorid1"));
                }
                if(hindi.isChecked()){
                    subjects.add("hindi");
                    regSub.add(new Pair<>("english", "mentorid2"));
                }
                if(telugu.isChecked()){
                    subjects.add("telugu");
                    regSub.add(new Pair<>("english", "mentorid3"));
                }
                if(math.isChecked()){
                    subjects.add("math");
                }
                if(physics.isChecked()){
                    subjects.add("physics");
                }
                if(chemistry.isChecked()){
                    subjects.add("chemistry");
                }
                if(biology.isChecked()){
                    subjects.add("biology");
                }
                if(history.isChecked()){
                    subjects.add("history");
                }
                if(geography.isChecked()){
                    subjects.add("geography");
                }
                if(social.isChecked()){
                    subjects.add("social");
                }
                if(science.isChecked()){
                    subjects.add("science");
                }

                DatabaseReference newStudentRef = FirebaseDatabase.getInstance().getReference("users").push();

//                newStudentRef.setValue(new UserSchema(newStudentRef.getKey(), name, email, phone, class_selected, language_selected, subjects, time_selected, regSub));

                //newStudentRef.child("name").setValue(name);
                //newStudentRef.child("email").setValue(email);
                //newStudentRef.child("standard").setValue(class_selected);
                //newStudentRef.child("prefLang").setValue(language_selected);
                //newStudentRef.child("timeSlots").setValue(time_selected);
                //newStudentRef.child("intrSubjects").setValue(subjects);

//                firebaseDatabase=FirebaseDatabase.getInstance();
//                databaseReference=firebaseDatabase.getReference("users");
                String key=newStudentRef.getKey();
//                newStudentRef=newStudentRef.child(key);

                UserDetails userSchema = new UserDetails(name,email,phone,class_selected,language_selected,subjects,time_selected,regSub);
                newStudentRef.setValue(userSchema);


//                DatabaseReference mentorsRef = rootRef.child("mentors");
//                mentorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for(DataSnapshot child: snapshot.getChildren()){
//                            Log.e("Mentor key", child.getKey());
//                            Log.e("Mentor val", child.getValue().toString());
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(AuthLogin.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
//                        Intent i = new Intent(AuthLogin.this,MainActivity.class);
//                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(i);
//                    }
//                });


                Intent i = new Intent(AuthSignupStudents2.this, MyMentors.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });



    }
}
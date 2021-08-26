package com.example.mymargdarshaka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AuthSignupStudents2 extends AppCompatActivity {

    AutoCompleteTextView a;

    CheckBox english, math, hindi, telugu, physics, chemistry, biology, history, geography, science, social;
    Button submit_button;

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
                String time_selected  = extras.getString("time_selected");

                ArrayList<String> subjects = new ArrayList<>();

                if(english.isChecked()){
                    subjects.add("english");
                }
                if(hindi.isChecked()){
                    subjects.add("hindi");
                }
                if(telugu.isChecked()){
                    subjects.add("telugu");
                }
                if(maths.isChecked()){
                    subjects.add("hindi");
                }

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference newStudentRef = firebaseDatabase.getReference("users").push();
                newStudentRef.child("name").setValue(name);
                newStudentRef.child("email").setValue(email);
                newStudentRef.child("standard").setValue(class_selected);
                newStudentRef.child("prefLang").setValue(language_selected);
                newStudentRef.child("timeSlots").setValue(time_selected);
                newStudentRef.child("intrSubjects").setValue(subjects);

                Intent i = new Intent(AuthSignupStudents2.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });



    }
}
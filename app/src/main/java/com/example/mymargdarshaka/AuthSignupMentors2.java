package com.example.mymargdarshaka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class AuthSignupMentors2 extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private static final String USER_ID = "userId";
    private static final String SHARED_PREF_NAME = "login";
    private static final String TYPE="userType";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_signup_mentors2);

        Bundle extras = getIntent().getExtras();
        String name  = extras.getString("name");
        String email  = extras.getString("email");
        String phone = extras.getString("phone");

        ArrayList<String> prefLangs = extras.getStringArrayList("prefLangs");
        ArrayList<String> timeSlots = extras.getStringArrayList("timeSlots");

        // will be filled by matching
        HashMap<String,ArrayList<String>> regStudents = new HashMap<>();

        ArrayList<String> teachSubjects = new ArrayList<>();
        ArrayList<String> classes = new ArrayList<>();

        Button submit_button = (Button) findViewById(R.id.mentorsSignupButton2);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // helper variable for determining which classes a mentor can teach
                int temp = 0;

                if(((CheckBox)findViewById(R.id.check_english6)).isChecked()) teachSubjects.add("english6");
                if(((CheckBox)findViewById(R.id.check_hindi6)).isChecked()) teachSubjects.add("hindi6");
                if(((CheckBox)findViewById(R.id.check_telugu6)).isChecked()) teachSubjects.add("telugu6");
                if(((CheckBox)findViewById(R.id.check_math6)).isChecked()) teachSubjects.add("math6");
                if(((CheckBox)findViewById(R.id.check_science6)).isChecked()) teachSubjects.add("science6");
                if(((CheckBox)findViewById(R.id.check_social6)).isChecked()) teachSubjects.add("social6");

                if(temp < teachSubjects.size()){
                    classes.add("6");
                    temp = teachSubjects.size();
                }

                if(((CheckBox)findViewById(R.id.check_english7)).isChecked()) teachSubjects.add("english7");
                if(((CheckBox)findViewById(R.id.check_hindi7)).isChecked()) teachSubjects.add("hindi7");
                if(((CheckBox)findViewById(R.id.check_telugu7)).isChecked()) teachSubjects.add("telugu7");
                if(((CheckBox)findViewById(R.id.check_math7)).isChecked()) teachSubjects.add("math7");
                if(((CheckBox)findViewById(R.id.check_science7)).isChecked()) teachSubjects.add("science7");
                if(((CheckBox)findViewById(R.id.check_social7)).isChecked()) teachSubjects.add("social7");

                if(temp < teachSubjects.size()){
                    classes.add("7");
                    temp = teachSubjects.size();
                }

                if(((CheckBox)findViewById(R.id.check_english8)).isChecked()) teachSubjects.add("english8");
                if(((CheckBox)findViewById(R.id.check_hindi8)).isChecked()) teachSubjects.add("hindi8");
                if(((CheckBox)findViewById(R.id.check_telugu8)).isChecked()) teachSubjects.add("telugu8");
                if(((CheckBox)findViewById(R.id.check_math8)).isChecked()) teachSubjects.add("math8");
                if(((CheckBox)findViewById(R.id.check_science8)).isChecked()) teachSubjects.add("science8");
                if(((CheckBox)findViewById(R.id.check_social8)).isChecked()) teachSubjects.add("social8");

                if(temp < teachSubjects.size()){
                    classes.add("8");
                    temp = teachSubjects.size();
                }

                if(((CheckBox)findViewById(R.id.check_english9)).isChecked()) teachSubjects.add("english9");
                if(((CheckBox)findViewById(R.id.check_hindi9)).isChecked()) teachSubjects.add("hindi9");
                if(((CheckBox)findViewById(R.id.check_telugu9)).isChecked()) teachSubjects.add("telugu9");
                if(((CheckBox)findViewById(R.id.check_math9)).isChecked()) teachSubjects.add("math9");
                if(((CheckBox)findViewById(R.id.check_science9)).isChecked()) teachSubjects.add("science9");
                if(((CheckBox)findViewById(R.id.check_social9)).isChecked()) teachSubjects.add("social9");

                if(temp < teachSubjects.size()){
                    classes.add("9");
                    temp = teachSubjects.size();
                }

                if(((CheckBox)findViewById(R.id.check_english10)).isChecked()) teachSubjects.add("english10");
                if(((CheckBox)findViewById(R.id.check_hindi10)).isChecked()) teachSubjects.add("hindi10");
                if(((CheckBox)findViewById(R.id.check_telugu10)).isChecked()) teachSubjects.add("telugu10");
                if(((CheckBox)findViewById(R.id.check_math10)).isChecked()) teachSubjects.add("math10");
                if(((CheckBox)findViewById(R.id.check_science10)).isChecked()) teachSubjects.add("science10");
                if(((CheckBox)findViewById(R.id.check_social10)).isChecked()) teachSubjects.add("social10");

                if(temp < teachSubjects.size()){
                    classes.add("10");
                    temp = teachSubjects.size();
                }

                if(((CheckBox)findViewById(R.id.check_english11)).isChecked()) teachSubjects.add("english11");
                if(((CheckBox)findViewById(R.id.check_hindi11)).isChecked()) teachSubjects.add("hindi11");
                if(((CheckBox)findViewById(R.id.check_telugu11)).isChecked()) teachSubjects.add("telugu11");
                if(((CheckBox)findViewById(R.id.check_math11)).isChecked()) teachSubjects.add("math11");
                if(((CheckBox)findViewById(R.id.check_physics11)).isChecked()) teachSubjects.add("physics11");
                if(((CheckBox)findViewById(R.id.check_chemistry11)).isChecked()) teachSubjects.add("chemistry11");
                if(((CheckBox)findViewById(R.id.check_history11)).isChecked()) teachSubjects.add("history11");
                if(((CheckBox)findViewById(R.id.check_geography11)).isChecked()) teachSubjects.add("geography11");

                if(temp < teachSubjects.size()){
                    classes.add("11");
                    temp = teachSubjects.size();
                }

                if(((CheckBox)findViewById(R.id.check_english12)).isChecked()) teachSubjects.add("english12");
                if(((CheckBox)findViewById(R.id.check_hindi12)).isChecked()) teachSubjects.add("hindi12");
                if(((CheckBox)findViewById(R.id.check_telugu12)).isChecked()) teachSubjects.add("telugu12");
                if(((CheckBox)findViewById(R.id.check_math12)).isChecked()) teachSubjects.add("math12");
                if(((CheckBox)findViewById(R.id.check_physics12)).isChecked()) teachSubjects.add("physics12");
                if(((CheckBox)findViewById(R.id.check_chemistry12)).isChecked()) teachSubjects.add("chemistry12");
                if(((CheckBox)findViewById(R.id.check_history12)).isChecked()) teachSubjects.add("history12");
                if(((CheckBox)findViewById(R.id.check_geography12)).isChecked()) teachSubjects.add("geography12");

                if(temp < teachSubjects.size()){
                    classes.add("12");
                    temp = teachSubjects.size();
                }

                // push mentor details to DB before taking test
                DatabaseReference newMentorRef = FirebaseDatabase.getInstance().getReference("mentors").push();
                String key = newMentorRef.getKey();
                newMentorRef.setValue(new MentorDetails(
                        name,
                        email,
                        phone,
                        classes,
                        prefLangs,
                        timeSlots,
                        regStudents,
                        teachSubjects,
                        1)
                ).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(TYPE,"mentor");
                        editor.putString(USER_ID,key);
                        editor.apply();

                        Intent i = new Intent(AuthSignupMentors2.this, Test.class);
                        i.putExtra("firstTime",true);
                        i.putExtra("mentorId",key);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                });
            }
        });
    }
}
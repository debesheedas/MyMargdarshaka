package com.example.mymargdarshaka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class AuthSignupMentors1 extends AppCompatActivity {

    Button next;

    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_signup_mentors1);

        next=(Button) findViewById(R.id.mentorsSignupButton1);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                TextInputLayout nameInputLayout = findViewById(R.id.nameTextField);
                String name = nameInputLayout.getEditText().getText().toString();

                TextInputLayout emailInputLayout = findViewById(R.id.emailTextField);
                String email = emailInputLayout.getEditText().getText().toString();

//                AutoCompleteTextView languageInput = (AutoCompleteTextView)findViewById(R.id.text_view_language);
//                String language_selected = languageInput.getEditableText().toString();
//
//                AutoCompleteTextView timeInput = (AutoCompleteTextView)findViewById(R.id.text_view_time);
//                String time_selected = timeInput.getEditableText().toString();
                CheckBox english = (CheckBox) findViewById(R.id.check_english);
                CheckBox hindi = (CheckBox) findViewById(R.id.check_hindi);
                CheckBox telugu = (CheckBox) findViewById(R.id.check_telugu);

                ArrayList<String> prefLangs = new ArrayList<>();
                if(english.isChecked()) prefLangs.add("English");
                if(hindi.isChecked()) prefLangs.add("Hindi");
                if(telugu.isChecked()) prefLangs.add("Telugu");

                CheckBox morning = (CheckBox) findViewById(R.id.check_morning);
                CheckBox afternoon = (CheckBox) findViewById(R.id.check_afternoon);
                CheckBox evening = (CheckBox) findViewById(R.id.check_evening);

                ArrayList<String> timeSlots = new ArrayList<>();
                if(morning.isChecked()) timeSlots.add("Morning");
                if(afternoon.isChecked()) timeSlots.add("Afternoon");
                if(evening.isChecked()) timeSlots.add("Evening");

                if(name.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Name", Toast.LENGTH_SHORT).show();
                }
                else if(email.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Email Address ", Toast.LENGTH_SHORT).show();
                }
                else if(prefLangs.size() == 0)
                {
                    Toast.makeText(getApplicationContext(), "Please Select a Preferred Language ", Toast.LENGTH_SHORT).show();
                }
                else if(timeSlots.size() == 0)
                {
                    Toast.makeText(getApplicationContext(), "Please Select a Preferred Time ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent i = new Intent(AuthSignupMentors1.this,AuthSignupMentors2.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("name", name);
                    i.putExtra("email", email);
                    i.putExtra("phone", getIntent().getStringExtra("phone"));
                    i.putStringArrayListExtra("prefLangs", prefLangs);
                    i.putStringArrayListExtra("timeSlots", timeSlots);
                    startActivity(i);
                }
            }
        });

//        AutoCompleteTextView text_view_language, text_view_time;// instantiating ac_text_view
//        text_view_language = findViewById(R.id.text_view_language);
//        text_view_time = findViewById(R.id.text_view_time);
//
//        ArrayList<String> languages = new ArrayList<>();
//        languages.add("English");
//        languages.add("Hindi");
//        languages.add("Telugu");
//
//        ArrayList<String> times = new ArrayList<>();
//        times.add("Morning");
//        times.add("Afternoon");
//        times.add("Evening");
//
//        ArrayAdapter<String> languagesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, languages);
//        text_view_language.setAdapter(languagesAdapter);
//
//        ArrayAdapter<String> timesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, times);
//        text_view_time.setAdapter(timesAdapter);
    }
}
package com.example.mymargdarshaka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.ArrayList;

public class AuthSignupMentors1 extends AppCompatActivity {

    Button logout;

    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_signup_mentors1);

        logout=(Button) findViewById(R.id.mentorsSignupButton1);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                Intent i = new Intent(AuthSignupMentors1.this,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        AutoCompleteTextView text_view_language, text_view_time;// instantiating ac_text_view
        text_view_language = findViewById(R.id.text_view_language);
        text_view_time = findViewById(R.id.text_view_time);

        ArrayList<String> languages = new ArrayList<>();
        languages.add("English");
        languages.add("Hindi");
        languages.add("Telugu");

        ArrayList<String> times = new ArrayList<>();
        times.add("Morning");
        times.add("Afternoon");
        times.add("Evening");

        ArrayAdapter<String> languagesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, languages);
        text_view_language.setAdapter(languagesAdapter);

        ArrayAdapter<String> timesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, times);
        text_view_time.setAdapter(timesAdapter);
    }
}
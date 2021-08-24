package com.example.mymargdarshaka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class AuthSignupStudents1 extends AppCompatActivity {

    Button logout;

    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_signup_students1);

        logout = (Button) findViewById(R.id.logout_student);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                Intent i = new Intent(AuthSignupStudents1.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });


        AutoCompleteTextView ac_text_view;
        // instantiating ac_text_view
        ac_text_view = findViewById(R.id.ac_text_view);

        // creating a list of dropdown menu options
        ArrayList<String> classes = new ArrayList<>();
        classes.add("Class 6");
        classes.add("Class 7");
        classes.add("Class 8");
        classes.add("Class 9");
        classes.add("Class 10");
        classes.add("Class 11");
        classes.add("Class 12");

        // creating a classAdapter, I'm not sure what this does but it works
        ArrayAdapter<String> classesAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,    // this is the style of each option,
                                                        // but I just used something simple
                classes
        );

        // attaching the adapter to ac_text_view
        ac_text_view.setAdapter(classesAdapter);

        // adding a listener which responds to an option selection
        // using a toast to demonstrate
        ac_text_view.setOnItemClickListener((parent, view, position, id) -> Toast.makeText(getApplicationContext(), "Selected Item: " + classes.get(position), Toast.LENGTH_SHORT).show());

    }
}
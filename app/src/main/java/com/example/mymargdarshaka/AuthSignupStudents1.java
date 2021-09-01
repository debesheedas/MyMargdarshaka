package com.example.mymargdarshaka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AuthSignupStudents1 extends AppCompatActivity {

    Button next_button;
    static AutoCompleteTextView text_view_class, text_view_language, text_view_time;// instantiating ac_text_view
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_signup_students1);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        next_button=(Button) findViewById(R.id.studentSignupButton1);
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                //extracting user input from UI elements
                TextInputLayout nameInputLayout = findViewById(R.id.nameTextField);
                String name = nameInputLayout.getEditText().getText().toString();

                TextInputLayout emailInputLayout = findViewById(R.id.emailTextField);
                String email = emailInputLayout.getEditText().getText().toString();

                AutoCompleteTextView classInput = (AutoCompleteTextView)findViewById(R.id.text_view_class);
                String class_selected = classInput.getEditableText().toString().substring(6);

                AutoCompleteTextView languageInput = (AutoCompleteTextView)findViewById(R.id.text_view_language);
                String language_selected = languageInput.getEditableText().toString();

                AutoCompleteTextView timeInput = (AutoCompleteTextView)findViewById(R.id.text_view_time);
                String time_selected = timeInput.getEditableText().toString();

                //checks to ensure that user enters required input
                if(name.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Name", Toast.LENGTH_SHORT).show();
                }
                else if(class_selected.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please Select a Class ", Toast.LENGTH_SHORT).show();
                }
                else if(language_selected.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please Select a Preferred Language ", Toast.LENGTH_SHORT).show();
                }
                else if(time_selected.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Please Select a Preferred Time ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.e("margaApp", name);
                    Log.e("margaApp", email);
                    Log.e("margaApp", class_selected);
                    Log.e("margaApp", language_selected);
                    Log.e("margaApp", time_selected);

                    Intent i = new Intent(AuthSignupStudents1.this, AuthSignupStudents2.class);
                    i.putExtra("phone",getIntent().getStringExtra("phone"));

                    // to send collected data to the next activity
                    i.putExtra("name", name);
                    i.putExtra("email", email);
                    i.putExtra("class_selected", class_selected);
                    i.putExtra("language_selected", language_selected);
                    i.putExtra("time_selected", time_selected);
                    i.putExtra("phone", getIntent().getStringExtra("phone"));

                    startActivity(i);
                }
            }
        });

        text_view_class = findViewById(R.id.text_view_class);
        text_view_language = findViewById(R.id.text_view_language);
        text_view_time = findViewById(R.id.text_view_time);

        // creating a list of dropdown menu options
        ArrayList<String> classes = new ArrayList<>();
        classes.add("Class 6");
        classes.add("Class 7");
        classes.add("Class 8");
        classes.add("Class 9");
        classes.add("Class 10");
        classes.add("Class 11");
        classes.add("Class 12");

        ArrayList<String> languages = new ArrayList<>();
        languages.add("English");
        languages.add("Hindi");
        languages.add("Telugu");

        ArrayList<String> times = new ArrayList<>();
        times.add("Morning");
        times.add("Afternoon");
        times.add("Evening");

        // creating a classAdapter for creating options for multiple choice inputs using dropdown menu
        ArrayAdapter<String> classesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, classes);
        text_view_class.setAdapter(classesAdapter);  // attaching the adapter to ac_text_view

        ArrayAdapter<String> languagesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, languages);
        text_view_language.setAdapter(languagesAdapter);

        ArrayAdapter<String> timesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, times);
        text_view_time.setAdapter(timesAdapter);
    }
}
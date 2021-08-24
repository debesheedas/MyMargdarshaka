package com.example.mymargdarshaka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AuthSignupStudents1 extends AppCompatActivity {


    Button logout;

    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_signup_students1);

        //List<String> list = Arrays.asList("Option 1", "Option 2");
        //val items = listOf("Option 1", "Option 2", "Option 3", "Option 4")
        //val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        //(textField.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        //menu = findViewById(R.id.class_select);

        ArrayList<String> classes = new ArrayList<>();
        classes.add("Class 6");
        classes.add("Class 7");
        classes.add("Class 8");
        classes.add("Class 9");
        classes.add("Class 10");
        classes.add("Class 11");

//        ArrayAdapter<String> classesAdapter = new ArrayAdapter<>(
//                this,
//                android.R.layout.simple_list_item_1,
//                classes);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                R.layout.list_item, classes);
        //simpleListView.setAdapter(arrayAdapter);
        //(AutoCompleteTextView)textField.editText.setAdapter(arrayAdapter);



        logout=(Button) findViewById(R.id.studentSignupButton1);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                Intent i = new Intent(AuthSignupStudents1.this,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

    }
}
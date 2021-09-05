package com.example.mymargdarshaka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class AuthSignupMentors1 extends AppCompatActivity {

  Button next;
  SharedPreferences sharedPreferences;
  private static final String SHARED_PREF_NAME = "login";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_auth_signup_mentors1);

    next = findViewById(R.id.mentorsSignupButton1);

    sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

    next.setOnClickListener(
        view -> {
          SharedPreferences.Editor editor = sharedPreferences.edit();
          editor.clear();
          editor.apply();

          // extracting user input from UI elements
          TextInputLayout nameInputLayout = findViewById(R.id.nameTextField);
          String name = nameInputLayout.getEditText().getText().toString();

          TextInputLayout emailInputLayout = findViewById(R.id.emailTextField);
          String email = emailInputLayout.getEditText().getText().toString();

          CheckBox english = findViewById(R.id.check_english);
          CheckBox hindi = findViewById(R.id.check_hindi);
          CheckBox telugu = findViewById(R.id.check_telugu);

          ArrayList<String> prefLangs = new ArrayList<>();
          if (english.isChecked()) prefLangs.add("English");
          if (hindi.isChecked()) prefLangs.add("Hindi");
          if (telugu.isChecked()) prefLangs.add("Telugu");

          CheckBox morning = findViewById(R.id.check_morning);
          CheckBox afternoon = findViewById(R.id.check_afternoon);
          CheckBox evening = findViewById(R.id.check_evening);

          ArrayList<String> timeSlots = new ArrayList<>();
          if (morning.isChecked()) timeSlots.add("Morning");
          if (afternoon.isChecked()) timeSlots.add("Afternoon");
          if (evening.isChecked()) timeSlots.add("Evening");

          //regex pattern to check for valid email address
          String regex = "^(.+)@(.+)$";
          Pattern pattern = Pattern.compile(regex);

          // checks to ensure that user enters required input
          if (name.equals("")) {
            Toast.makeText(getApplicationContext(), "Please Enter Name", Toast.LENGTH_SHORT)
                    .show();
          } else if (email.equals("")) {
            Toast.makeText(
                    getApplicationContext(), "Please Enter Email Address ", Toast.LENGTH_SHORT)
                .show();
          } else if(!pattern.matcher(email).matches()){
              Toast.makeText(
                      getApplicationContext(), "Please Enter a Valid Email Address ", Toast.LENGTH_SHORT)
                      .show();
            }
          else if (prefLangs.size() == 0) {
            Toast.makeText(
                    getApplicationContext(),
                    "Please Select a Preferred Language ",
                    Toast.LENGTH_SHORT)
                .show();
          } else if (timeSlots.size() == 0) {
            Toast.makeText(
                    getApplicationContext(), "Please Select a Preferred Time ", Toast.LENGTH_SHORT)
                .show();
          } else {
            Intent i = new Intent(AuthSignupMentors1.this, AuthSignupMentors2.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            // sending the details to the next activity
            i.putExtra("name", name);
            i.putExtra("email", email);
            i.putExtra("phone", getIntent().getStringExtra("phone"));
            i.putStringArrayListExtra("prefLangs", prefLangs);
            i.putStringArrayListExtra("timeSlots", timeSlots);
            startActivity(i);
          }
        });
  }
}

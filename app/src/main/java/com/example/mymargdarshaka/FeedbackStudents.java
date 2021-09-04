package com.example.mymargdarshaka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class FeedbackStudents extends AppCompatActivity {
  private final CollectionReference mColRef =
      FirebaseFirestore.getInstance().collection("students_feedback");

  SharedPreferences sharedPreferences,pref;
  private static final String SHARED_PREF_NAME = "login";
  com.google.android.material.appbar.MaterialToolbar topAppBar;
  androidx.drawerlayout.widget.DrawerLayout drawerLayout;
  NavigationView navigationView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feedback_students);
    EditText feedbackInput = findViewById(R.id.text_feedback);
    Button feedbackSubmitButton = findViewById(R.id.button_submit);

    feedbackSubmitButton.setOnClickListener(
        view -> {
          String feedback = feedbackInput.getText().toString();
          HashMap<String, String> data = new HashMap<>();
          data.put("feedback", feedback);

          // saving the feedback in the database
          mColRef
              .add(data)
              .addOnCompleteListener(
                  new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                      Toast.makeText(
                              FeedbackStudents.this,
                              "Your feedback is recorded",
                              Toast.LENGTH_SHORT)
                          .show();
                      Intent intent = new Intent(FeedbackStudents.this, MyMentors.class);
                      startActivity(intent);
                    }
                  });
        });

    sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
    // Functionality for App Bar with Menu
    topAppBar = findViewById(R.id.topAppBar);
    drawerLayout = findViewById(R.id.drawerLayout);
    navigationView = findViewById(R.id.navigationView);

    pref=getSharedPreferences("lang",MODE_PRIVATE);

      //TODO @Shreetesh Paste code snippet here

      topAppBar.setOnMenuItemClickListener(new com.google.android.material.appbar.MaterialToolbar.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem item) {
              SharedPreferences.Editor editor = pref.edit();
              switch(item.getItemId()){
                  case R.id.english:
                      //TODO: here add the language preference as english
                      editor.putString("language","en");
                      editor.apply();
                      Toast.makeText(getApplicationContext(), "Please restart the app for language change to English", Toast.LENGTH_SHORT)
                              .show();
                      break;
                  case R.id.hindi:
                      //TODO here add the language preference as hindi
                      editor.putString("language","hi");
                      editor.apply();
                      Toast.makeText(getApplicationContext(), "Please restart the app for language change to Hindi", Toast.LENGTH_SHORT)
                              .show();
                      break;
              }
              return true;
          }
      });

    topAppBar.setNavigationOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            drawerLayout.openDrawer(Gravity.LEFT);
          }
        });
    // Functionality for side panel with navigation options
    navigationView.setNavigationItemSelectedListener(
        new NavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            String choice = item.toString();
            if (choice.equals(getString(R.string.guidelines_label))) {
              Intent i = new Intent(FeedbackStudents.this, GuidelinesForStudents.class);
              startActivity(i);
            } else if (choice.equals(getString(R.string.my_mentors_label))) {
              Intent i = new Intent(FeedbackStudents.this, MyMentors.class);
              startActivity(i);
            } else if (choice.equals(getString(R.string.feedback_label))) {
              // code to shift to Feedback Page
            } else if (choice.equals(getString(R.string.logout_label))) {

                // logging the student out
              SharedPreferences.Editor editor = sharedPreferences.edit();
              FirebaseAuth.getInstance().signOut();
              editor.clear();
              editor.apply();

              Intent i = new Intent(FeedbackStudents.this, MainActivity.class);
              i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(i);
            }
            return true;
          }
        });
  }
}

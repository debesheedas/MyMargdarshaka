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

public class FeedbackMentors extends AppCompatActivity {
  private final CollectionReference mColRef =
      FirebaseFirestore.getInstance().collection("mentors_feedback");

  com.google.android.material.appbar.MaterialToolbar topAppBar;
  androidx.drawerlayout.widget.DrawerLayout drawerLayout;
  NavigationView navigationView;
  SharedPreferences sharedPreferences;
  private static final String SHARED_PREF_NAME = "login";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_feedback_mentors);
    EditText feedbackInput = findViewById(R.id.text_feedback);
    Button feedbackSubmitButton = findViewById(R.id.button_submit);

    feedbackSubmitButton.setOnClickListener(
        view -> {
          String feedback = feedbackInput.getText().toString();
          HashMap<String, String> data = new HashMap<>();
          data.put("feedback", feedback);
          mColRef
              .add(data)
              .addOnCompleteListener(
                  new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                      Toast.makeText(
                              FeedbackMentors.this, "Your feedback is recorded", Toast.LENGTH_SHORT)
                          .show();
                      Intent intent = new Intent(FeedbackMentors.this, MyMentors.class);
                      startActivity(intent);
                    }
                  });
        });

    sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
    // Functionality for App Bar with Menu
    topAppBar = findViewById(R.id.topAppBar);
    drawerLayout = findViewById(R.id.drawerLayout);
    navigationView = findViewById(R.id.navigationView);

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
            if (choice.equals("Guidelines")) {
              Intent i = new Intent(FeedbackMentors.this, GuidelinesForMentors.class);
              startActivity(i);
            } else if (choice.equals("My Students")) {
              Intent intent = new Intent(FeedbackMentors.this, MyStudents.class);
              startActivity(intent);
            } else if (choice.equals("Feedback")) {
              // code to shift to Feedback Page
            } else if (choice.equals("Logout")) {
              SharedPreferences.Editor editor = sharedPreferences.edit();
              FirebaseAuth.getInstance().signOut();
              editor.clear();
              editor.commit();

              Intent i = new Intent(FeedbackMentors.this, MainActivity.class);
              i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(i);
            }
            drawerLayout.closeDrawer(Gravity.START, true);
            return true;
          }
        });
  }
}

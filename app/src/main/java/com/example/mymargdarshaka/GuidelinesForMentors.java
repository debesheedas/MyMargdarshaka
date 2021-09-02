package com.example.mymargdarshaka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GuidelinesForMentors extends AppCompatActivity {

  TextView niosLink, resources;
  SharedPreferences sharedPreferences;
  FirebaseDatabase firebaseDatabase;
  DatabaseReference databaseReference;

  com.google.android.material.appbar.MaterialToolbar topAppBar;
  androidx.drawerlayout.widget.DrawerLayout drawerLayout;
  NavigationView navigationView;

  private static final String SHARED_PREF_NAME = "login";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_guidelines_for_mentors);
    sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

    // hyperlink for NIOS website
    niosLink = (TextView) findViewById(R.id.m_guideline6);
    niosLink.setMovementMethod(LinkMovementMethod.getInstance());
    // hyperlink for resources document
    resources = (TextView) findViewById(R.id.m_guideline9);
    resources.setMovementMethod(LinkMovementMethod.getInstance());
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
              // code for going to Guidelines page
            } else if (choice.equals("My Students")) {
              Intent i = new Intent(GuidelinesForMentors.this, MyStudents.class);
              startActivity(i);
            } else if (choice.equals("Feedback")) {
              Intent i = new Intent(GuidelinesForMentors.this, FeedbackMentors.class);
              startActivity(i);
            } else if (choice.equals("Logout")) {

              // logging out the mentor
              SharedPreferences.Editor editor = sharedPreferences.edit();
              editor.clear();
              editor.commit();

              Intent i = new Intent(GuidelinesForMentors.this, MainActivity.class);
              i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(i);
            }
            return true;
          }
        });
  }
}

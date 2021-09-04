package com.example.mymargdarshaka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GuidelinesForMentors extends AppCompatActivity {

  TextView niosLink, resources;
  SharedPreferences sharedPreferences,pref;
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

    pref=getSharedPreferences("lang",MODE_PRIVATE);

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

    //TODO: @Shreetesh Please add the functionality of saving the selected language in the shared preferences and then copy paste the following code snippet wherever indicated
    //start of code snippet--------------------------------------------
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
    //end of language options UI code snippet-----------------------------------------------------

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
              // code for going to Guidelines page
            } else if (choice.equals(getString(R.string.my_students_label))) {
              Intent i = new Intent(GuidelinesForMentors.this, MyStudents.class);
              startActivity(i);
            } else if (choice.equals(getString(R.string.feedback_label))) {
              Intent i = new Intent(GuidelinesForMentors.this, FeedbackMentors.class);
              startActivity(i);
            } else if (choice.equals(getString(R.string.logout_label))) {

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

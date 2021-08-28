package com.example.mymargdarshaka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        //hyperlink for NIOS website
        niosLink =(TextView) findViewById(R.id.m_guideline6);
        niosLink.setMovementMethod(LinkMovementMethod.getInstance());
        //hyperlink for resources document
        resources =(TextView) findViewById(R.id.m_guideline9);
        resources.setMovementMethod(LinkMovementMethod.getInstance());

        topAppBar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Toast.makeText(getApplicationContext(), "menu item selected "+item.toString(), Toast.LENGTH_SHORT).show();

                String choice = item.toString();
                if(choice.equals("Guidelines"))
                {
                    //code for going to Guidelines page
                }
                else if(choice.equals("My Students"))
                {
                    Intent i = new Intent(GuidelinesForMentors.this,MyStudents.class);
                    startActivity(i);
                }
                else if(choice.equals("Feedback"))
                {   //code for Feedback
                    LayoutInflater inflater = (LayoutInflater)
                            getSystemService(LAYOUT_INFLATER_SERVICE);
                    View popupView = inflater.inflate(R.layout.feedback_popup, null);

                    // create the popup window
                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    boolean focusable = true; // lets taps outside the popup also dismiss it
                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                    // show the popup window, which view you pass in doesn't matter, it is only used for the window token
                    View view = findViewById(android.R.id.content).getRootView();
                    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                    // dismiss the popup window when touched
                    popupView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            popupWindow.dismiss();
                            return true;
                        }
                    });
                }
                else if(choice.equals("Logout"))
                {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    Intent i = new Intent(GuidelinesForMentors.this,MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }

                drawerLayout.closeDrawer(Gravity.START, true);
                return true;
            }
        });
    }
}
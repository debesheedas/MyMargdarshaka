package com.example.mymargdarshaka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyStudents extends AppCompatActivity {
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
        setContentView(R.layout.activity_my_students);

        LinearLayout root = findViewById(R.id.root_linear);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

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
                    Intent i = new Intent(MyStudents.this,GuidelinesForMentors.class);
                    startActivity(i);
                }
                else if(choice.equals("My Students"))
                {
                    //code to shift to Student Details Page
                }
                else if(choice.equals("Feedback"))
                {
                    //code for Feedback
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

                    Intent i = new Intent(MyStudents.this,MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }

                drawerLayout.closeDrawer(Gravity.START, true);
                return true;
            }
        });

        //Code for starting up the popup
//        //if(condition for first time open)
//        if(true)
//        {
//            LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
//            View popupView = inflater.inflate(R.layout.feedback_popup, null);
//
//            // create the popup window
//            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
//            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
//            boolean focusable = true; // lets taps outside the popup also dismiss it
//            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
//
//           // show the popup window, which view you pass in doesn't matter, it is only used for the window token
//            View view = findViewById(R.id.activity_my_students).getRootView();
//            popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//            // dismiss the popup window when touched
//            popupView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    popupWindow.dismiss();
//                    return true;
//                }
//            });
//        }


    }

    private int dpAsPixels(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private TextView getTextView(String data, int aboveId) {
        TextView textView = new TextView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        int marginTop = dpAsPixels(16);
        params.setMargins(0, marginTop, 0, 0);
        params.addRule(RelativeLayout.BELOW, aboveId);
        textView.setTextSize(14);
        textView.setLayoutParams(params);
        textView.setText(data);
        textView.setTextColor(ContextCompat.getColor(this, R.color.mentor_details));
        return textView;
    }

    private MaterialCardView getCard(
            String classLevel,
            ArrayList<Map<String, String>> students,
            int aboveId
    ) {
        MaterialCardView card = new MaterialCardView(this);
        RelativeLayout.LayoutParams cardParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.addRule(RelativeLayout.BELOW, aboveId);
        card.setLayoutParams(cardParams);
        card.setCardElevation(dpAsPixels(10));
        int cardMargin = dpAsPixels(8);
        cardParams.setMargins(cardMargin, cardMargin, cardMargin, cardMargin);


        LinearLayout linearLayoutCard = new LinearLayout(this);
        LinearLayout.LayoutParams linearLayoutCardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearLayoutCard.setOrientation(LinearLayout.VERTICAL);
        linearLayoutCard.setLayoutParams(linearLayoutCardParams);

        //TODO(@Kranthi): Add the textViews dynamically for students details


        card.addView(linearLayoutCard);

        return card;
    }
}
//
////code for guidelines popup ---------------------------------------------
////if(condition for first time open)
//        if(true)
//                {
//                LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
//                View popupView = inflater.inflate(R.layout.guidelines_for_mentors_popup, null);
//
//                // create the popup window
//                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
//                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
//                boolean focusable = true; // lets taps outside the popup also dismiss it
//final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
//
//        // show the popup window, which view you pass in doesn't matter, it is only used for the window token
//        View view = findViewById(R.id.activity_my_students).getRootView();
//        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//
//        TextView niosLink, resources;
//        //hyperlink for NIOS website
//        niosLink =(TextView) popupView.findViewById(R.id.s_guideline6);
//        niosLink.setMovementMethod(LinkMovementMethod.getInstance());
//        //hyperlink for resources document
//        resources =(TextView) popupView.findViewById(R.id.s_guideline9);
//        resources.setMovementMethod(LinkMovementMethod.getInstance());
//        }
////code for guidelines popup ends here---------------------------------------------
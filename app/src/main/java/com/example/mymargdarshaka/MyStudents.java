package com.example.mymargdarshaka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyStudents extends AppCompatActivity {

  SharedPreferences sharedPreferences,pref;
  FirebaseDatabase firebaseDatabase;
  DatabaseReference databaseReference;

  private DatabaseReference rootRef;

  com.google.android.material.appbar.MaterialToolbar topAppBar;
  androidx.drawerlayout.widget.DrawerLayout drawerLayout;
  NavigationView navigationView;

  private static final String SHARED_PREF_NAME = "login";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_students);

    LinearLayout root = findViewById(R.id.root_linear);
    rootRef = FirebaseDatabase.getInstance().getReference();
    sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

    pref=getSharedPreferences("lang",MODE_PRIVATE);

    rootRef
        .child("mentors")
        .child(getIntent().getStringExtra("mentorId"))
        .addListenerForSingleValueEvent(
            new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {

                MentorDetails details = snapshot.getValue(MentorDetails.class);
                HashMap<String, ArrayList<String>> regStudents = details.getRegStudents();
                if (regStudents == null) {
                  return;
                }
                Set<String> subjects = regStudents.keySet();

                rootRef
                    .child("users")
                    .get()
                    .addOnCompleteListener(
                        new OnCompleteListener<DataSnapshot>() {
                          @Override
                          public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                              HashMap<String, Object> users = (HashMap<String, java.lang.Object>) task.getResult().getValue();
                              Log.d("FOO", task.getResult().toString());
                              Log.d("FOO", task.getResult().getValue().getClass().toString());

                              // stores subject as key, ArrayList of student names and phone numbers
                              // as value
                              // for rendering the data in UI
                              HashMap<String, ArrayList<String>> data = new HashMap<>();

                              for (String subName : subjects) {

                                data.put(subName, new ArrayList<>());
                                ArrayList<String> studentIds = regStudents.get(subName);

                                for (String studentId : studentIds) {
                                  HashMap student = (HashMap) users.get(studentId);
                                  if (student != null) {
                                    String name = (String) student.get("name");
                                    String phone = (String) student.get("phone");
                                    data.get(subName).add(name + " - " + phone);
                                  }
                                }
                              }

                              // here use the data to render
                              int prevId = R.id.card_view;
                              for (HashMap.Entry<String, ArrayList<String>> entry :
                                  data.entrySet()) {
                                MaterialCardView cardView =
                                    getCard(entry.getKey(), entry.getValue(), prevId);
                                root.addView(cardView);
                                prevId = cardView.getId();
                              }
                            }

                            // popup for mentor guidelines  ------------
                            if (getIntent().getBooleanExtra("firstTime", false)) {
                              LayoutInflater inflater =
                                  (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                              View popupView =
                                  inflater.inflate(R.layout.guidelines_for_mentors_popup, null);

                              // create the popup window
                              int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                              int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                              boolean focusable =
                                  true; // lets taps outside the popup also dismiss it
                              final PopupWindow popupWindow =
                                  new PopupWindow(popupView, width, height, focusable);

                              // show the popup window, which view you pass in doesn't matter, it is
                              // only used for the window token
                              View view = findViewById(R.id.activity_my_students).getRootView();
                              popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                              // dismiss the popup window when touched
                              popupView.setOnTouchListener(
                                  new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                      popupWindow.dismiss();
                                      return true;
                                    }
                                  });
                            }
                            // --------------

                          }
                        });
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {}
            });

    ArrayList<String> students = new ArrayList<>();
    students.add("STUDENT 1");
    students.add("STUDENT 2");
    students.add("STUDENT 3");

    MaterialCardView card = getCard("Physics Grade 11", students, R.id.card_view);
    MaterialCardView card2 = getCard("Physics Grade 12", students, card.getId());

    root.addView(card);
    root.addView(card2);
    // Functionality for App Bar with Menu
    topAppBar = findViewById(R.id.topAppBar);
    drawerLayout = findViewById(R.id.drawerLayout);
    navigationView = findViewById(R.id.navigationView);

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
              Intent i = new Intent(MyStudents.this, GuidelinesForMentors.class);
              startActivity(i);
            } else if (choice.equals(getString(R.string.my_mentors_label))) {
              // code to shift to Student Details Page
            } else if (choice.equals(getString(R.string.feedback_label))) {
              Intent intent = new Intent(MyStudents.this, FeedbackMentors.class);
              startActivity(intent);
              // logging the mentor out
            } else if (choice.equals(getString(R.string.logout_label))) {
              SharedPreferences.Editor editor = sharedPreferences.edit();
              FirebaseAuth.getInstance().signOut();
              editor.clear();
              editor.commit();

              Intent i = new Intent(MyStudents.this, MainActivity.class);
              i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
              startActivity(i);
            }
            return true;
          }
        });
  }

  private int dpAsPixels(int dp) {
    float scale = getResources().getDisplayMetrics().density;
    return (int) (dp * scale + 0.5f);
  }

  private TextView getTextView(String data, int aboveId) {
    TextView textView = new TextView(this);
    RelativeLayout.LayoutParams params =
        new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    int margin = dpAsPixels(16);
    params.setMargins(margin, margin, 0, margin);
    params.addRule(RelativeLayout.BELOW, aboveId);
    textView.setTextSize(14);
    textView.setLayoutParams(params);
    textView.setText(data);
    textView.setTextColor(ContextCompat.getColor(this, R.color.mentor_details));
    return textView;
  }

  // UI for card
  private MaterialCardView getCard(String title, ArrayList<String> students, int aboveId) {
    MaterialCardView card = new MaterialCardView(this);
    RelativeLayout.LayoutParams cardParams =
        new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    cardParams.addRule(RelativeLayout.BELOW, aboveId);
    card.setLayoutParams(cardParams);
    card.setCardElevation(dpAsPixels(10));
    int cardMargin = dpAsPixels(8);
    cardParams.setMargins(cardMargin, cardMargin, cardMargin, cardMargin);

    LinearLayout linearLayoutCard = new LinearLayout(this);
    LinearLayout.LayoutParams linearLayoutCardParams =
        new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    linearLayoutCard.setOrientation(LinearLayout.VERTICAL);
    linearLayoutCard.setLayoutParams(linearLayoutCardParams);

    TextView titleTextView = new TextView(this);
    RelativeLayout.LayoutParams titleTextViewParams =
        new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    titleTextView.setLayoutParams(titleTextViewParams);
    Pattern courseLevel = Pattern.compile("([a-z]+)(\\d+)");
    Matcher matcher = courseLevel.matcher(title);
    if (matcher.find()) {
      titleTextView.setText(
          matcher.group(1).substring(0, 1).toUpperCase(Locale.ROOT)
              + matcher.group(1).substring(1)
              + " "
              + matcher.group(2));
    } else {
      titleTextView.setText(title);
    }
    int titlePadding = dpAsPixels(15);
    titleTextView.setPadding(titlePadding, titlePadding, titlePadding, titlePadding);
    titleTextView.setTextSize(25);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      titleTextView.setBackgroundColor(getColor(R.color.purple_500));
      titleTextView.setTextColor(getColor(R.color.white));
    }

    linearLayoutCard.addView(titleTextView);
    int prevId = titleTextView.getId();
    for (String student : students) {
      TextView textView = getTextView(student, prevId);
      linearLayoutCard.addView(textView);
      prevId = textView.getId();
    }
    card.addView(linearLayoutCard);
    return card;
  }
}

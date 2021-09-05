package com.example.mymargdarshaka;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyMentors extends AppCompatActivity {

  SharedPreferences sharedPreferences,pref;
  FirebaseDatabase firebaseDatabase;
  DatabaseReference databaseReference;
  private DatabaseReference rootRef;

  com.google.android.material.appbar.MaterialToolbar topAppBar;
  androidx.drawerlayout.widget.DrawerLayout drawerLayout;
  NavigationView navigationView;

  private static final String SHARED_PREF_NAME = "login";
  private static final String PHONE = "userPhone";
  private static final String USER_ID = "userId";


  // rendering the assigned mentors as a list of cards
  public void display(
      HashMap<String, String> mentors, String prefLang, String timeSlot, LinearLayout root) {
    DatabaseReference mentorsRef = rootRef.child("mentors");
    mentorsRef.addListenerForSingleValueEvent(
        new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
            Set keys = mentors.keySet();
            for (DataSnapshot child : snapshot.getChildren()) {
              for (Object key : keys) {
                if (mentors.get(key.toString()).equals(child.getKey())) {
                  MentorDetails mentorDetails = child.getValue(MentorDetails.class);
                  MaterialCardView card =
                      getCard(
                          key.toString(),
                          mentorDetails.getName(),
                          mentorDetails.getPhone(),
                          mentorDetails.getEmail(),
                          prefLang,
                          timeSlot,
                          R.id.card_view);
                  root.addView(card);
                }
              }
            }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {}
        });

    // display guidelines popup if the student is logging in for the first time
    if (getIntent().getBooleanExtra("firstTime", false)) {
      // and raising a null point exception
      LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
      View popupView = inflater.inflate(R.layout.guidelines_for_students_popup, null);

      // create the popup window
      int width = LinearLayout.LayoutParams.WRAP_CONTENT;
      int height = LinearLayout.LayoutParams.WRAP_CONTENT;
      boolean focusable = true; // lets taps outside the popup also dismiss it
      final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

      // show the popup window, which view you pass in doesn't matter, it is only used for the
      // window token
      View view = findViewById(R.id.activity_my_mentors).getRootView();
      popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

      TextView niosLink, resources;
      // hyperlink for NIOS website
      niosLink = (TextView) popupView.findViewById(R.id.s_guideline6);
      niosLink.setMovementMethod(LinkMovementMethod.getInstance());
      // hyperlink for resources document
      resources = (TextView) popupView.findViewById(R.id.s_guideline8);
      resources.setMovementMethod(LinkMovementMethod.getInstance());
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_mentors);

    LinearLayout root = findViewById(R.id.root_linear);
    rootRef = FirebaseDatabase.getInstance().getReference();
    sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

    pref=getSharedPreferences("lang",MODE_PRIVATE);

    // obtaining the list of assigned mentors for each subject
    rootRef
        .child("users")
        .child(getIntent().getStringExtra("studentId"))
        .addListenerForSingleValueEvent(
            new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails details = snapshot.getValue(UserDetails.class);
                if (details.getRegSubjects() != null) {
                  display(
                      details.getRegSubjects(), details.getPrefLang(), details.getTimeSlot(), root);
                }
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {}
            });

    // Functionality for App Bar with Menu
    topAppBar = findViewById(R.id.topAppBar);
    drawerLayout = findViewById(R.id.drawerLayout);
    navigationView = findViewById(R.id.navigationView);

    // Functionality for App Bar with Menu
    topAppBar.setOnMenuItemClickListener(new com.google.android.material.appbar.MaterialToolbar.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {
        SharedPreferences.Editor editor = pref.edit();
        switch(item.getItemId()){
          case R.id.english:
            editor.putString("language","en");
            editor.apply();
            Toast.makeText(getApplicationContext(), "Please restart the app for language change to English", Toast.LENGTH_SHORT)
                    .show();
            break;
          case R.id.hindi:
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
              Intent i = new Intent(MyMentors.this, GuidelinesForStudents.class);
              i.putExtra("studentId",getIntent().getStringExtra("studentId"));
              i.putExtra("firstTime",getIntent().getBooleanExtra("firstTime",false));
              startActivity(i);
            } else if (choice.equals(getString(R.string.my_mentors_label))) {
              // code to shift to Mentor Details Page
            } else if (choice.equals(getString(R.string.feedback_label))) {
              Intent intent = new Intent(MyMentors.this, FeedbackStudents.class);
              intent.putExtra("studentId",getIntent().getStringExtra("studentId"));
              intent.putExtra("firstTime",getIntent().getBooleanExtra("firstTime",false));
              startActivity(intent);
              // logging the student out
            } else if (choice.equals(getString(R.string.logout_label))) {
              SharedPreferences.Editor editor = sharedPreferences.edit();
              FirebaseAuth.getInstance().signOut();
              editor.clear();
              editor.apply();

              Intent i = new Intent(MyMentors.this, MainActivity.class);
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

  // UI for the card
  private MaterialCardView getCard(
      String title,
      String name,
      String phone,
      String email,
      String language,
      String timeSlot,
      int aboveId) {
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

    LinearLayout linearLayoutPrimary = new LinearLayout(this);
    LinearLayout.LayoutParams linearLayoutPrimaryParams =
        new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    linearLayoutPrimary.setOrientation(LinearLayout.VERTICAL);
    //        int linearLayoutPrimaryPadding = dpAsPixels(16);
    //        linearLayoutPrimary.setPadding(linearLayoutPrimaryPadding, linearLayoutPrimaryPadding,
    // linearLayoutPrimaryPadding, linearLayoutPrimaryPadding);
    linearLayoutPrimary.setLayoutParams(linearLayoutPrimaryParams);

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
    LinearLayout linearLayoutPrimaryChild = new LinearLayout(this);
    linearLayoutPrimaryChild.setOrientation(LinearLayout.VERTICAL);
    linearLayoutPrimaryChild.setLayoutParams(linearLayoutCardParams);

    TextView nameTextView = getTextView("Mentor - " + name, titleTextView.getId());
    TextView mobileTextView = getTextView("Mobile - " + phone, nameTextView.getId());
    TextView emailTextView = getTextView("Email - " + email, mobileTextView.getId());
    TextView languageTextView = getTextView("Language - " + language, emailTextView.getId());
    TextView timeSlotTextView = getTextView("Time slot - " + timeSlot, languageTextView.getId());

    LinearLayout linearLayoutSecondary = new LinearLayout(this);
    linearLayoutSecondary.setOrientation(LinearLayout.HORIZONTAL);
    LinearLayout.LayoutParams linearLayoutSecondaryParams =
        new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    linearLayoutSecondaryParams.setMargins(8, 8, 8, 8);
    linearLayoutSecondary.setLayoutParams(linearLayoutSecondaryParams);

    MaterialButton phoneButton = new MaterialButton(this, null, R.attr.borderlessButtonStyle);
    RelativeLayout.LayoutParams phoneButtonParams =
        new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    phoneButtonParams.setMargins(0, 0, 8, 0);
    phoneButton.setText(R.string.PHONE);
    phoneButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Uri u = Uri.parse("tel:" + phone);
        Intent i = new Intent(Intent.ACTION_DIAL, u);
        startActivity(i);
      }
    });

    MaterialButton emailButton = new MaterialButton(this, null, R.attr.borderlessButtonStyle);
    RelativeLayout.LayoutParams emailButtonParams =
        new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    emailButtonParams.setMargins(0, 0, 8, 0);
    emailButton.setText(R.string.EMAIL);
    emailButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Uri u = Uri.parse("mailto:" + email);
        Intent i = new Intent(Intent.ACTION_VIEW, u);
        startActivity(i);
      }
    });

    linearLayoutPrimaryChild.addView(nameTextView);
    linearLayoutPrimaryChild.addView(mobileTextView);
    linearLayoutPrimaryChild.addView(emailTextView);
    linearLayoutPrimaryChild.addView(languageTextView);
    linearLayoutPrimaryChild.addView(timeSlotTextView);

    linearLayoutPrimary.addView(titleTextView);
    linearLayoutPrimary.addView(linearLayoutPrimaryChild);

    linearLayoutSecondary.addView(phoneButton);
    linearLayoutSecondary.addView(emailButton);

    linearLayoutCard.addView(linearLayoutPrimary);
    linearLayoutCard.addView(linearLayoutSecondary);

    card.addView(linearLayoutCard);
    return card;
  }

  public void setLocale(Activity activity, String langCode) {
    Locale locale = new Locale(langCode);
    Locale.setDefault(locale);
    Resources resources = activity.getResources();
    Configuration config = resources.getConfiguration();
    config.setLocale(locale);
    resources.updateConfiguration(config, resources.getDisplayMetrics());
  }
}

package com.example.mymargdarshaka;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

  Button studentButton, mentorButton;
  ImageButton rightButton, leftButton;
  SharedPreferences sharedPreferences;

  private static final String SHARED_PREF_NAME = "login";
  private static final String TYPE = "userType";
  private static final String USER_ID = "userId";

  ImageView imageView;

  private DatabaseReference rootRef;

  static int count = 1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    //TODO: get the chosen language code from shared preferences
    // and replace "hi" with it

    setLocale(MainActivity.this, "en");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    rootRef = FirebaseDatabase.getInstance().getReference();
    studentButton = (Button) findViewById(R.id.studentLoginButton);
    mentorButton = (Button) findViewById(R.id.mentorLoginButton);
    rightButton = (ImageButton) findViewById(R.id.rightButton);
    leftButton = (ImageButton) findViewById(R.id.leftButton);

    ChipGroup languageChipGroup = findViewById(R.id.chip_group_language);
    Chip selectedLanguage = findViewById(languageChipGroup.getCheckedChipId());
    Log.d("INITIAL LANGUAGE",selectedLanguage.getText().toString());
//    if (selectedLanguage.getText().toString().equals("हिंदी")) {
//      setLocale(MainActivity.this, "hi");
//    }
//    else{
//      setLocale(MainActivity.this,"en");
//    }
    languageChipGroup.setOnCheckedChangeListener(
        new ChipGroup.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(ChipGroup group, int checkedId) {
            Log.d("CHECKED ID",String.valueOf(checkedId));
            if(checkedId!=-1) {
              Chip chip = findViewById(languageChipGroup.getCheckedChipId());
              String selectedLanguage = chip.getText().toString();

              Log.d("afsd", selectedLanguage);

              //TODO: save the preferred language code
              // hi for hindi and en for english
              if (selectedLanguage.equals("हिंदी")) {
                Log.d("LANGUAGE", "onCheckedChanged: hindi");
                setLocale(MainActivity.this, "hi");
                setContentView(R.layout.activity_main);
              } else if (selectedLanguage.equals("English") || selectedLanguage.equals("अंग्रेज़ी")) {
                Log.d("LANGUAGE", "onCheckedChanged: english");
                setLocale(MainActivity.this, "en");
                setContentView(R.layout.activity_main);
              }
            }
          }
        });

    // carousel of images on mainpage
    imageView = (ImageView) findViewById(R.id.slideView);
    rightButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            if (count != 6) count++;
            switch (count) {
              case 1:
                imageView.setImageResource(R.drawable.image1);
                break;
              case 2:
                imageView.setImageResource(R.drawable.image2);
                break;
              case 3:
                imageView.setImageResource(R.drawable.image3);
                break;
              case 4:
                imageView.setImageResource(R.drawable.image4);
                break;
              case 5:
                imageView.setImageResource(R.drawable.image5);
                break;
              case 6:
                imageView.setImageResource(R.drawable.image6);
                break;
            }
          }
        });
    leftButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            if (count != 1) count--;
            switch (count) {
              case 1:
                imageView.setImageResource(R.drawable.image1);
                break;
              case 2:
                imageView.setImageResource(R.drawable.image2);
                break;
              case 3:
                imageView.setImageResource(R.drawable.image3);
                break;
              case 4:
                imageView.setImageResource(R.drawable.image4);
                break;
              case 5:
                imageView.setImageResource(R.drawable.image5);
                break;
              case 6:
                imageView.setImageResource(R.drawable.image6);
                break;
            }
          }
        });

    sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

    String type = sharedPreferences.getString(TYPE, null);
    String userId = sharedPreferences.getString(USER_ID, null);

    // code for debugging purpose (do not delete) ------------------------------------------

    //        SharedPreferences.Editor editor = sharedPreferences.edit();
    //        FirebaseAuth.getInstance().signOut();
    //        editor.clear();
    //        editor.apply();

    // -------------------------------------------------------------------------------------

    // if already logged in, students and mentors will be redirected to MyMentors and MyStudents
    // respectively.
    if (type != null) {
      Toast.makeText(MainActivity.this, "You are already logged in", Toast.LENGTH_SHORT).show();
      Intent i1;

      Log.e("type: ", type);

      if (type.equals("student")) {
        i1 = new Intent(MainActivity.this, MyMentors.class);
        i1.putExtra("studentId", userId);
        i1.putExtra("firstTime", false);
        i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i1);
        return;
      } else {
        // check noTest for this mentor
        rootRef
            .child("mentors")
            .orderByChild(userId)
            .addListenerForSingleValueEvent(
                new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                      MentorDetails details = child.getValue(MentorDetails.class);
                      // obtaining the number of attempted tests from the database
                      int noTests = details.getNoTests();
                      if (noTests == -1) {        // if the mentor has passed the test previously
                        Intent i2 = new Intent(MainActivity.this, MyStudents.class);
                        i2.putExtra("mentorId", userId);
                        i2.putExtra("firstTime", false);
                        i2.setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i2);
                      } else if (noTests >= 5) {    // if the mentor has exceeded the threshold number of attempts
                        Toast.makeText(
                                MainActivity.this, "You are not eligible", Toast.LENGTH_SHORT)
                            .show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        FirebaseAuth.getInstance().signOut();
                        editor.clear();
                        editor.apply();
                      } else {        // if the mentor still has a few attempts left
                        rootRef
                            .child("mentors")
                            .child(userId)
                            .child("noTests")
                            .setValue(noTests + 1);
                        Intent i2 = new Intent(MainActivity.this, Test.class);
                        i2.putExtra("mentorId", userId);
                        i2.putExtra("firstTime", false);
                        i2.setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i2);
                      }
                    }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {}
                });
        return;
      }
    }

    // if the user is logged out
    studentButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent i = new Intent(MainActivity.this, AuthLogin.class);
            i.putExtra("userType", "student");
            startActivity(i);
          }
        });

    // if the user is logged out
    mentorButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Intent i = new Intent(MainActivity.this, AuthLogin.class);
            i.putExtra("userType", "mentor");
            startActivity(i);
          }
        });
  }

  public void setLocale(Activity activity, String langCode) {
    Locale locale = new Locale(langCode);
    Locale.setDefault(locale);
    Resources resources = activity.getResources();
    Log.d("RESOURCES",String.valueOf(resources.getDisplayMetrics()));
    Configuration config = resources.getConfiguration();
    config.setLocale(locale);
    Log.d("CONFIGURATIONS",String.valueOf(config));
    resources.updateConfiguration(config, resources.getDisplayMetrics());
    Log.d("LANGUAGE", "Finished setting language");
  }
}

package com.example.mymargdarshaka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class AuthSignupStudents2 extends AppCompatActivity {

  AutoCompleteTextView a;
  CheckBox english,
      math,
      hindi,
      telugu,
      physics,
      chemistry,
      biology,
      history,
      geography,
      science,
      social;
  Button submit_button;
  boolean x;
  private DatabaseReference rootRef;
  SharedPreferences sharedPreferences;
  private static final String USER_ID = "userId";

  ArrayList<String> subjects = new ArrayList<>();
  HashMap<String, String> regSub = new HashMap<>();

  private static final String SHARED_PREF_NAME = "login";
  private static final String TYPE = "userType";

  Bundle extras;
  String name;
  String email;
  String class_selected;
  String language_selected;
  String phone;
  String time_selected;

    @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_auth_signup_students2);
    x = true;
    a = AuthSignupStudents1.text_view_class;
    String s = a.getEditableText().toString();

    english = findViewById(R.id.check_english);
    math =  findViewById(R.id.check_math);
    hindi = findViewById(R.id.check_hindi);
    telugu = findViewById(R.id.check_telugu);
    physics =  findViewById(R.id.check_physics);
    chemistry =  findViewById(R.id.check_chemistry);
    biology =  findViewById(R.id.check_biology);
    science =  findViewById(R.id.check_science);
    social = findViewById(R.id.check_social);
    history =  findViewById(R.id.check_history);
    geography =  findViewById(R.id.check_geography);

    english.setVisibility(View.VISIBLE);
    hindi.setVisibility(View.VISIBLE);
    math.setVisibility(View.VISIBLE);
    telugu.setVisibility(View.VISIBLE);

    // only the subjects available for the chosen class are displayed for user to choose
    if (s.equals("Class 11") || s.equals("Class 12")) {
      physics.setVisibility(View.VISIBLE);
      chemistry.setVisibility(View.VISIBLE);
      biology.setVisibility(View.VISIBLE);
      history.setVisibility(View.VISIBLE);
      geography.setVisibility(View.VISIBLE);
    } else {
      science.setVisibility(View.VISIBLE);
      social.setVisibility(View.VISIBLE);
    }
    submit_button =findViewById(R.id.studentSignupButton2);
    submit_button.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            extras = getIntent().getExtras();
            name = extras.getString("name");
            email = extras.getString("email");
            class_selected = extras.getString("class_selected");
            language_selected = extras.getString("language_selected");
            phone = extras.getString("phone");
            time_selected = extras.getString("time_selected");

            if (english.isChecked()) {
              subjects.add("english" + class_selected);
              //                    regSub.add(new Pair<>("english", "mentorid1"));
            }
            if (hindi.isChecked()) {
              subjects.add("hindi" + class_selected);
              //                    regSub.add(new Pair<>("english", "mentorid2"));
            }
            if (telugu.isChecked()) {
              subjects.add("telugu" + class_selected.toString());
              //                    regSub.add(new Pair<>("english", "mentorid3"));
            }
            if (math.isChecked()) {
              subjects.add("math" + class_selected);
            }
            if (physics.isChecked()) {
              subjects.add("physics" + class_selected.toString());
            }
            if (chemistry.isChecked()) {
              subjects.add("chemistry" + class_selected.toString());
            }
            if (biology.isChecked()) {
              subjects.add("biology" + class_selected.toString());
            }
            if (history.isChecked()) {
              subjects.add("history" + class_selected.toString());
            }
            if (geography.isChecked()) {
              subjects.add("geography" + class_selected.toString());
            }
            if (social.isChecked()) {
              subjects.add("social" + class_selected.toString());
            }
            if (science.isChecked()) {
              subjects.add("science" + class_selected.toString());
            }

            DatabaseReference newStudentRef =
                FirebaseDatabase.getInstance().getReference("users").push();
            String key = newStudentRef.getKey();
              rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference mentorsRef = rootRef.child("mentors");

            ArrayList<Pair<Integer, Pair<String, String>>> mentors =
                new ArrayList<>(); // Integer, <mentor id, subject>

            mentorsRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long l = snapshot.getChildrenCount();
                    long k = 0;
                    for (DataSnapshot child : snapshot.getChildren()) {

                      k++;
                      Log.e("mentor: ", child.getValue().toString());

                      MentorDetails mentor = child.getValue(MentorDetails.class);
                      Log.e("1", String.valueOf(mentor.getClasses().contains(class_selected)));
                      Log.e("2", String.valueOf(mentor.getPrefLangs().contains(language_selected)));
                      Log.e("3", String.valueOf(mentor.getTimeSlots().contains(time_selected)));
                      Log.e("4", String.valueOf(mentor.getNoTests()));
                      if (mentor.getClasses().contains(class_selected)
                          && mentor.getPrefLangs().contains(language_selected)
                          && mentor.getTimeSlots().contains(time_selected)
                          && mentor.getNoTests() == -1) {
                        for (String i : mentor.getTeachSubjects()) {
                          if (subjects.contains(i)) {
                            mentors.add(
                                Pair.create(
                                    mentor.getRegStudents() == null
                                        ? 0
                                        : mentor.getRegStudents().size(),
                                    Pair.create(child.getKey(), i)));
                          }
                        }
                      }
                      if (k == l) {
                        assignMentor(key, newStudentRef, mentorsRef, mentors);
                      }
                    }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {}
                });
          }

          public void assignMentor(
              String key,
              DatabaseReference newStudentRef,
              DatabaseReference mentorsRef,
              ArrayList<Pair<Integer, Pair<String, String>>> mentors) {
            Collections.sort(
                mentors,
                    (p1, p2) -> p1.first - p2.first);
            ArrayList<String> tempSub1 = new ArrayList<>();
            tempSub1.addAll(subjects);
            for (int i = 0; i < mentors.size(); i++) {
              if (tempSub1.size() == 0) {
                break;
              }
              tempSub1.remove(mentors.get(i).second.second);
            }
            ArrayList<String> tempSub = new ArrayList<>();
            tempSub.addAll(subjects);
            UserDetails userSchema =
                new UserDetails(
                    name,
                    email,
                    phone,
                    class_selected,
                    language_selected,
                    tempSub1,
                    time_selected,
                    regSub);
            newStudentRef.setValue(userSchema);

            sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(TYPE, "student");
            editor.putString(USER_ID, key);
            editor.apply();

            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
            ArrayList<Pair<String, String>> forMentors = new ArrayList<>(); // <Mentorid, subject>
            for (int i = 0; i < mentors.size(); i++) {
              if (tempSub.size() == 0) {
                break;
              }
              usersRef
                  .child(key)
                  .child("regSubjects")
                  .child(mentors.get(i).second.second)
                  .setValue(mentors.get(i).second.first);
              tempSub.remove(mentors.get(i).second.second);
              forMentors.add(
                  Pair.create(mentors.get(i).second.first, mentors.get(i).second.second));
            }
            mentorsRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot child : snapshot.getChildren()) {
                      for (int j = 0; j < forMentors.size(); j++) {
                        if (forMentors.get(j).first.equals(child.getKey())) {
                          MentorDetails details = child.getValue(MentorDetails.class);
                          if (details.getRegStudents() == null) {
                            HashMap<String, ArrayList<String>> regStudents = new HashMap<>();
                            ArrayList<String> temp = new ArrayList<>();
                            temp.add(key);
                            regStudents.put(forMentors.get(j).second, temp);
                            mentorsRef
                                .child(forMentors.get(j).first)
                                .child("regStudents")
                                .child(forMentors.get(j).second)
                                .setValue(temp);
                          } else if (details.getRegStudents().get(forMentors.get(j).second)
                              == null) {
                            ArrayList<String> temp = new ArrayList<>();
                            temp.add(key);
                            mentorsRef
                                .child(forMentors.get(j).first)
                                .child("regStudents")
                                .child(forMentors.get(j).second)
                                .setValue(temp);
                          } else {
                            ArrayList<String> temp = new ArrayList<>();
                            temp.addAll(details.getRegStudents().get(forMentors.get(j).second));
                            temp.add(key);
                            mentorsRef
                                .child(forMentors.get(j).first)
                                .child("regStudents")
                                .child(forMentors.get(j).second)
                                .setValue(temp);
                          }
                        }
                      }
                    }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {}
                });

            Intent i = new Intent(AuthSignupStudents2.this, MyMentors.class);
            i.putExtra("noMentorsAssignedHere", x);
            i.putExtra("phone", getIntent().getStringExtra("phone"));
            i.putExtra("studentId", key);
            i.putExtra("firstTime", true);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
          }
        });
  }
}

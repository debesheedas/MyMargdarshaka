package com.example.mymargdarshaka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.common.io.LineReader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthSignupMentors2 extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private static final String USER_ID = "userId";
    private static final String SHARED_PREF_NAME = "login";
    private static final String TYPE="userType";
    private static ArrayList<CheckBox> allCheckBoxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_signup_mentors2);

        // get the data from the previous Activity
        Bundle extras = getIntent().getExtras();
        String name  = extras.getString("name");
        String email  = extras.getString("email");
        String phone = extras.getString("phone");

        ArrayList<String> prefLangs = extras.getStringArrayList("prefLangs");
        ArrayList<String> timeSlots = extras.getStringArrayList("timeSlots");

        // this will be filled in MentorMatching after completing the test successfully
        HashMap<String,ArrayList<String>> regStudents = new HashMap<>();

        // all the subjects the mentor can teach
        ArrayList<String> teachSubjects = new ArrayList<>();
        // all the classes the mentor can teach
        ArrayList<String> teachClasses = new ArrayList<>();

        // for rendering UI elements ( checkboxes ) dynamically.
        ArrayList<String> subjects = new ArrayList<>();
        subjects.add("english");
        subjects.add("hindi");
        subjects.add("telugu");
        subjects.add("math");
        subjects.add("physics");
        subjects.add("chemistry");
        subjects.add("biology");
        subjects.add("history");
        subjects.add("geography");
        subjects.add("science");
        subjects.add("social");

        ArrayList<String> classes_options = new ArrayList<>();
        classes_options.add("6");
        classes_options.add("7");
        classes_options.add("8");
        classes_options.add("9");
        classes_options.add("10");
        classes_options.add("11");
        classes_options.add("12");

        ArrayList<String> junior_class_options = new ArrayList<>();
        junior_class_options.add("6");
        junior_class_options.add("7");
        junior_class_options.add("8");
        junior_class_options.add("9");
        junior_class_options.add("10");

        ArrayList<String> senior_class_options = new ArrayList<>();
        senior_class_options.add("11");
        senior_class_options.add("12");

        LinearLayout root = findViewById(R.id.root_linear);
        for(String subject: subjects)
        {
            if(subject.equals("english")||subject.equals("hindi")||subject.equals("telugu")||subject.equals("math")) {
                MaterialCardView card = getCard(subject, classes_options, R.id.card_view);
                root.addView(card);
            } else if(subject.equals("physics")||subject.equals("chemistry")||subject.equals("biology")||
                    subject.equals("history")||subject.equals("geography")) {
                MaterialCardView card = getCard(subject, senior_class_options, R.id.card_view);
                root.addView(card);
            } else if(subject.equals("science")||subject.equals("social")){
                MaterialCardView card = getCard(subject, junior_class_options, R.id.card_view);
                root.addView(card);
            }
        }

        // submitButton listener --------------------------
        Button submitButton = (Button)findViewById(R.id.mentorsSignupButton2);
        submitButton.setOnClickListener(view -> {

            // helper variable for determining which classes a mentor can teach.
            int temp = 0;

            for(String c : junior_class_options){

                if(((CheckBox)root.findViewWithTag("check_english"+c)).isChecked()) teachSubjects.add("english"+c);
                if(((CheckBox)root.findViewWithTag("check_hindi"+c)).isChecked()) teachSubjects.add("hindi"+c);
                if(((CheckBox)root.findViewWithTag("check_telugu"+c)).isChecked()) teachSubjects.add("telugu"+c);
                if(((CheckBox)root.findViewWithTag("check_math"+c)).isChecked()) teachSubjects.add("math"+c);
                if(((CheckBox)root.findViewWithTag("check_science"+c)).isChecked()) teachSubjects.add("science"+c);
                if(((CheckBox)root.findViewWithTag("check_social"+c)).isChecked()) teachSubjects.add("social"+c);

                // if the mentor can teach atleast one subject in a class, the mentor is said to teach that class.
                if(temp < teachSubjects.size()){
                    teachClasses.add(c);
                    temp = teachSubjects.size();
                }
            }


            for(String c : senior_class_options){

                if(((CheckBox)root.findViewWithTag("check_english"+c)).isChecked()) teachSubjects.add("english"+c);
                if(((CheckBox)root.findViewWithTag("check_hindi"+c)).isChecked()) teachSubjects.add("hindi"+c);
                if(((CheckBox)root.findViewWithTag("check_telugu"+c)).isChecked()) teachSubjects.add("telugu"+c);
                if(((CheckBox)root.findViewWithTag("check_math"+c)).isChecked()) teachSubjects.add("math"+c);
                if(((CheckBox)root.findViewWithTag("check_physics"+c)).isChecked()) teachSubjects.add("physics"+c);
                if(((CheckBox)root.findViewWithTag("check_chemistry"+c)).isChecked()) teachSubjects.add("chemistry"+c);
                if(((CheckBox)root.findViewWithTag("check_biology"+c)).isChecked()) teachSubjects.add("biology"+c);
                if(((CheckBox)root.findViewWithTag("check_history"+c)).isChecked()) teachSubjects.add("history"+c);
                if(((CheckBox)root.findViewWithTag("check_geography"+c)).isChecked()) teachSubjects.add("geography"+c);

                // if the mentor can teach atleast one subject in a class, the mentor is said to teach that class.
                if(temp < teachSubjects.size()){
                    teachClasses.add(c);
                    temp = teachSubjects.size();
                }

            }

            // if the mentor didn't select any subjects, show a toast
            if(teachSubjects.size() == 0){
                Toast.makeText(AuthSignupMentors2.this, "Please select atleast one subject ", Toast.LENGTH_SHORT).show();
                return;
            }

            // push mentor details to DB before taking the test, and set the noTest to 1 as he is going to take a test after signup
            DatabaseReference newMentorRef = FirebaseDatabase.getInstance().getReference("mentors").push();
            newMentorRef.setValue(new MentorDetails(
                    name,
                    email,
                    phone,
                    teachClasses,
                    prefLangs,
                    timeSlots,
                    regStudents,
                    teachSubjects,
                    1)
            ).addOnCompleteListener(task -> {

                // logging in the mentor
                sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(TYPE,"mentor");
                editor.putString(USER_ID, newMentorRef.getKey());
                editor.apply();

                // send the mentor to take the Test
                Intent i = new Intent(AuthSignupMentors2.this, Test.class);
                i.putExtra("firstTime",true);
                i.putExtra("mentorId", newMentorRef.getKey());
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            });
        });

    }

    private int dpAsPixels(int dp) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

//    private TextView getTextView(String data, int aboveId) {
//        TextView textView = new TextView(this);
//        RelativeLayout.LayoutParams params =
//                new RelativeLayout.LayoutParams(
//                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        int margin = dpAsPixels(16);
//        params.setMargins(margin, margin, 0, margin);
//        params.addRule(RelativeLayout.BELOW, aboveId);
//        textView.setTextSize(14);
//        textView.setLayoutParams(params);
//        textView.setText(data);
//        textView.setTextColor(ContextCompat.getColor(this, R.color.mentor_details));
//        return textView;
//    }
    private CheckBox getCheckBox(String subject, String data, int aboveId) {
        CheckBox check = new CheckBox((this));
        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        check.setLayoutParams(params);
        check.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.pink)));
        String tag = "check_"+subject+data;
        check.setTag(tag);
        check.setPadding(10,10, 10, 10);
        String text = "Class "+data;
        //check.setText(check.getTag().toString());
        check.setText(text);
        return check;
    }

    private MaterialCardView getCard(String subject, ArrayList<String> classes_options, int aboveId) {
        String title = subject.substring(0, 1).toUpperCase() + subject.substring(1);
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
        titleTextView.setText(title);
        int titlePadding = dpAsPixels(10);
        titleTextView.setPadding(titlePadding, titlePadding, titlePadding, titlePadding);
        titleTextView.setTextSize(22);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            titleTextView.setBackgroundColor(getColor(R.color.pink));
            titleTextView.setTextColor(getColor(R.color.white));
        }
        linearLayoutCard.addView(titleTextView);
        int prevId = titleTextView.getId();
        for (String class_option : classes_options) {
            CheckBox checkBox = getCheckBox(subject, class_option,prevId);
            //allCheckBoxes.add(checkBox);
            linearLayoutCard.addView(checkBox);
            prevId = checkBox.getId();
        }
        card.addView(linearLayoutCard);
        return card;
    }
}
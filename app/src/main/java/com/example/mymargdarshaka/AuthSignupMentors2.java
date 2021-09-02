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

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;
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

        Bundle extras = getIntent().getExtras();
        String name  = extras.getString("name");
        String email  = extras.getString("email");
        String phone = extras.getString("phone");

        ArrayList<String> prefLangs = extras.getStringArrayList("prefLangs");
        ArrayList<String> timeSlots = extras.getStringArrayList("timeSlots");

        // will be filled by matching
        HashMap<String,ArrayList<String>> regStudents = new HashMap<>();

        ArrayList<String> teachSubjects = new ArrayList<>();
        ArrayList<String> classes = new ArrayList<>();

        //@Aashrith - You'll have to use "getTag() or findViewByTag() instead of findViewById()
        //use the arrayLIst allCheckBoxes to check if atleast 1 checkbox is checked, inside onListener for Submit button
        //display toast if not checked

//        Button submit_button = findViewById(R.id.mentorsSignupButton2);
//        submit_button.setOnClickListener(view -> {
//
//            // helper variable for determining which classes a mentor can teach
//            int temp = 0;
//
//            if(((CheckBox)findViewById(R.id.check_english6)).isChecked()) teachSubjects.add("english6");
//            if(((CheckBox)findViewById(R.id.check_hindi6)).isChecked()) teachSubjects.add("hindi6");
//            if(((CheckBox)findViewById(R.id.check_telugu6)).isChecked()) teachSubjects.add("telugu6");
//            if(((CheckBox)findViewById(R.id.check_math6)).isChecked()) teachSubjects.add("math6");
//            if(((CheckBox)findViewById(R.id.check_science6)).isChecked()) teachSubjects.add("science6");
//            if(((CheckBox)findViewById(R.id.check_social6)).isChecked()) teachSubjects.add("social6");
//
//            // if the mentor can teach atleast one subject in a class, the mentor is said to teach that class.
//            if(temp < teachSubjects.size()){
//                classes.add("6");
//                temp = teachSubjects.size();
//            }
//
//            if(((CheckBox)findViewById(R.id.check_english7)).isChecked()) teachSubjects.add("english7");
//            if(((CheckBox)findViewById(R.id.check_hindi7)).isChecked()) teachSubjects.add("hindi7");
//            if(((CheckBox)findViewById(R.id.check_telugu7)).isChecked()) teachSubjects.add("telugu7");
//            if(((CheckBox)findViewById(R.id.check_math7)).isChecked()) teachSubjects.add("math7");
//            if(((CheckBox)findViewById(R.id.check_science7)).isChecked()) teachSubjects.add("science7");
//            if(((CheckBox)findViewById(R.id.check_social7)).isChecked()) teachSubjects.add("social7");
//
//            if(temp < teachSubjects.size()){
//                classes.add("7");
//                temp = teachSubjects.size();
//            }
//
//            if(((CheckBox)findViewById(R.id.check_english8)).isChecked()) teachSubjects.add("english8");
//            if(((CheckBox)findViewById(R.id.check_hindi8)).isChecked()) teachSubjects.add("hindi8");
//            if(((CheckBox)findViewById(R.id.check_telugu8)).isChecked()) teachSubjects.add("telugu8");
//            if(((CheckBox)findViewById(R.id.check_math8)).isChecked()) teachSubjects.add("math8");
//            if(((CheckBox)findViewById(R.id.check_science8)).isChecked()) teachSubjects.add("science8");
//            if(((CheckBox)findViewById(R.id.check_social8)).isChecked()) teachSubjects.add("social8");
//
//            if(temp < teachSubjects.size()){
//                classes.add("8");
//                temp = teachSubjects.size();
//            }
//
//            if(((CheckBox)findViewById(R.id.check_english9)).isChecked()) teachSubjects.add("english9");
//            if(((CheckBox)findViewById(R.id.check_hindi9)).isChecked()) teachSubjects.add("hindi9");
//            if(((CheckBox)findViewById(R.id.check_telugu9)).isChecked()) teachSubjects.add("telugu9");
//            if(((CheckBox)findViewById(R.id.check_math9)).isChecked()) teachSubjects.add("math9");
//            if(((CheckBox)findViewById(R.id.check_science9)).isChecked()) teachSubjects.add("science9");
//            if(((CheckBox)findViewById(R.id.check_social9)).isChecked()) teachSubjects.add("social9");
//
//            if(temp < teachSubjects.size()){
//                classes.add("9");
//                temp = teachSubjects.size();
//            }
//
//            if(((CheckBox)findViewById(R.id.check_english10)).isChecked()) teachSubjects.add("english10");
//            if(((CheckBox)findViewById(R.id.check_hindi10)).isChecked()) teachSubjects.add("hindi10");
//            if(((CheckBox)findViewById(R.id.check_telugu10)).isChecked()) teachSubjects.add("telugu10");
//            if(((CheckBox)findViewById(R.id.check_math10)).isChecked()) teachSubjects.add("math10");
//            if(((CheckBox)findViewById(R.id.check_science10)).isChecked()) teachSubjects.add("science10");
//            if(((CheckBox)findViewById(R.id.check_social10)).isChecked()) teachSubjects.add("social10");
//
//            if(temp < teachSubjects.size()){
//                classes.add("10");
//                temp = teachSubjects.size();
//            }
//
//            if(((CheckBox)findViewById(R.id.check_english11)).isChecked()) teachSubjects.add("english11");
//            if(((CheckBox)findViewById(R.id.check_hindi11)).isChecked()) teachSubjects.add("hindi11");
//            if(((CheckBox)findViewById(R.id.check_telugu11)).isChecked()) teachSubjects.add("telugu11");
//            if(((CheckBox)findViewById(R.id.check_math11)).isChecked()) teachSubjects.add("math11");
//            if(((CheckBox)findViewById(R.id.check_physics11)).isChecked()) teachSubjects.add("physics11");
//            if(((CheckBox)findViewById(R.id.check_chemistry11)).isChecked()) teachSubjects.add("chemistry11");
//            if(((CheckBox)findViewById(R.id.check_history11)).isChecked()) teachSubjects.add("history11");
//            if(((CheckBox)findViewById(R.id.check_geography11)).isChecked()) teachSubjects.add("geography11");
//
//            if(temp < teachSubjects.size()){
//                classes.add("11");
//                temp = teachSubjects.size();
//            }
//
//            if(((CheckBox)findViewById(R.id.check_english12)).isChecked()) teachSubjects.add("english12");
//            if(((CheckBox)findViewById(R.id.check_hindi12)).isChecked()) teachSubjects.add("hindi12");
//            if(((CheckBox)findViewById(R.id.check_telugu12)).isChecked()) teachSubjects.add("telugu12");
//            if(((CheckBox)findViewById(R.id.check_math12)).isChecked()) teachSubjects.add("math12");
//            if(((CheckBox)findViewById(R.id.check_physics12)).isChecked()) teachSubjects.add("physics12");
//            if(((CheckBox)findViewById(R.id.check_chemistry12)).isChecked()) teachSubjects.add("chemistry12");
//            if(((CheckBox)findViewById(R.id.check_history12)).isChecked()) teachSubjects.add("history12");
//            if(((CheckBox)findViewById(R.id.check_geography12)).isChecked()) teachSubjects.add("geography12");
//
//            if(temp < teachSubjects.size()){
//                classes.add("12");
//                temp = teachSubjects.size();
//            }
//
//            // push mentor details to DB before taking test, and set the noTest to 1 as he is going to take a test after signup
//            DatabaseReference newMentorRef = FirebaseDatabase.getInstance().getReference("mentors").push();
//            String key = newMentorRef.getKey();
//            newMentorRef.setValue(new MentorDetails(
//                    name,
//                    email,
//                    phone,
//                    classes,
//                    prefLangs,
//                    timeSlots,
//                    regStudents,
//                    teachSubjects,
//                    1)
//            ).addOnCompleteListener(task -> {
//                sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString(TYPE,"mentor");
//                editor.putString(USER_ID,key);
//                editor.apply();
//
//                // send the mentor to take the Test
//                Intent i = new Intent(AuthSignupMentors2.this, Test.class);
//                i.putExtra("firstTime",true);
//                i.putExtra("mentorId",key);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);
//            });
//        });

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
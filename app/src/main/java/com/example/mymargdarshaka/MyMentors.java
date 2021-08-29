package com.example.mymargdarshaka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyMentors extends AppCompatActivity {

    SharedPreferences sharedPreferences;
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
        setContentView(R.layout.activity_my_mentors);

        LinearLayout root = findViewById(R.id.root_linear);

        MaterialCardView card = getCard(
                "Physics Grade 11",
                "Someone Random",
                "9876543210",
                "test@test.com",
                "English",
                "Morning",
                R.id.card_view
        );

        MaterialCardView card2 = getCard(
                "Physics Grade 12",
                "Someone Random 2",
                "9876543211",
                "test@test.con",
                "Hindi",
                "Afternoon",
                card.getId()
        );

        root.addView(card);
        root.addView(card2);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        boolean x=getIntent().getBooleanExtra("noMentorsAssignedHere",true);

        String userid= FirebaseAuth.getInstance().getCurrentUser().toString();

        DatabaseReference usersRef = rootRef;
        usersRef.orderByChild("id").equalTo(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                setLoading(true);
                for(DataSnapshot users: snapshot.getChildren()){
//                    setLoading(true);
                    Log.e("User key", users.getKey());
                    Log.e("User val", users.getValue().toString());
//                    setLoading(false);

                    UserSchema user=users.getValue(UserSchema.class);

                    UserDetails details=user.getDetails();
                    int registered = details.regSubjects.size();
                    int total = details.intrSubjects.size();

                    if(total<registered){
                        ArrayList<String> remainingSubjects=new ArrayList<>();
                        for(int i=0;i<details.intrSubjects.size();i++){
                            if(!details.regSubjects.contains(details.intrSubjects.get(i))){
                                remainingSubjects.add(details.intrSubjects.get(i));
                            }
                        }

                    }
                }
//                setLoading(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MyMentors.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                Intent i = new Intent(MyMentors.this,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        if(x){
            Toast.makeText(MyMentors.this,"No mentors are available at this time",Toast.LENGTH_LONG).show();
        }

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
                    Intent i = new Intent(MyMentors.this,GuidelinesForStudents.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
                else if(choice.equals("My Mentors"))
                {
                    //code to shift to Mentor Details Page
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

                    Intent i = new Intent(MyMentors.this,MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }

                drawerLayout.closeDrawer(Gravity.START, true);
                return true;
            }
        });



//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.clear();
//                editor.commit();
//
//                Intent i = new Intent(MyMentors.this,MainActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);

//                firebaseDatabase=FirebaseDatabase.getInstance();
//                databaseReference=firebaseDatabase.getReference("users");
//                String key=databaseReference.push().getKey();
//                databaseReference=databaseReference.child(key);
//
//                ArrayList<String> intr=new ArrayList<>();
//                intr.add("Maths");
//                intr.add("Science");
//
//                ArrayList<String> times=new ArrayList<>();
//                times.add("10:30");
//
//                ArrayList<Pair<String,String>> reg=new ArrayList<>();
//                reg.add(new Pair<String,String>("Maths","mentorid1"));
//                reg.add(new Pair<String,String>("Science","mentorid2"));
//
//                UserDetails userSchema = new UserDetails("Somename","abc@gmail.com","9898989888","6","Hindi",intr,times,reg);
//                databaseReference.setValue(userSchema);

//                firebaseDatabase=FirebaseDatabase.getInstance();
//                databaseReference=firebaseDatabase.getReference("mentors");
//                String key=databaseReference.push().getKey();
//                databaseReference=databaseReference.child(key);
//
//                ArrayList<String> prefLangs=new ArrayList<>();
//                prefLangs.add("English");
//                prefLangs.add("Hindi");
//
//                ArrayList<String> classes=new ArrayList<>();
//                classes.add("8");
//                classes.add("9");
//                classes.add("10");
//
//                ArrayList<String> times=new ArrayList<>();
//                times.add("10:30");
//
//                ArrayList<String> teachSubjects=new ArrayList<>();
//                teachSubjects.add("Maths");
//                teachSubjects.add("Science");
//
//                ArrayList<String> regStudents=new ArrayList<>();
//                regStudents.add("student id 1");
//                regStudents.add("student id 2");
//
//                MentorDetails mentorSchema = new MentorDetails("MyName","abc@gmail.com","9898989888",classes,prefLangs,times,regStudents,teachSubjects);
//                databaseReference.setValue(mentorSchema);

//            }
//        });

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
            String title,
            String name,
            String phone,
            String email,
            String language,
            String timeSlot,
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


        LinearLayout linearLayoutPrimary = new LinearLayout(this);
        LinearLayout.LayoutParams linearLayoutPrimaryParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearLayoutPrimary.setOrientation(LinearLayout.VERTICAL);
        int linearLayoutPrimaryPadding = dpAsPixels(16);
        linearLayoutPrimary.setPadding(linearLayoutPrimaryPadding, linearLayoutPrimaryPadding, linearLayoutPrimaryPadding, linearLayoutPrimaryPadding);
        linearLayoutPrimary.setLayoutParams(linearLayoutPrimaryParams);


        TextView titleTextView = new TextView(this);
        RelativeLayout.LayoutParams titleTextViewParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        titleTextView.setLayoutParams(titleTextViewParams);
        titleTextView.setText(title);
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


        TextView nameTextView = getTextView(
                "Mentor - " + name,
                titleTextView.getId()
        );
        TextView mobileTextView = getTextView(
                "Mobile - " + phone,
                nameTextView.getId()
        );
        TextView emailTextView = getTextView(
                "Email - " + email,
                mobileTextView.getId()
        );
        TextView languageTextView = getTextView(
                "Language - " + language,
                emailTextView.getId()
        );
        TextView timeSlotTextView = getTextView(
                "Time slot - " + timeSlot,
                languageTextView.getId()
        );


        LinearLayout linearLayoutSecondary = new LinearLayout(this);
        linearLayoutSecondary.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams linearLayoutSecondaryParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearLayoutSecondaryParams.setMargins(8, 8, 8, 8);
        linearLayoutSecondary.setLayoutParams(linearLayoutSecondaryParams);


        MaterialButton phoneButton = new MaterialButton(
                this,
                null,
                R.attr.borderlessButtonStyle
        );
        RelativeLayout.LayoutParams phoneButtonParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        phoneButtonParams.setMargins(0, 0, 8, 0);
        phoneButton.setText("PHONE");


        MaterialButton emailButton = new MaterialButton(
                this,
                null, R.attr.borderlessButtonStyle
        );
        RelativeLayout.LayoutParams emailButtonParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        emailButtonParams.setMargins(0, 0, 8, 0);
        emailButton.setText("EMAIL");


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
}
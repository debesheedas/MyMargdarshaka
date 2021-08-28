package com.example.mymargdarshaka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class AuthSignupStudents2 extends AppCompatActivity {

    AutoCompleteTextView a;

    CheckBox english, math, hindi, telugu, physics, chemistry, biology, history, geography, science, social;
    Button submit_button;
    boolean x;
    private DatabaseReference rootRef;

    ArrayList<String> subjects = new ArrayList<>();
    ArrayList<Pair<String, String>> regSub = new ArrayList<>();

    Bundle extras;
    String name;
    String email;
    String class_selected;
    String language_selected;
    String phone;
    String time_selected;
    ArrayList<Map<String,String>> regs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_signup_students2);

        x=false;

        a = AuthSignupStudents1.text_view_class;
        String s = a.getEditableText().toString();

        english = (CheckBox) findViewById(R.id.check_english);
        math = (CheckBox) findViewById(R.id.check_math);
        hindi = (CheckBox) findViewById(R.id.check_hindi);
        telugu = (CheckBox) findViewById(R.id.check_telugu);
        physics = (CheckBox) findViewById(R.id.check_physics);
        chemistry = (CheckBox) findViewById(R.id.check_chemistry);
        biology = (CheckBox) findViewById(R.id.check_biology);
        science = (CheckBox) findViewById(R.id.check_science);
        social = (CheckBox) findViewById(R.id.check_social);
        history = (CheckBox) findViewById(R.id.check_history);
        geography = (CheckBox) findViewById(R.id.check_geography);

        english.setVisibility(View.VISIBLE);
        hindi.setVisibility(View.VISIBLE);
        math.setVisibility(View.VISIBLE);
        telugu.setVisibility(View.VISIBLE);

        if(s.equals("Class 11")||s.equals("Class 12"))
        {
            physics.setVisibility(View.VISIBLE);
            chemistry.setVisibility(View.VISIBLE);
            biology.setVisibility(View.VISIBLE);
            history.setVisibility(View.VISIBLE);
            geography.setVisibility(View.VISIBLE);
        }
        else
        {
            science.setVisibility(View.VISIBLE);
            social.setVisibility(View.VISIBLE);
        }


        submit_button=(Button) findViewById(R.id.studentSignupButton2);
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                extras = getIntent().getExtras();
                name  = extras.getString("name");
                email  = extras.getString("email");
                class_selected = extras.getString("class_selected");
                language_selected  = extras.getString("language_selected");
                phone = extras.getString("phone");
                time_selected  = extras.getString("time_selected");


                if(english.isChecked()){
                    subjects.add("english");
//                    regSub.add(new Pair<>("english", "mentorid1"));
                }
                if(hindi.isChecked()){
                    subjects.add("hindi");
//                    regSub.add(new Pair<>("english", "mentorid2"));
                }
                if(telugu.isChecked()){
                    subjects.add("telugu");
//                    regSub.add(new Pair<>("english", "mentorid3"));
                }
                if(math.isChecked()){
                    subjects.add("math");
                }
                if(physics.isChecked()){
                    subjects.add("physics");
                }
                if(chemistry.isChecked()){
                    subjects.add("chemistry");
                }
                if(biology.isChecked()){
                    subjects.add("biology");
                }
                if(history.isChecked()){
                    subjects.add("history");
                }
                if(geography.isChecked()){
                    subjects.add("geography");
                }
                if(social.isChecked()){
                    subjects.add("social");
                }
                if(science.isChecked()){
                    subjects.add("science");
                }

                DatabaseReference newStudentRef = FirebaseDatabase.getInstance().getReference("users").push();

//                newStudentRef.setValue(new UserSchema(newStudentRef.getKey(), name, email, phone, class_selected, language_selected, subjects, time_selected, regSub));

                //newStudentRef.child("name").setValue(name);
                //newStudentRef.child("email").setValue(email);
                //newStudentRef.child("standard").setValue(class_selected);
                //newStudentRef.child("prefLang").setValue(language_selected);
                //newStudentRef.child("timeSlots").setValue(time_selected);
                //newStudentRef.child("intrSubjects").setValue(subjects);

                String key=newStudentRef.getKey();

                ArrayList<Pair<Integer,Pair<String,ArrayList<String>>>> arr = new ArrayList<>();

                rootRef = FirebaseDatabase.getInstance().getReference();

                DatabaseReference mentorsRef = rootRef.child("mentors");
                try {
                    mentorsRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            long l = snapshot.getChildrenCount();
                            long k = 0;
                            for (DataSnapshot child : snapshot.getChildren()) {
                                Log.e("hello: ", child.getValue().toString());
                                try {
                                    MentorDetails mentor = child.getValue(MentorDetails.class);

                                    ArrayList<String> classes = mentor.getClasses();
                                    ArrayList<String> prefLangs = mentor.getPrefLangs();
                                    ArrayList<String> teachSubjects = mentor.getTeachSubjects();
                                    ArrayList<String> timeSlots = mentor.getTimeSlots();
                                    ArrayList<Map<String, String>> regStudents = mentor.getRegStudents();
                                    if (regStudents == null) {
                                        regStudents = new ArrayList<>();
                                    }
                                    boolean fine = false;
                                    if (classes.contains(s)) {
                                        fine = true;
                                    }
                                    else{
                                        fine=false;
                                    }
                                    Log.e("1: ", String.valueOf(fine));
                                    if (fine) {
                                        if (prefLangs.contains(language_selected)) {
                                            fine = true;
                                        }
                                        else{
                                            fine=false;
                                        }
                                    }
                                    Log.e("2: ", String.valueOf(fine));
                                    if (fine) {
                                        for(String i:timeSlots){
                                            Log.e("fds",i);
                                        }
                                        Log.e("fds2",time_selected);
                                        if (timeSlots.contains(time_selected)) {
                                            fine = true;
                                        }
                                        else{
                                            fine=false;
                                        }
                                    }
                                    Log.e("3: ", String.valueOf(fine));
                                    ArrayList<String> t = new ArrayList<>();

                                    for (String i : teachSubjects) {
                                        System.out.println(i);
                                    }
                                    System.out.println();
                                    for (String i : subjects) {
                                        System.out.println(i);
                                    }

                                    if (fine) {
                                        fine = false;
                                        for (int i = 0; i < subjects.size(); i++) {
                                            if (teachSubjects.contains(subjects.get(i))) {
                                                fine = true;
                                                t.add(subjects.get(i));
                                            }
                                        }
                                    }
                                    Log.e("4: ", String.valueOf(fine));
                                    if (fine) {
                                        arr.add(Pair.create(regStudents.size(), Pair.create(child.getKey(), t)));
                                    }

                                    Log.e("k: ", String.valueOf(k));

                                    if (k >= l - 1) {
                                        assignMentors(arr, key, newStudentRef);
                                        Intent i = new Intent(AuthSignupStudents2.this, MyMentors.class);
                                        i.putExtra("noMentorsAssignedHere",x);
                                        i.putExtra("userid",key);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        break;
                                    }

                                    k++;
                                }
                                catch (Exception e){

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                catch (Exception e){
                    Log.e("Error ---- ",e.getMessage());
                }

            }

            public void assignMentors(ArrayList<Pair<Integer,Pair<String,ArrayList<String>>>> arr, String key, DatabaseReference newStudentRef){
                ArrayList<Pair<String,Pair<String, String>>> forMentors = new ArrayList<>();
                if(arr.size()>0) {
                    Collections.sort(arr, new Comparator<Pair<Integer, Pair<String, ArrayList<String>>>>() {
                        @Override
                        public int compare(Pair<Integer, Pair<String, ArrayList<String>>> p1, Pair<Integer, Pair<String, ArrayList<String>>> p2) {
                            return p1.first - p2.first;
                        }
                    });
                    ArrayList<String> tempSub = new ArrayList<>();
                    for (int i = 0; i < subjects.size(); i++) {
                        tempSub.add(subjects.get(i));
                    }

                    for (int i = 0; i < arr.size(); i++) {
                        for (int j = 0; j < arr.get(i).second.second.size(); j++) {
                            if (tempSub.contains(arr.get(i).second.second.get(j))) {
                                regSub.add(Pair.create(arr.get(i).second.first, arr.get(i).second.second.get(j)));
                                tempSub.remove(arr.get(i).second.second.get(j));
                                forMentors.add(Pair.create(arr.get(i).second.second.get(j),Pair.create(key, arr.get(i).second.first)));
//                                System.out.println(arr.get(i).second.second.get(j));
                            }
                        }
                        if (tempSub.size() == 0) {
                            break;
                        }
                    }
                }
                else{
                    x=true;
                }
                    UserDetails userSchema = new UserDetails(name,email,phone,class_selected,language_selected,subjects,time_selected,regSub);
                    newStudentRef.setValue(userSchema);

                    if(forMentors.size()>0){
                        rootRef = FirebaseDatabase.getInstance().getReference();

                        DatabaseReference mentorsRef = rootRef.child("mentors");

                        for(int i=0;i<forMentors.size();i++){
                            System.out.println("ajflakjflkjafkljk;lsklsdafl;aklsfkladsklsfjklaj;l" + forMentors.get(i).first);
                            System.out.println("safsadfsdafasfsadfsf" + forMentors.get(i).second.first);
                            System.out.println(" asfsafasfasdfdsafsafsfdsa" + forMentors.get(i).second.second);
                            Map<String,String> m=new HashMap<>();
                            m.put(forMentors.get(i).first, forMentors.get(i).second.first);
                            Map<String, Object> childUpdates = new HashMap<>();


                            mentorsRef.orderByChild(forMentors.get(i).second.second).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    regs=snapshot.getValue(MentorDetails.class).getRegStudents();
//                                    if(regs==null){

//                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
//                            regs.add(m);
                            childUpdates.put("regStudents", m);
                            mentorsRef.child(forMentors.get(i).second.second).setValue(childUpdates);
                        }

//                        mentorsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                for(DataSnapshot child: snapshot.getChildren()){
//                                    for(int i=0;i<forMentors.size();i++){
//                                        if(forMentors.get(i).second.second.equals(child.getKey())){
//                                                MentorDetails mentor = child.getValue(MentorDetails.class);
//
//                                                ArrayList<String> classes = mentor.getClasses();
//                                                ArrayList<String> prefLangs = mentor.getPrefLangs();
//                                                ArrayList<String> teachSubjects = mentor.getTeachSubjects();
//                                                ArrayList<String> timeSlots = mentor.getTimeSlots();
//                                                ArrayList<Map<String, String>> regStudents = mentor.getRegStudents();
//                                                if (regStudents == null) {
//                                                    regStudents = new ArrayList<>();
//                                                }
//
//                                                String name = mentor.getName();
//                                                String email = mentor.getEmail();
//                                                String phone = mentor.getPhone();
//
//                                                Map<String,String> m=new HashMap<>();
//                                                m.put(forMentors.get(i).first, forMentors.get(i).second.first);
//
//                                                regStudents.add(m);
//
//                                                mentorsRef.child(child.getKey()).child("name").setValue(name);
//                                                mentorsRef.child(child.getKey()).child("phone").setValue(phone);
//                                                mentorsRef.child(child.getKey()).child("email").setValue(email);
//                                                mentorsRef.child(child.getKey()).child("name").setValue(name);
//                                                mentorsRef.child(child.getKey()).child("classes").setValue(classes);
//                                                mentorsRef.child(child.getKey()).child("prefLangs").setValue(prefLangs);
//                                                mentorsRef.child(child.getKey()).child("teachSubjects").setValue(teachSubjects);
//                                                mentorsRef.child(child.getKey()).child("regStudents").setValue(regStudents);
//                                                mentorsRef.child(child.getKey()).child("timeSlots").setValue(timeSlots);
//
//                                        }
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });

                    }

                Intent i = new Intent(AuthSignupStudents2.this, MyMentors.class);
                i.putExtra("noMentorsAssignedHere",x);
                i.putExtra("userid",key);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }


        });



    }
}
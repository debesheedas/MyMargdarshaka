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

    Bundle extras = getIntent().getExtras();
    String name  = extras.getString("name");
    String email  = extras.getString("email");
    String class_selected = extras.getString("class_selected");
    String language_selected  = extras.getString("language_selected");
    String phone = extras.getString("phone");
    String time_selected  = extras.getString("time_selected");

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

//                firebaseDatabase=FirebaseDatabase.getInstance();
//                databaseReference=firebaseDatabase.getReference("users");
                String key=newStudentRef.getKey();
//                newStudentRef=newStudentRef.child(key);


                ArrayList<Pair<Integer,Pair<String,ArrayList<String>>>> arr = new ArrayList<>();

                rootRef = FirebaseDatabase.getInstance().getReference();

                DatabaseReference mentorsRef = rootRef.child("mentors");
                mentorsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long l=snapshot.getChildrenCount();
                        long k=0;
                        for(DataSnapshot child: snapshot.getChildren()){
                            Log.e("Mentor key", child.getKey());
                            Log.e("Mentor val", child.getValue().toString());

                            MentorDetails mentor=child.getValue(MentorDetails.class);

                            ArrayList<String> classes=mentor.getClasses();
                            ArrayList<String> prefLangs=mentor.getPrefLangs();
                            ArrayList<String> teachSubjects=mentor.getTeachSubjects();
                            ArrayList<String> timeSlots=mentor.getTimeSlots();
                            ArrayList<String> regStudents=mentor.getRegStudents();
                            boolean fine=false;
                            if(classes.contains(s)){
                                fine=true;
                            }
                            if(fine){
                                if(prefLangs.contains(language_selected)){
                                    fine=true;
                                }
                            }
                            if(fine){
                                if(timeSlots.contains(time_selected)){
                                    fine=true;
                                }
                            }
                            ArrayList<String> t=new ArrayList<>();
                            if(fine){
                                fine=false;
                                for(int i=0;i<subjects.size();i++){
                                    if(teachSubjects.contains(subjects.get(i))){
                                        fine=true;
                                        t.add(subjects.get(i));
                                    }
                                }
                            }
                            if(fine){
                                arr.add(Pair.create(regStudents.size(),Pair.create(child.getKey(),t)));
                            }

                            if(k==l-1 && fine){
                                assignMentors(arr,key,newStudentRef);
                            }

                            k++;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            public void assignMentors(ArrayList<Pair<Integer,Pair<String,ArrayList<String>>>> arr, String key, DatabaseReference newStudentRef){
                if(arr.size()>0){
                    Collections.sort(arr,new Comparator<Pair<Integer,Pair<String,ArrayList<String>>>>(){
                        @Override
                        public int compare(Pair<Integer,Pair<String,ArrayList<String>>> p1, Pair<Integer,Pair<String,ArrayList<String>>> p2) {
                            return p1.first-p2.first;
                        }
                    });
                    ArrayList<String> tempSub=new ArrayList<>();
                    for(int i=0;i<subjects.size();i++){
                        tempSub.add(subjects.get(i));
                    }
                    ArrayList<Pair<String,String>> forMentors=new ArrayList<>();
                    for(int i=0;i<arr.size();i++){
                        for(int j=0;j<arr.get(i).second.second.size();j++){
                            if(tempSub.contains(arr.get(i).second.second.get(j))){
                                regSub.add(Pair.create(arr.get(i).second.first,arr.get(i).second.second.get(j)));
                                tempSub.remove(arr.get(i).second.second.get(j));
                                forMentors.add(Pair.create(key,arr.get(i).second.first));
                            }
                        }
                        if(tempSub.size()==0){
                            break;
                        }
                    }
                    UserDetails userSchema = new UserDetails(name,email,phone,class_selected,language_selected,subjects,time_selected,regSub);
                    newStudentRef.setValue(userSchema);

                    if(forMentors.size()>0){
                        rootRef = FirebaseDatabase.getInstance().getReference();

                        DatabaseReference mentorsRef = rootRef.child("mentors");
                        mentorsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot child: snapshot.getChildren()){
                                    for(int i=0;i<forMentors.size();i++){
                                        if(forMentors.get(i).second.equals(child.getKey())){
                                            MentorDetails mentor=child.getValue(MentorDetails.class);

                                            ArrayList<String> classes=mentor.getClasses();
                                            ArrayList<String> prefLangs=mentor.getPrefLangs();
                                            ArrayList<String> teachSubjects=mentor.getTeachSubjects();
                                            ArrayList<String> timeSlots=mentor.getTimeSlots();
                                            ArrayList<String> regStudents=mentor.getRegStudents();
                                            String name=mentor.getName();
                                            String email=mentor.getEmail();
                                            String phone=mentor.getPhone();

                                            regStudents.add(forMentors.get(i).first);

                                            rootRef.child(child.getKey()).child("name").setValue(name);
                                            rootRef.child(child.getKey()).child("phone").setValue(phone);
                                            rootRef.child(child.getKey()).child("email").setValue(email);
                                            rootRef.child(child.getKey()).child("name").setValue(name);
                                            rootRef.child(child.getKey()).child("classes").setValue(classes);
                                            rootRef.child(child.getKey()).child("prefLangs").setValue(prefLangs);
                                            rootRef.child(child.getKey()).child("teachSubjects").setValue(teachSubjects);
                                            rootRef.child(child.getKey()).child("regStudents").setValue(regStudents);
                                            rootRef.child(child.getKey()).child("timeSlots").setValue(timeSlots);

                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }

                }
                else{
                    x=true;
                }

                Intent i = new Intent(AuthSignupStudents2.this, MyMentors.class);
                i.putExtra("noMentorsAssignedHere",x);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }


        });



    }
}
package com.example.mymargdarshaka;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MentorMatching {

    public static void match(String newMentorKey){

       // Retrieve Mentor Detais with the key
       DatabaseReference newMentorRef = FirebaseDatabase.getInstance().getReference("mentors").child(newMentorKey);
       newMentorRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DataSnapshot> task) {
               if(task.isSuccessful()){

                   MentorDetails newMentor = task.getResult().getValue(MentorDetails.class);
                   ArrayList<ArrayList<String>> matches = new ArrayList<ArrayList<String>>();
                   DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

                   usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {

                           for(DataSnapshot user : dataSnapshot.getChildren()){

                               UserDetails student = user.getValue(UserDetails.class);

                               if(newMentor.getClasses().contains(student.getStandard()) && newMentor.getTimeSlots().contains(student.getTimeSlot()) && newMentor.getPrefLangs().contains(student.getPrefLang())){
                                   // this student is applicable

                                   // also add condition when intrSub is null
                                   for(String intrSub : student.getIntrSubjects()){

                                       if(newMentor.getTeachSubjects().contains(intrSub)){
                                           matches.add(new ArrayList<String>(){
                                               {
                                                   add(user.getKey());
                                                   add(intrSub);
                                               }
                                           });
                                       }
                                   }
                               }
                           }

                           // all the matches with the students
                           for(ArrayList<String> match : matches){

                               String studentId = match.get(0);
                               String subject = match.get(1);

                               // add student to mentor list
                               if(newMentor.getRegStudents().get(subject) == null){
                                   newMentor.getRegStudents().put(subject, new ArrayList<String>());
                               }
                               newMentor.getRegStudents().get(subject).add(studentId);

                                // add mentor to student list
                               usersRef.child(studentId).child("regSubjects").child(subject).setValue(newMentorKey);

                           }


                       }

                       @Override
                       public void onCancelled(DatabaseError databaseError) {
                           System.out.println("The read failed: " + databaseError.getCode());
                       }
                   });

               }else{
                   Log.e("firebase", "Error getting data", task.getException());
               }
           }
       });

    }
}

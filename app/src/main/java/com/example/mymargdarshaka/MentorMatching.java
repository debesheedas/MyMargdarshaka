package com.example.mymargdarshaka;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MentorMatching {

  public static void match(String newMentorKey, Test context) {
    DatabaseReference newMentorRef =
        FirebaseDatabase.getInstance().getReference("mentors").child(newMentorKey);
    newMentorRef
        .get()
        .addOnCompleteListener(
            new OnCompleteListener<DataSnapshot>() {
              @Override
              public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {

                  MentorDetails newMentor = task.getResult().getValue(MentorDetails.class);
                  Log.e("newMentor : ", newMentor.toString());
                  ArrayList<ArrayList<String>> matches = new ArrayList<ArrayList<String>>();
                  DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

                  usersRef.addListenerForSingleValueEvent(
                      new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                          Log.e("MENTOR : ", newMentor.toString());

                          for (DataSnapshot user : dataSnapshot.getChildren()) {

                            UserDetails student = user.getValue(UserDetails.class);

                            Log.e("student : ", student.toString());

                            if (newMentor.getClasses().contains(student.getStandard())
                                && newMentor.getTimeSlots().contains(student.getTimeSlot())
                                && newMentor.getPrefLangs().contains(student.getPrefLang())) {
                              // this student is applicable
                              ArrayList<String> temp = new ArrayList<>();
                              // also add condition when intrSub is null
                              if (student.getIntrSubjects() == null) continue;

                              for (String intrSub : student.getIntrSubjects()) {

                                if (newMentor.getTeachSubjects().contains(intrSub)) {

                                  if (student.getRegSubjects() == null) {
                                    student.setRegSubjects(new HashMap<>());
                                  }
                                  student.getRegSubjects().put(intrSub, newMentorKey);

                                  if (newMentor.getRegStudents() == null) {
                                    newMentor.setRegStudents(
                                        new HashMap<String, ArrayList<String>>());
                                  }

                                  if (newMentor.getRegStudents().get(intrSub) == null) {
                                    newMentor
                                        .getRegStudents()
                                        .put(intrSub, new ArrayList<String>());
                                  }
                                  newMentor.getRegStudents().get(intrSub).add(user.getKey());

                                } else {
                                  temp.add(intrSub);
                                }
                              }

                              student.setIntrSubjects(temp);
                              usersRef.child(user.getKey()).setValue(student);
                            }
                          }
                          //
                          newMentorRef.setValue(newMentor);

                          Intent i = new Intent(context, MyStudents.class);
                          i.putExtra("firstTime", true);
                          i.putExtra("mentorId", newMentorKey);
                          context.startActivity(i);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                          System.out.println("The read failed: " + databaseError.getCode());
                        }
                      });

                } else {
                  Log.e("firebase", "Error getting data", task.getException());
                }
              }
            });
  }
}

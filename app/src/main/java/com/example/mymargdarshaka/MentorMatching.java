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

                  // matches stores arraylist of students he can teach along with the subject
                  ArrayList<ArrayList<String>> matches = new ArrayList<ArrayList<String>>();

                  DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                  usersRef.addListenerForSingleValueEvent(
                      new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                          // iterate for each student and verify the matching criteria
                          for (DataSnapshot user : dataSnapshot.getChildren()) {

                            UserDetails student = user.getValue(UserDetails.class);

                            // if the student's class, timeslot, language matches then the student is applicable
                            if (newMentor.getClasses().contains(student.getStandard())
                                && newMentor.getTimeSlots().contains(student.getTimeSlot())
                                && newMentor.getPrefLangs().contains(student.getPrefLang())) {
                              // this student is applicable

                              // stores all the subjects that the mentor can't teach, this is set back as interestedSubjects of the student
                              ArrayList<String> cantTeach = new ArrayList<>();

                              // intrSubjects only contains subjects that he is interested in but not registered for
                              // if the student had registered for all the subjects he is interested in, skip him
                              // null check as it can be empty
                              if (student.getIntrSubjects() == null) continue;

                              // iterate for each interested subject, if the mentor can teach the interested subject, add the students and the subject to matches
                              for (String intrSub : student.getIntrSubjects()) {

                                if (newMentor.getTeachSubjects().contains(intrSub)) {
                                  // mentor can teach the subject

                                  // null checks before adding the data

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
                                  cantTeach.add(intrSub);
                                }
                              }

                              student.setIntrSubjects(cantTeach);
                              // set the updated data in the DB
                              usersRef.child(user.getKey()).setValue(student);
                            }
                          }
                          // set the updated data in the DB
                          newMentorRef.setValue(newMentor);

                          // next send him to MyStudents
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

package com.example.mymargdarshaka;

import java.util.ArrayList;
import java.util.HashMap;

// mentor schema for the database
public class MentorSchema {
  private String id;
  private MentorDetails mentorDetails;

  public MentorSchema() {}

  public MentorSchema(
      String id,
      String name,
      String email,
      String phone,
      ArrayList<String> classes,
      ArrayList<String> prefLangs,
      ArrayList<String> timeSlots,
      HashMap<String, ArrayList<String>> regStudents,
      ArrayList<String> teachSubjects,
      int noTests) {
    this.id = id;
    this.mentorDetails =
        new MentorDetails(
            name, email, phone, classes, prefLangs, timeSlots, regStudents, teachSubjects, noTests);
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setMentorDetails(MentorDetails mentorDetails) {
    this.mentorDetails = mentorDetails;
  }

  public String getId() {
    return this.id;
  }

  public MentorDetails getDetails() {
    return this.mentorDetails;
  }

  public String getName() {
    return this.mentorDetails.getName();
  }

  public String getPhone() {
    return this.mentorDetails.getPhone();
  }

  public String getEmail() {
    return this.mentorDetails.getEmail();
  }

  public ArrayList<String> getClasses() {
    return this.mentorDetails.getClasses();
  }

  public HashMap<String, ArrayList<String>> getRegStudents() {
    return this.mentorDetails.getRegStudents();
  }

  public ArrayList<String> getTimeSlots() {
    return this.mentorDetails.getTimeSlots();
  }

  public ArrayList<String> getPrefLangs() {
    return this.mentorDetails.getPrefLangs();
  }

  public ArrayList<String> getTeachSubjects() {
    return this.mentorDetails.getTeachSubjects();
  }

}

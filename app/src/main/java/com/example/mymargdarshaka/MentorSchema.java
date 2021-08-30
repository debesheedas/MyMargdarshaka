package com.example.mymargdarshaka;

import android.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class MentorSchema {
    private String id;
    private MentorDetails mentorDetails;

    public MentorSchema(){}

    public MentorSchema(String id, String name, String email, String phone, ArrayList<String> classes, ArrayList<String> prefLangs, ArrayList<String> timeSlots, HashMap<String, ArrayList<String>> regStudents, ArrayList<String> teachSubjects){
        this.id=id;
        this.mentorDetails=new MentorDetails(name,email,phone,classes,prefLangs,timeSlots,regStudents,teachSubjects);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMentorDetails(MentorDetails mentorDetails) {
        this.mentorDetails = mentorDetails;
    }

    public String getId(){
        return this.id;
    }

    public MentorDetails getDetails(){
        return this.mentorDetails;
    }

    public String getName(){
        return this.mentorDetails.getName();
    }

    public String getPhone(){
        return this.mentorDetails.getPhone();
    }

    public String getEmail(){
        return this.mentorDetails.getEmail();
    }

    public ArrayList<String> getClasses(){
        return this.mentorDetails.getClasses();
    }

    public HashMap<String,ArrayList<String>> getRegStudents(){
        return this.mentorDetails.getRegStudents();
    }

    public ArrayList<String> getTimeSlots(){
        return this.mentorDetails.getTimeSlots();
    }

    public ArrayList<String> getPrefLangs(){
        return this.mentorDetails.getPrefLangs();
    }

    public ArrayList<String> getTeachSubjects(){
        return this.mentorDetails.getTeachSubjects();
    }

//    public void setName(String name) {
//        this.mentorDetails.setName(name);
//    }
//
//    public void setEmail(String email) {
//        this.mentorDetails.setEmail(email);
//    }
//
//    public void setPhone(String phone) {
//        this.mentorDetails.setPhone(phone);
//    }
//
//    public void setClasses(ArrayList<String> classes) {
//        this.mentorDetails.setClasses(classes);
//    }
//
//    public void setPrefLangs(ArrayList<String> prefLangs) {
//        this.mentorDetails.setPrefLangs(prefLangs);
//    }
//
//    public void setTimeSlots(ArrayList<String> timeSlots) {
//        this.mentorDetails.setTimeSlots(timeSlots);
//    }
//
//    public void setRegStudents(ArrayList<String> regStudents) {
//        this.mentorDetails.setRegStudents(regStudents);
//    }
//
//    public void setTeachSubjects(ArrayList<String> teachSubjects) {
//        this.mentorDetails.setTeachSubjects(teachSubjects);
//    }

}

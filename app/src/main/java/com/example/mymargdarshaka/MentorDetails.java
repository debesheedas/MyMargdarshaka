package com.example.mymargdarshaka;

import java.util.ArrayList;

public class MentorDetails {

    String name,email,phone;
    ArrayList<String> classes;
    ArrayList<String> prefLangs;
    ArrayList<String> timeSlots;
    ArrayList<String> regStudents;
    ArrayList<String> teachSubjects;

    public MentorDetails(){}

    public MentorDetails(String name, String email, String phone, ArrayList<String> classes, ArrayList<String> prefLangs, ArrayList<String> timeSlots, ArrayList<String> regStudents, ArrayList<String> teachSubjects){
        this.name=name;
        this.email=email;
        this.phone=phone;
        this.classes=classes;
        this.prefLangs=prefLangs;
        this.timeSlots=timeSlots;
        this.regStudents=regStudents;
        this.teachSubjects=teachSubjects;
    }

    public String getName(){
        return name;
    }

    public String getPhone(){
        return phone;
    }

    public String getEmail(){
        return email;
    }

    public ArrayList<String> getClasses(){
        return classes;
    }

    public ArrayList<String> getRegStudents(){
        return regStudents;
    }

    public ArrayList<String> getTimeSlots(){
        return timeSlots;
    }

    public ArrayList<String> getPrefLangs(){
        return prefLangs;
    }

    public ArrayList<String> getTeachSubjects(){
        return teachSubjects;
    }

}

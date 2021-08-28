package com.example.mymargdarshaka;

import java.util.ArrayList;

public class MentorDetails {

    private String name,email,phone;
    private ArrayList<String> classes;
    private ArrayList<String> prefLangs;
    private ArrayList<String> timeSlots;
    private ArrayList<String> regStudents;
    private ArrayList<String> teachSubjects;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setClasses(ArrayList<String> classes) {
        this.classes = classes;
    }

    public void setPrefLangs(ArrayList<String> prefLangs) {
        this.prefLangs = prefLangs;
    }

    public void setTimeSlots(ArrayList<String> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public void setRegStudents(ArrayList<String> regStudents) {
        this.regStudents = regStudents;
    }

    public void setTeachSubjects(ArrayList<String> teachSubjects) {
        this.teachSubjects = teachSubjects;
    }
}

package com.example.mymargdarshaka;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class UserDetails {

    String name,email,phone,standard,prefLang, timeSlot;
    ArrayList<String> intrSubjects;
    HashMap<String,String> regSubjects;

    public UserDetails(){}

    public UserDetails(String name, String email, String phone, String standard, String prefLang, ArrayList<String> intrSubjects, String timeSlot, HashMap<String,String> regSubjects){
        this.name=name;
        this.email=email;
        this.phone=phone;
        this.standard=standard;
        this.prefLang=prefLang;
        this.intrSubjects=intrSubjects;
        this.regSubjects=regSubjects;
        this.timeSlot=timeSlot;
    }

    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }
    public String getPhone(){
        return phone;
    }
    public String getStandard(){ return standard; }
    public String getPrefLang(){
        return prefLang;
    }
    public ArrayList<String> getIntrSubjects(){
        return intrSubjects;
    }
    public String getTimeSlot(){ return timeSlot; }
    //public ArrayList<Pair<String,String>> getRegSubjects(){
    //    return regSubjects;
    //}

    public HashMap<String, String> getRegSubjects(){
        return regSubjects;
    }

}

package com.example.mymargdarshaka;

import android.util.Pair;

import java.util.ArrayList;

public class UserSchema {
    String id;
    UserDetails userDetails;

    public UserSchema(){}

    public UserSchema(String id, String name, String email, String phone, String standard, String prefLang, ArrayList<String> intrSubjects, String timeSlot, ArrayList<Pair<String,String>> regSubjects){
        this.id=id;
        this.userDetails=new UserDetails(name,email,phone,standard,prefLang,intrSubjects,timeSlot,regSubjects);
    }

    public String getId(){
        return this.id;
    }

    public UserDetails getDetails(){
        return this.userDetails;
    }
}

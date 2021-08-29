package com.example.mymargdarshaka;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class UserSchema {
    String id;
    UserDetails userDetails;

    public UserSchema(){}

    public UserSchema(String id, String name, String email, String phone, String standard, String prefLang, ArrayList<String> intrSubjects, String timeSlot, HashMap<String,String> regSubjects){
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

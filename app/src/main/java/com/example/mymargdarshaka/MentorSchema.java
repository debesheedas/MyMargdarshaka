package com.example.mymargdarshaka;

import android.util.Pair;

import java.util.ArrayList;

public class MentorSchema {
    String id;
    MentorDetails mentorDetails;

    public MentorSchema(){}

    public MentorSchema(String id, String name, String email, String phone, ArrayList<String> classes, ArrayList<String> prefLangs, ArrayList<String> timeSlots, ArrayList<String> regStudents, ArrayList<String> teachSubjects){
        this.id=id;
        this.mentorDetails=new MentorDetails(name,email,phone,classes,prefLangs,timeSlots,regStudents,teachSubjects);
    }

    public String getId(){
        return this.id;
    }

    public MentorDetails getDetails(){
        return this.mentorDetails;
    }

}

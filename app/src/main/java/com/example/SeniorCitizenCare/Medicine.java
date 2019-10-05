package com.example.SeniorCitizenCare;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import java.util.List;

public class Medicine {

    //int UserId;
    //private String name;
    private String time;
    private List<Integer> days;

    public Medicine(){
        //public no arg constructor needed
    }

    public Medicine( String time, List<Integer> days) {
      //  UserId = userId;
        //MedicineName = medicineName;
      //  this.name = name;
        this.time = time;
        this.days = days;
        //Username = username;
    }

   // public int getUserId() {return UserId;}

    public String getTime() {
        return time;
    }

    public List<Integer> getDays() {
        return days;
    }

    //public String getName() {return name;}


}

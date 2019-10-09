package com.example.SeniorCitizenCare;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Medicine {

    //int UserId;
    private String name;
    private ArrayList<Date> selecteddays;
    private List<Integer> days;
    private ArrayList<Integer> selectedtimings;

    public Medicine(){
        //public no arg constructor needed
    }

    public Medicine(String name, ArrayList<Date> selecteddays, List<Integer> days, ArrayList<Integer> selectedtimings) {
      //  UserId = userId;
        this.name = name;
      //  this.name = name;
        this.selecteddays = selecteddays;
        this.days = days;
        this.selectedtimings = selectedtimings;
        //Username = username;
    }

   // public int getUserId() {return UserId;}


    public ArrayList<Date> getSelecteddays() {return selecteddays;}

    public List<Integer> getDays() {
        return days;
    }

    public String getName() {return name;}

    public ArrayList<Integer> getSelectedtimings(){
        return selectedtimings;
    }


}

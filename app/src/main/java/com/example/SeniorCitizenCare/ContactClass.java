package com.example.SeniorCitizenCare;

public class ContactClass {
    int mImageResource;
    String mName;
    String mNumber;
    String mRelation;

    public ContactClass(){

    }

    public ContactClass(int mImageResource, String mName, String mNumber, String mRelation) {
        this.mImageResource = mImageResource;
        this.mName = mName;
        this.mNumber = mNumber;
        this.mRelation = mRelation;
    }

    public int getmImageResource() {
        return mImageResource;
    }

    public String getmName() {
        return mName;
    }

    public String getmNumber() {
        return mNumber;
    }

    public String getmRelation() {
        return mRelation;
    }
}

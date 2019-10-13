package com.example.SeniorCitizenCare;

public class ContactClassSmall {

    int mImageResource;
    String mName;
    String mNumber;

    public ContactClassSmall(int mImageResource, String mName, String mNumber) {
        this.mImageResource = mImageResource;
        this.mName = mName;
        this.mNumber = mNumber;
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
}

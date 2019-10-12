package com.example.SeniorCitizenCare;

public class YogaClass {

    private int mImageResource;
    private String mName;
    private String mDescription;

    public YogaClass(int mImageResource, String mName, String mDescription) {
        this.mImageResource = mImageResource;
        this.mName = mName;
        this.mDescription = mDescription;
    }

    public int getmImageResource() {
        return mImageResource;
    }

    public String getmName() {
        return mName;
    }

    public String getmDescription() {
        return mDescription;
    }
}

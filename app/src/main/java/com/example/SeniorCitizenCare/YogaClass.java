package com.example.SeniorCitizenCare;

public class YogaClass {

    private int mImageResource;
    private String mName;
    private String mDescription;
    private String mLink;

    public YogaClass(int mImageResource, String mName, String mDescription, String mLink) {
        this.mImageResource = mImageResource;
        this.mName = mName;
        this.mDescription = mDescription;
        this.mLink = mLink;
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

    public String getmLink() {
        return mLink;
    }
}

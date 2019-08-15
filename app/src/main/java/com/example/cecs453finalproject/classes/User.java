package com.example.cecs453finalproject.classes;


/*
 *
 * Created on 07/10/19
 * By Tylar Simone and Brandon Mitchell
 * Califonia State University Long Beach.
 * CECS 453
 * Professor Arjang Fahim.
 *
 * Expense Tracker
 *
 * */

import java.io.Serializable;

public class User implements Serializable {

    public static final String TAG = "User";
    private static final long serialVersionUID = -7066781375376919335L;

    private long mId;
    private String mUsername;
    private String mPassword;
    private String mEmail;
    private double mIncome;

    public User () {}

    public User(String username, String password, String email, double income)
    {
        this.mUsername = username;
        this.mPassword = password;
        this.mEmail = email;
        this.mIncome = income;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public double getIncome() {
        return mIncome;
    }

    public void setIncome(double mIncome) {
        this.mIncome = mIncome;
    }
}

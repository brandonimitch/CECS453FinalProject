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

import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction implements Serializable {

    private static final String TAG = "Transaction";
    private static final long serialVersionUID = 4084819698284796501L;

    private long mId;
    private String mDate;
    private String mDescr;
    private String mCategory;
    private int mType;
    private double mAmount;
    private User mUser;
    private Date dateObject;

    public Transaction() {}

    public Transaction( String date, String descr, String category, int type, double amount){
        this.mDate = date;
        this.mDescr = descr;
        this.mCategory = category;
        this.mType = type;
        this.mAmount = amount;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
        setDateObject(mDate);
    }

    public String getDescr() {
        return mDescr;
    }

    public void setDescr(String mDescr) {
        this.mDescr = mDescr;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public int getType() {
        return mType;
    }

    public void setType(int mType) {
        this.mType = mType;
    }

    public double getAmount() {
        return mAmount;
    }

    public void setAmount(double mAmount) {
        this.mAmount = mAmount;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User mUser) {
        this.mUser = mUser;
    }

    public Date getDateObject() {
        return dateObject;
    }

    public void setDateObject(String dateString) {
        try {
            dateObject = new SimpleDateFormat("MM/dd/yyyy").parse(dateString);
            Log.e(TAG,"Parsed Date");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setDateObject(Date dateObject)
    {
        this.dateObject = dateObject;
    }
}


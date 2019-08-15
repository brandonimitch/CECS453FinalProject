package com.example.cecs453finalproject.classes;

import java.io.Serializable;


public class Expense implements Serializable {

    private static final String TAG = "Expense";

    private long mId;
    private String mCategoryName;
    private User mUser;
    private Double mExpense;

    // Default empty constructor.
    public Expense() {}

    public Expense(String name, Double expense) {
        this.mCategoryName = name;
        this.mExpense = expense;
    }

    public long getId() { return mId; }

    public void setId(long mId) {
        this.mId = mId;
    }

    public void setExpense(Double amount) { this.mExpense = amount; }

    public Double getExpense() { return mExpense; }

    public String getCategoryName() {
        return mCategoryName;
    }

    public void setName(String mName) {
        this.mCategoryName = mName;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User mUser) {
        this.mUser = mUser;
    }

}

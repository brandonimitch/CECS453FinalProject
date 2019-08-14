package com.example.cecs453finalproject.classes;

import java.io.Serializable;

public class Category implements Serializable {

    private static final String TAG = "Category";

    private long mId;
    private String mName;
    private User mUser;

    public Category() {}

    public Category( String name){
        this.mName = name;
    }

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User mUser) {
        this.mUser = mUser;
    }
}

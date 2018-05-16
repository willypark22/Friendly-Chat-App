package com.google.firebase.udacity.friendlychat;

public class User {
    public String userName;
    public String mUid;
  //  public String mDirectMessage;

    public User() {}  // Needed for Firebase

    public User(String name,String uid) {
        this.userName = name;
        this.mUid = uid;


    }

    public String getName() { return userName; }

    public void setName(String name) { this.userName = name; }

    public String getUid() { return mUid; }

    public void setUid(String uid) { this.mUid = uid; }




}
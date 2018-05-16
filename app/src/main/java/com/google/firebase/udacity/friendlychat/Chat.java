package com.google.firebase.udacity.friendlychat;


public class Chat {
    public String chatName;


    public Chat() {
    }  // Needed for Firebase

    public Chat(String name) {
        this.chatName = name;

    }

    public String getName() {
        return chatName;
    }

    public void setName(String name) {
        this.chatName = name;
    }

}
package com.google.firebase.udacity.friendlychat;


public class Chat {
    public String chatName;
    public String typingI;


    public Chat() {
    }  // Needed for Firebase

    public Chat(String name,String typ) {
        this.chatName = name;
        this.typingI = typ;

    }

    public String getName() {
        return chatName;
    }

    public void setName(String name) {
        this.chatName = name;
    }

    public String getTyping() {
        return typingI;
    }

    public void setTyping(String typ) {
        this.typingI = typ;
    }

}
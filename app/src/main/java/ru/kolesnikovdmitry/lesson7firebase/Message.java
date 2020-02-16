package ru.kolesnikovdmitry.lesson7firebase;

import android.provider.ContactsContract;

import java.util.Date;

public class Message {
    public String textMessage;
    public String userName;
    private long time;
    public boolean likeMessage;

    public Message() {}

    public Message(String userName, String textMessage) {
        this.userName = userName;
        this.textMessage = textMessage;
        this.likeMessage = false;

        this.time = new Date().getTime(); // этот метод устанваливает время и дату момента создания элемента класса, то есть когда соо отправляется.
    }

    public String getUserName() {
        return userName;
    }

    public String getTextMessage() {
        return  textMessage;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean getLikeMessage() {
        return likeMessage;
    }

    public void setLikeMessage(boolean likeMessage) {
        this.likeMessage = likeMessage;
    }






}

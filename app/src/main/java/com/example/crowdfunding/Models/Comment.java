package com.example.crowdfunding.Models;

import com.google.firebase.database.ServerValue;

public class Comment {
    private String userid, content, userimg, username;
    private Object timestamp;

    public Comment(String userid, String content, String userimg, String username) {
        this.userid = userid;
        this.content = content;
        this.username = username;
        this.userimg = userimg;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public Comment(){

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserimg() {
        return userimg;
    }

    public void setUserimg(String userimg) {
        this.userimg = userimg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}

package com.example.crowdfunding.Models;

public class Card {

    private String number;
    private String projectID;
    private String title;
    private String userID;
    private String amount;

    public Card(String number, String projectID, String title, String userID, String amount) {
        this.number = number;
        this.projectID = projectID;
        this.title = title;
        this.userID = userID;
        this.amount = amount;
    }

    public Card(){
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setNumber(String number){
        this.number = number;
    }

    public String getNumber(){
        return this.number;
    }
}
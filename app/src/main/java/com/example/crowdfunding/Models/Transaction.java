package com.example.crowdfunding.Models;

public class Transaction {

    String iduser;
    String idcard;
    String idproject;
    String amount;

    public Transaction(){
    }

    public Transaction(String iduser, String idcard, String idproject, String amount) {
        this.iduser = iduser;
        this.idcard = idcard;
        this.idproject = idproject;
        this.amount = amount;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getIdproject() {
        return idproject;
    }

    public void setIdproject(String idproject) {
        this.idproject = idproject;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

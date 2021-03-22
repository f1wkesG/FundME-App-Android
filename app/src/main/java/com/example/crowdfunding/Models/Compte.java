package com.example.crowdfunding.Models;

public class Compte {
    private String paypal_email;

    public Compte(String paypal_email) {

        this.paypal_email = paypal_email;
    }
    public Compte(){

    }
    public String getPaypal_email() {
        return paypal_email;
    }

    public void setPaypal_email(String paypal_email) {
        this.paypal_email = paypal_email;
    }
}

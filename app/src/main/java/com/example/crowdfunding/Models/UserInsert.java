package com.example.crowdfunding.Models;

public class UserInsert {
    String name, email, photo;

    public UserInsert(String name, String email, String photo) {
        this.name = name;
        this.email = email;
        this.photo = photo;
    }

    public UserInsert(){

    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto(){
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

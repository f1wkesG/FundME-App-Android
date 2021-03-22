package com.example.crowdfunding.Models;

public class PostCategory {
    private String p_id;

    public PostCategory(){

    }
    public PostCategory(String p_id) {
        this.p_id = p_id;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }
}

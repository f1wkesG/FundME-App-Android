package com.example.crowdfunding.Models;


import com.google.firebase.database.ServerValue;

public class Project {

    private String userid, projectId, title, description, images, website, video,date, category;
    private int montant, montant_r;
    private Object timestamp;


    public Project(){

    }



    public Project(String userid, String title, String desc, int montant, String images, String websitelink, String videolink, String category, String date){
        this.userid = userid;
        this.title = title;
        this.description = desc;
        this.montant = montant;
        this.montant_r = 0;
        this.images = images;
        this.website = websitelink;
        this.video = videolink;
        this.date =  date;
        this.category = category;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public String getProjectId() {
        return projectId;
    }

    public int getMontant() {
        return montant;
    }

    public int getMontant_r() {
        return montant_r;
    }

    public String getUserid() {
        return userid;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public String getDate() {
        return date;
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImages() {
        return images;
    }

    public String getWebsite() {
        return website;
    }

    public String getVideo() {
        return video;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    public void setMontant_r(int montant_r) {
        this.montant_r = montant_r;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Project{" +
                "userid='" + userid + '\'' +
                ", projectId='" + projectId + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", images='" + images + '\'' +
                ", website='" + website + '\'' +
                ", video='" + video + '\'' +
                ", date='" + date + '\'' +
                ", montant=" + montant +
                ", montant_r=" + montant_r +
                ", timestamp=" + timestamp +
                '}';
    }
}

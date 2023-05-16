package com.example.caspaceapplication.ModelClasses;

import com.google.firebase.Timestamp;

public class MyFavorites_ModelClass {

    String branchImage, branchName, userId;
    Timestamp timestamp;

    public MyFavorites_ModelClass() {

    }

    public MyFavorites_ModelClass(String branchImage, String branchName, String userId, Timestamp timestamp) {
        this.branchImage = branchImage;
        this.branchName = branchName;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public String getBranchImage() {
        return branchImage;
    }

    public void setBranchImage(String branchImage) {
        this.branchImage = branchImage;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}

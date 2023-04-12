package com.example.caspaceapplication.Owner;

import com.google.firebase.auth.FirebaseUser;

public class BranchModel {

    private String branchName, branchAddress, branchPicture;

    private BranchModel() {
    }

    public BranchModel(String branchName, String branchAddress, String branchPicture) {
        this.branchName = branchName;
        this.branchAddress = branchAddress;
        this.branchPicture = branchPicture;
    }

    public BranchModel(String branchName, String branchAddress, String branchPicture, FirebaseUser ownerId) {
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBranchAddress() {return branchAddress;}

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getBranchPicture(){return branchPicture;}

    public void setBranchPicture(String branchPicture){this.branchPicture = branchPicture;}
}

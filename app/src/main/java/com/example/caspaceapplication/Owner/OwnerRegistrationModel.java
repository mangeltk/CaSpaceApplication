package com.example.caspaceapplication.Owner;

public class OwnerRegistrationModel {

    private String ownerImage, ownerIDNum, ownerCompanyName, ownerEmail, ownerFirstname, ownerLastname, ownerUsername, ownerPassword,ownerAccountStatus, userType, ownerBusinessPermit;
    private String ownerBranchStatus;

    public OwnerRegistrationModel() {
    }

    public OwnerRegistrationModel(String ownerImage, String ownerIDNum, String ownerCompanyName, String ownerEmail, String ownerFirstname, String ownerLastname, String ownerUsername, String ownerPassword, String ownerAccountStatus, String userType, String ownerBusinessPermit, String ownerBranchStatus) {
        this.ownerImage = ownerImage;
        this.ownerIDNum = ownerIDNum;
        this.ownerCompanyName = ownerCompanyName;
        this.ownerEmail = ownerEmail;
        this.ownerFirstname = ownerFirstname;
        this.ownerLastname = ownerLastname;
        this.ownerUsername = ownerUsername;
        this.ownerPassword = ownerPassword;
        this.ownerAccountStatus = ownerAccountStatus;
        this.userType = userType;
        this.ownerBusinessPermit = ownerBusinessPermit;
        this.ownerBranchStatus = ownerBranchStatus;
    }

    public String getOwnerBranchStatus() {
        return ownerBranchStatus;
    }

    public void setOwnerBranchStatus(String ownerBranchStatus) {
        this.ownerBranchStatus = ownerBranchStatus;
    }

    public String getOwnerImage() {
        return ownerImage;
    }

    public void setOwnerImage(String ownerImage) {
        this.ownerImage = ownerImage;
    }

    public String getOwnerBusinessPermit() {
        return ownerBusinessPermit;
    }

    public void setOwnerBusinessPermit(String ownerBusinessPermit) {
        this.ownerBusinessPermit = ownerBusinessPermit;
    }

    public String getOwnerIDNum() {
        return ownerIDNum;
    }

    public void setOwnerIDNum(String ownerIDNum) {
        this.ownerIDNum = ownerIDNum;
    }

    public String getOwnerCompanyName() {
        return ownerCompanyName;
    }

    public void setOwnerCompanyName(String ownerCompanyName) {
        this.ownerCompanyName = ownerCompanyName;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerFirstname() {
        return ownerFirstname;
    }

    public void setOwnerFirstname(String ownerFirstname) {
        this.ownerFirstname = ownerFirstname;
    }

    public String getOwnerLastname() {
        return ownerLastname;
    }

    public void setOwnerLastname(String ownerLastname) {
        this.ownerLastname = ownerLastname;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }

    public String getOwnerPassword() {
        return ownerPassword;
    }

    public void setOwnerPassword(String ownerPassword) {
        this.ownerPassword = ownerPassword;
    }
    public String getOwnerAccountStatus() {
        return ownerAccountStatus;
    }

    public void setOwnerAccountStatus(String ownerAccountStatus) {
        this.ownerAccountStatus = ownerAccountStatus;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}

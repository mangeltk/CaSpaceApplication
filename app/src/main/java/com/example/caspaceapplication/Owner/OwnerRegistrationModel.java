package com.example.caspaceapplication.Owner;

public class OwnerRegistrationModel {

    private String ownerIDNum, ownerCompanyName, ownerEmail, ownerFirstname, ownerLastname, ownerUsername, ownerPassword;

    public OwnerRegistrationModel() {
    }

    public OwnerRegistrationModel(String ownerIDNum, String ownerCompanyName, String ownerEmail, String ownerFirstname, String ownerLastname, String ownerUsername, String ownerPassword) {
        this.ownerIDNum = ownerIDNum;
        this.ownerCompanyName = ownerCompanyName;
        this.ownerEmail = ownerEmail;
        this.ownerFirstname = ownerFirstname;
        this.ownerLastname = ownerLastname;
        this.ownerUsername = ownerUsername;
        this.ownerPassword = ownerPassword;
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
}

package com.example.caspaceapplication;

public class OwnerRegistrationModel {

    private String ownerEmail, ownerFullname, ownerCompanyName, ownerIDNum, ownerPassword;

    public OwnerRegistrationModel() {
    }

    public OwnerRegistrationModel(String ownerEmail, String ownerFullname, String ownerCompanyName, String ownerIDNum, String ownerPassword) {
        this.ownerEmail = ownerEmail;
        this.ownerFullname = ownerFullname;
        this.ownerCompanyName = ownerCompanyName;
        this.ownerIDNum = ownerIDNum;
        this.ownerPassword = ownerPassword;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getOwnerFullname() {
        return ownerFullname;
    }

    public void setOwnerFullname(String ownerFullname) {
        this.ownerFullname = ownerFullname;
    }

    public String getOwnerCompanyName() {
        return ownerCompanyName;
    }

    public void setOwnerCompanyName(String ownerCompanyName) {this.ownerCompanyName = ownerCompanyName;}

    public String getOwnerIDNum() {
        return ownerIDNum;
    }

    public void setOwnerIDNum(String ownerIDNum) {
        this.ownerIDNum = ownerIDNum;
    }

    public String getOwnerPassword() {
        return ownerPassword;
    }

    public void setOwnerPassword(String ownerPassword) {
        this.ownerPassword = ownerPassword;
    }
}

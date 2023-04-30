package com.example.caspaceapplication.Owner.Profile;

public class Owner_UserProfileDataClass {

    private String ownerCompanyName, ownerEmail, ownerFirstname, ownerIDNum, ownerLastname, ownerPassword, ownerUsername;
    //private String ownerPicture; todo: owner pic


    public Owner_UserProfileDataClass() {
    }

    public Owner_UserProfileDataClass(String ownerCompanyName, String ownerEmail, String ownerFirstname, String ownerIDNum, String ownerLastname, String ownerPassword, String ownerUsername) {
        this.ownerCompanyName = ownerCompanyName;
        this.ownerEmail = ownerEmail;
        this.ownerFirstname = ownerFirstname;
        this.ownerIDNum = ownerIDNum;
        this.ownerLastname = ownerLastname;
        this.ownerPassword = ownerPassword;
        this.ownerUsername = ownerUsername;
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

    public String getOwnerIDNum() {
        return ownerIDNum;
    }

    public void setOwnerIDNum(String ownerIDNum) {
        this.ownerIDNum = ownerIDNum;
    }

    public String getOwnerLastname() {
        return ownerLastname;
    }

    public void setOwnerLastname(String ownerLastname) {
        this.ownerLastname = ownerLastname;
    }

    public String getOwnerPassword() {
        return ownerPassword;
    }

    public void setOwnerPassword(String ownerPassword) {
        this.ownerPassword = ownerPassword;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerUsername) {
        this.ownerUsername = ownerUsername;
    }
}

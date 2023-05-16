package com.example.caspaceapplication;

public class AllUserAccountsModelClass {

    private String userCombinedId, userFirstName, userLastName, userEmail, userImage, userType;

    public AllUserAccountsModelClass() {
    }



    public AllUserAccountsModelClass(String userCombinedId, String userFirstName, String userLastName, String userEmail, String userImage, String userType) {
        this.userCombinedId = userCombinedId;
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userEmail = userEmail;
        this.userImage = userImage;
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserCombinedId() {
        return userCombinedId;
    }

    public void setUserCombinedId(String userCombinedId) {
        this.userCombinedId = userCombinedId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}

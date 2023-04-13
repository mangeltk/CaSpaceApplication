package com.example.caspaceapplication;

public class Messaging_User {

    private String id;
    private String customer_firstName;
    private String imageURL;

    public Messaging_User(String id, String customer_firstName, String imageURL) {
        this.id = id;
        this.customer_firstName = customer_firstName;
        this.imageURL = imageURL;
    }

    public Messaging_User()
    {

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {

        return customer_firstName;
    }

    public void setUsername(String customer_firstName)
    {
        this.customer_firstName = customer_firstName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }



}

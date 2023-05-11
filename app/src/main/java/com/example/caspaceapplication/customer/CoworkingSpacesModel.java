package com.example.caspaceapplication.customer;

public class CoworkingSpacesModel {

    String cospaceName;
    String cospaceImage;
    String owner_id;

    public CoworkingSpacesModel() {
    }

    public CoworkingSpacesModel(String cospaceName, String cospaceImage, String owner_id) {
        this.cospaceName = cospaceName;
        this.cospaceImage = cospaceImage;
        this.owner_id = owner_id;
    }

    public String getCospaceName() {
        return cospaceName;
    }

    public void setCospaceName(String cospaceName) {
        this.cospaceName = cospaceName;
    }

    public String getCospaceImage() {
        return cospaceImage;
    }

    public void setCospaceImage(String cospaceImage) {
        this.cospaceImage = cospaceImage;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }
}

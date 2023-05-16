package com.example.caspaceapplication.customer;

public class TechnologyHubsModel {

    String cospaceName;
    String cospaceImage;

    public TechnologyHubsModel(String cospaceName, String cospaceImage) {
        this.cospaceName = cospaceName;
        this.cospaceImage = cospaceImage;
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
}

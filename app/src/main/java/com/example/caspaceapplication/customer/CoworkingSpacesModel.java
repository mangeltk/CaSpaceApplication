package com.example.caspaceapplication.customer;

import android.widget.ImageView;

public class CoworkingSpacesModel {

    String cospaceName, cospaceAddress;
    ImageView cospaceImage;

    public CoworkingSpacesModel()
    {

    }

    public CoworkingSpacesModel(String cospaceName, String cospaceAddress, ImageView cospaceImage) {
        this.cospaceName = cospaceName;
        //this.cospaceAddress = cospaceAddress;
        this.cospaceImage = cospaceImage;
    }

    public String getCospaceName() {
        return cospaceName;
    }

    public void setCospaceName(String cospaceName) {
        this.cospaceName = cospaceName;
    }

    public String getCospaceAddress() {
        return cospaceAddress;
    }

    public void setCospaceAddress(String cospaceAddress) {
        this.cospaceAddress = cospaceAddress;
    }

    public ImageView getCospaceImage() {
        return cospaceImage;
    }
    public void setCospaceImage(ImageView cospaceImage) {
        this.cospaceImage = cospaceImage;
    }
}

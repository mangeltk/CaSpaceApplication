package com.example.caspaceapplication.Owner;

import com.google.firebase.firestore.GeoPoint;

public class BranchModel {

    private String cospaceStreetAddress, cospaceCityAddress, cospaceCategory, cospaceId, cospaceImage, cospaceName;
    private GeoPoint location;

    public BranchModel() {

    }

    public BranchModel(String cospaceStreetAddress, String cospaceCityAddress, String cospaceCategory, String cospaceId, String cospaceImage, String cospaceName, GeoPoint location) {
        this.cospaceStreetAddress = cospaceStreetAddress;
        this.cospaceCityAddress = cospaceCityAddress;
        this.cospaceCategory = cospaceCategory;
        this.cospaceId = cospaceId;
        this.cospaceImage = cospaceImage;
        this.cospaceName = cospaceName;
        this.location = location;
    }

    public String getCospaceStreetAddress() {
        return cospaceStreetAddress;
    }

    public void setCospaceStreetAddress(String cospaceStreetAddress) {
        this.cospaceStreetAddress = cospaceStreetAddress;
    }

    public String getCospaceCityAddress() {
        return cospaceCityAddress;
    }

    public void setCospaceCityAddress(String cospaceCityAddress) {
        this.cospaceCityAddress = cospaceCityAddress;
    }

    public String getCospaceCategory() {
        return cospaceCategory;
    }

    public void setCospaceCategory(String cospaceCategory) {
        this.cospaceCategory = cospaceCategory;
    }

    public String getCospaceId() {
        return cospaceId;
    }

    public void setCospaceId(String cospaceId) {
        this.cospaceId = cospaceId;
    }

    public String getCospaceImage() {
        return cospaceImage;
    }

    public void setCospaceImage(String cospaceImage) {
        this.cospaceImage = cospaceImage;
    }

    public String getCospaceName() {
        return cospaceName;
    }

    public void setCospaceName(String cospaceName) {
        this.cospaceName = cospaceName;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}
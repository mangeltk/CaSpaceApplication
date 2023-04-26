package com.example.caspaceapplication.Owner;

public class BranchModel {

    private String cospaceStreetAddress, cospaceCityAddress, cospaceCategory, cospaceId, cospaceImage, cospaceName;

    public BranchModel() {
    }

    public BranchModel(String cospaceStreetAddress, String cospaceCityAddress, String cospaceCategory, String cospaceId, String cospaceImage, String cospaceName) {
        this.cospaceStreetAddress = cospaceStreetAddress;
        this.cospaceCityAddress = cospaceCityAddress;
        this.cospaceCategory = cospaceCategory;
        this.cospaceId = cospaceId;
        this.cospaceImage = cospaceImage;
        this.cospaceName = cospaceName;
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
}

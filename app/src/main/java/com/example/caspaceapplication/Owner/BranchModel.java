package com.example.caspaceapplication.Owner;

public class BranchModel {

    private String cospaceAddress, cospaceCategory, cospaceId, cospaceImage, cospaceName;

    public BranchModel() {
    }

    public BranchModel(String cospaceAddress, String cospaceCategory, String cospaceId, String cospaceImage, String cospaceName) {
        this.cospaceAddress = cospaceAddress;
        this.cospaceCategory = cospaceCategory;
        this.cospaceId = cospaceId;
        this.cospaceImage = cospaceImage;
        this.cospaceName = cospaceName;
    }

    public String getCospaceAddress() {
        return cospaceAddress;
    }

    public void setCospaceAddress(String cospaceAddress) {
        this.cospaceAddress = cospaceAddress;
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

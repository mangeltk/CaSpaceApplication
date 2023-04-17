package com.example.caspaceapplication.Owner;

public class OwnerProDisc_ModelClass {

    private String proDiscTitle;
    private String ProDiscImage;
    private String ProDiscDescription;

    public OwnerProDisc_ModelClass() {
    }

    public OwnerProDisc_ModelClass(String proDiscTitle, String proDiscImage, String proDiscDescription) {
        this.proDiscTitle = proDiscTitle;
        ProDiscImage = proDiscImage;
        ProDiscDescription = proDiscDescription;
    }

    public String getProDiscTitle() {
        return proDiscTitle;
    }

    public void setProDiscTitle(String proDiscTitle) {
        this.proDiscTitle = proDiscTitle;
    }

    public String getProDiscImage() {
        return ProDiscImage;
    }

    public void setProDiscImage(String proDiscImage) {
        ProDiscImage = proDiscImage;
    }

    public String getProDiscDescription() {
        return ProDiscDescription;
    }

    public void setProDiscDescription(String proDiscDescription) {
        ProDiscDescription = proDiscDescription;
    }
}

package com.example.caspaceapplication.Owner;

public class OwnerProDisc_ModelClass {

    private String promotionTitle;
    private String promotionImage;
    private String promotionDescription;

    public OwnerProDisc_ModelClass() {
    }

    public OwnerProDisc_ModelClass(String promotionTitle, String promotionImage, String promotionDescription) {
        this.promotionTitle = promotionTitle;
        this.promotionImage = promotionImage;
        this.promotionDescription = promotionDescription;
    }

    public String getPromotionTitle() {
        return promotionTitle;
    }

    public void setPromotionTitle(String promotionTitle) {
        this.promotionTitle = promotionTitle;
    }

    public String getPromotionImage() {
        return promotionImage;
    }

    public void setPromotionImage(String promotionImage) {
        this.promotionImage = promotionImage;
    }

    public String getPromotionDescription() {
        return promotionDescription;
    }

    public void setPromotionDescription(String promotionDescription) {
        this.promotionDescription = promotionDescription;
    }
}

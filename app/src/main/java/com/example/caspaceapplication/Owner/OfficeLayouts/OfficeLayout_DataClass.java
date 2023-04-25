package com.example.caspaceapplication.Owner.OfficeLayouts;

public class OfficeLayout_DataClass {

    private String layoutImage;
    private String layoutName;
    private String layoutPeopleNum;
    private String layoutAreasize;
    private String layoutType;
    private String layoutPrice;
    private String layoutAvailability;

    public OfficeLayout_DataClass() {
    }

    public OfficeLayout_DataClass(String layoutImage, String layoutName, String layoutPeopleNum, String layoutAreasize, String layoutType, String layoutPrice, String layoutAvailability) {
        this.layoutImage = layoutImage;
        this.layoutName = layoutName;
        this.layoutPeopleNum = layoutPeopleNum;
        this.layoutAreasize = layoutAreasize;
        this.layoutType = layoutType;
        this.layoutPrice = layoutPrice;
        this.layoutAvailability = layoutAvailability;
    }

    public String getLayoutImage() {
        return layoutImage;
    }

    public void setLayoutImage(String layoutImage) {
        this.layoutImage = layoutImage;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }

    public String getLayoutPeopleNum() {
        return layoutPeopleNum;
    }

    public void setLayoutPeopleNum(String layoutPeopleNum) {
        this.layoutPeopleNum = layoutPeopleNum;
    }

    public String getLayoutAreasize() {
        return layoutAreasize;
    }

    public void setLayoutAreasize(String layoutAreasize) {
        this.layoutAreasize = layoutAreasize;
    }

    public String getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(String layoutType) {
        this.layoutType = layoutType;
    }

    public String getLayoutPrice() {
        return layoutPrice;
    }

    public void setLayoutPrice(String layoutPrice) {
        this.layoutPrice = layoutPrice;
    }

    public String getLayoutAvailability() {
        return layoutAvailability;
    }

    public void setLayoutAvailability(String layoutAvailability) {
        this.layoutAvailability = layoutAvailability;
    }
}

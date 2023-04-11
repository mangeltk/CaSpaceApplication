package com.example.caspaceapplication;

public class OfficeLayout_DataClass {

    private String layoutImage;
    private String layoutName;
    private String layoutPeopleNum;
    private String layoutAreasize;

    public OfficeLayout_DataClass() {
    }

    public OfficeLayout_DataClass(String layoutImage, String layoutName, String layoutPeopleNum, String layoutAreasize) {
        this.layoutImage = layoutImage;
        this.layoutName = layoutName;
        this.layoutPeopleNum = layoutPeopleNum;
        this.layoutAreasize = layoutAreasize;
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
}

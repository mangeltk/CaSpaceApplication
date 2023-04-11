package com.example.caspaceapplication;

public class OfficeLayout_DataClass {

    private String layoutName;
    private String layoutPeopleNum;
    private String layoutAreasize;
    private int layoutImage;

    public OfficeLayout_DataClass(String layoutName, String layoutPeopleNum, String layoutAreasize, int layoutImage) {
        this.layoutName = layoutName;
        this.layoutPeopleNum = layoutPeopleNum;
        this.layoutAreasize = layoutAreasize;
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

    public int getLayoutImage() {
        return layoutImage;
    }

    public void setLayoutImage(int layoutImage) {
        this.layoutImage = layoutImage;
    }
}

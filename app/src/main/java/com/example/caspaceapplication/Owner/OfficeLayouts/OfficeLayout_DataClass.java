package com.example.caspaceapplication.Owner.OfficeLayouts;

public class OfficeLayout_DataClass {

    private String layoutImage, layoutName, layoutAreasize, layoutType, layoutAvailability;
    private String layoutHourlyPrice, layoutDailyPrice, layoutWeeklyPrice, layoutMonthlyPrice, layoutAnnualPrice;
    private String layout_id, owner_id;
    private String maxCapacity, minCapacity;//for person capacity

    public OfficeLayout_DataClass() {
    }

    public OfficeLayout_DataClass(String layoutImage, String layoutName, String layoutAreasize, String layoutType, String layoutAvailability, String layoutHourlyPrice, String layoutDailyPrice, String layoutWeeklyPrice, String layoutMonthlyPrice, String layoutAnnualPrice, String layout_id, String owner_id, String maxCapacity, String minCapacity) {
        this.layoutImage = layoutImage;
        this.layoutName = layoutName;
        this.layoutAreasize = layoutAreasize;
        this.layoutType = layoutType;
        this.layoutAvailability = layoutAvailability;
        this.layoutHourlyPrice = layoutHourlyPrice;
        this.layoutDailyPrice = layoutDailyPrice;
        this.layoutWeeklyPrice = layoutWeeklyPrice;
        this.layoutMonthlyPrice = layoutMonthlyPrice;
        this.layoutAnnualPrice = layoutAnnualPrice;
        this.layout_id = layout_id;
        this.owner_id = owner_id;
        this.maxCapacity = maxCapacity;
        this.minCapacity = minCapacity;
    }


    public String getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(String maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getMinCapacity() {
        return minCapacity;
    }

    public void setMinCapacity(String minCapacity) {
        this.minCapacity = minCapacity;
    }

    public String getLayout_id() {
        return layout_id;
    }

    public void setLayout_id(String layout_id) {
        this.layout_id = layout_id;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
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

    public String getLayoutAvailability() {
        return layoutAvailability;
    }

    public void setLayoutAvailability(String layoutAvailability) {
        this.layoutAvailability = layoutAvailability;
    }

    public String getLayoutHourlyPrice() {
        return layoutHourlyPrice;
    }

    public void setLayoutHourlyPrice(String layoutHourlyPrice) {
        this.layoutHourlyPrice = layoutHourlyPrice;
    }

    public String getLayoutDailyPrice() {
        return layoutDailyPrice;
    }

    public void setLayoutDailyPrice(String layoutDailyPrice) {
        this.layoutDailyPrice = layoutDailyPrice;
    }

    public String getLayoutWeeklyPrice() {
        return layoutWeeklyPrice;
    }

    public void setLayoutWeeklyPrice(String layoutWeeklyPrice) {
        this.layoutWeeklyPrice = layoutWeeklyPrice;
    }

    public String getLayoutMonthlyPrice() {
        return layoutMonthlyPrice;
    }

    public void setLayoutMonthlyPrice(String layoutMonthlyPrice) {
        this.layoutMonthlyPrice = layoutMonthlyPrice;
    }

    public String getLayoutAnnualPrice() {
        return layoutAnnualPrice;
    }

    public void setLayoutAnnualPrice(String layoutAnnualPrice) {
        this.layoutAnnualPrice = layoutAnnualPrice;
    }
}

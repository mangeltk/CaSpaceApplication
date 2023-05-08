package com.example.caspaceapplication.customer.BookingTransactionManagement;

public class BookingDetails_ModelClass {

    String customerId, ownerId, rateType, rateValue, bookingStartDate, bookingEndDate, bookingStartTime,
            bookingEndTime, totalHours, totalPayment, customerFullname, organizationName, numOfTenants,
            customerPhoneNum, customerEmail, customerAddress, proofOfPayment, bookingStatus,
            branchImage, branchName, layoutImage, layoutName;

    public BookingDetails_ModelClass() {
    }

    public BookingDetails_ModelClass(String customerId, String ownerId, String rateType, String rateValue, String bookingStartDate, String bookingEndDate, String bookingStartTime, String bookingEndTime, String totalHours, String totalPayment, String customerFullname, String organizationName, String numOfTenants, String customerPhoneNum, String customerEmail, String customerAddress, String proofOfPayment, String bookingStatus, String branchImage, String branchName, String layoutImage, String layoutName) {
        this.customerId = customerId;
        this.ownerId = ownerId;
        this.rateType = rateType;
        this.rateValue = rateValue;
        this.bookingStartDate = bookingStartDate;
        this.bookingEndDate = bookingEndDate;
        this.bookingStartTime = bookingStartTime;
        this.bookingEndTime = bookingEndTime;
        this.totalHours = totalHours;
        this.totalPayment = totalPayment;
        this.customerFullname = customerFullname;
        this.organizationName = organizationName;
        this.numOfTenants = numOfTenants;
        this.customerPhoneNum = customerPhoneNum;
        this.customerEmail = customerEmail;
        this.customerAddress = customerAddress;
        this.proofOfPayment = proofOfPayment;
        this.bookingStatus = bookingStatus;
        this.branchImage = branchImage;
        this.branchName = branchName;
        this.layoutImage = layoutImage;
        this.layoutName = layoutName;
    }

    public String getBranchImage() {
        return branchImage;
    }

    public void setBranchImage(String branchImage) {
        this.branchImage = branchImage;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getBookingStartDate() {
        return bookingStartDate;
    }

    public void setBookingStartDate(String bookingStartDate) {
        this.bookingStartDate = bookingStartDate;
    }

    public String getBookingEndDate() {
        return bookingEndDate;
    }

    public void setBookingEndDate(String bookingEndDate) {
        this.bookingEndDate = bookingEndDate;
    }

    public String getBookingStartTime() {
        return bookingStartTime;
    }

    public void setBookingStartTime(String bookingStartTime) {
        this.bookingStartTime = bookingStartTime;
    }

    public String getBookingEndTime() {
        return bookingEndTime;
    }

    public void setBookingEndTime(String bookingEndTime) {
        this.bookingEndTime = bookingEndTime;
    }

    public String getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(String totalHours) {
        this.totalHours = totalHours;
    }

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getCustomerFullname() {
        return customerFullname;
    }

    public void setCustomerFullname(String customerFullname) {
        this.customerFullname = customerFullname;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getNumOfTenants() {
        return numOfTenants;
    }

    public void setNumOfTenants(String numOfTenants) {
        this.numOfTenants = numOfTenants;
    }

    public String getCustomerPhoneNum() {
        return customerPhoneNum;
    }

    public void setCustomerPhoneNum(String customerPhoneNum) {
        this.customerPhoneNum = customerPhoneNum;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getProofOfPayment() {
        return proofOfPayment;
    }

    public void setProofOfPayment(String proofOfPayment) {
        this.proofOfPayment = proofOfPayment;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}
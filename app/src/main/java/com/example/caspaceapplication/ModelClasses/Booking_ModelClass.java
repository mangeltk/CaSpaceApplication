package com.example.caspaceapplication.ModelClasses;

import com.google.firebase.Timestamp;

public class Booking_ModelClass {

    String  BookDateSelected, RateType, ratePrice, bookingStatus, layoutAvailability, layoutName,
            layoutImage, branchImage, branchName;

    Timestamp BookEndTimeSelected, BookStartTimeSelected, bookSubmittedDate;

    String  customerId, ownerId, customerFullname, organizationName, numOfTenants, customerPhoneNum,
            customerEmail, customerAddress, proofOfPayment, paymentOption, bookingId;

    String totalPayment, totalHours, totalDays;

    public Booking_ModelClass() {
    }

    public Booking_ModelClass(String bookDateSelected, String rateType, String ratePrice, String bookingStatus, String layoutAvailability, String layoutName, String layoutImage, String branchImage, String branchName, Timestamp bookEndTimeSelected, Timestamp bookStartTimeSelected, Timestamp bookSubmittedDate, String customerId, String ownerId, String customerFullname, String organizationName, String numOfTenants, String customerPhoneNum, String customerEmail, String customerAddress, String proofOfPayment, String paymentOption, String bookingId, String totalPayment, String totalHours, String totalDays) {
        BookDateSelected = bookDateSelected;
        RateType = rateType;
        this.ratePrice = ratePrice;
        this.bookingStatus = bookingStatus;
        this.layoutAvailability = layoutAvailability;
        this.layoutName = layoutName;
        this.layoutImage = layoutImage;
        this.branchImage = branchImage;
        this.branchName = branchName;
        BookEndTimeSelected = bookEndTimeSelected;
        BookStartTimeSelected = bookStartTimeSelected;
        this.bookSubmittedDate = bookSubmittedDate;
        this.customerId = customerId;
        this.ownerId = ownerId;
        this.customerFullname = customerFullname;
        this.organizationName = organizationName;
        this.numOfTenants = numOfTenants;
        this.customerPhoneNum = customerPhoneNum;
        this.customerEmail = customerEmail;
        this.customerAddress = customerAddress;
        this.proofOfPayment = proofOfPayment;
        this.paymentOption = paymentOption;
        this.bookingId = bookingId;
        this.totalPayment = totalPayment;
        this.totalHours = totalHours;
        this.totalDays = totalDays;
    }

    public String getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(String totalDays) {
        this.totalDays = totalDays;
    }

    public String getBookDateSelected() {
        return BookDateSelected;
    }

    public void setBookDateSelected(String bookDateSelected) {
        BookDateSelected = bookDateSelected;
    }

    public String getRateType() {
        return RateType;
    }

    public void setRateType(String rateType) {
        RateType = rateType;
    }

    public String getRatePrice() {
        return ratePrice;
    }

    public void setRatePrice(String ratePrice) {
        this.ratePrice = ratePrice;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getLayoutAvailability() {
        return layoutAvailability;
    }

    public void setLayoutAvailability(String layoutAvailability) {
        this.layoutAvailability = layoutAvailability;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }

    public String getLayoutImage() {
        return layoutImage;
    }

    public void setLayoutImage(String layoutImage) {
        this.layoutImage = layoutImage;
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

    public Timestamp getBookEndTimeSelected() {
        return BookEndTimeSelected;
    }

    public void setBookEndTimeSelected(Timestamp bookEndTimeSelected) {
        BookEndTimeSelected = bookEndTimeSelected;
    }

    public Timestamp getBookStartTimeSelected() {
        return BookStartTimeSelected;
    }

    public void setBookStartTimeSelected(Timestamp bookStartTimeSelected) {
        BookStartTimeSelected = bookStartTimeSelected;
    }

    public Timestamp getBookSubmittedDate() {
        return bookSubmittedDate;
    }

    public void setBookSubmittedDate(Timestamp bookSubmittedDate) {
        this.bookSubmittedDate = bookSubmittedDate;
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

    public String getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(String paymentOption) {
        this.paymentOption = paymentOption;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(String totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(String totalHours) {
        this.totalHours = totalHours;
    }
}

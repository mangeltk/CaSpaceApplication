package com.example.caspaceapplication.Owner.BookingTransactions;

public class OwnerBT_ModelClass {

    String bookingDate;
    String bookingDuration;
    String bookingID;
    String bookingPaymentProof;
    String bookingStatus;
    String bookingTotal;
    String cospaceID;
    String customerID;
    String ownerID;
    String spaceID;

    public OwnerBT_ModelClass() {
    }

    public OwnerBT_ModelClass(String bookingDate, String bookingDuration, String bookingID, String bookingPaymentProof, String bookingStatus, String bookingTotal, String cospaceID, String customerID, String ownerID, String spaceID) {
        this.bookingDate = bookingDate;
        this.bookingDuration = bookingDuration;
        this.bookingID = bookingID;
        this.bookingPaymentProof = bookingPaymentProof;
        this.bookingStatus = bookingStatus;
        this.bookingTotal = bookingTotal;
        this.cospaceID = cospaceID;
        this.customerID = customerID;
        this.ownerID = ownerID;
        this.spaceID = spaceID;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingDuration() {
        return bookingDuration;
    }

    public void setBookingDuration(String bookingDuration) {
        this.bookingDuration = bookingDuration;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getBookingPaymentProof() {
        return bookingPaymentProof;
    }

    public void setBookingPaymentProof(String bookingPaymentProof) {
        this.bookingPaymentProof = bookingPaymentProof;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getBookingTotal() {
        return bookingTotal;
    }

    public void setBookingTotal(String bookingTotal) {
        this.bookingTotal = bookingTotal;
    }

    public String getCospaceID() {
        return cospaceID;
    }

    public void setCospaceID(String cospaceID) {
        this.cospaceID = cospaceID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getSpaceID() {
        return spaceID;
    }

    public void setSpaceID(String spaceID) {
        this.spaceID = spaceID;
    }
}

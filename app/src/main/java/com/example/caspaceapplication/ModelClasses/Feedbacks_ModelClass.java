package com.example.caspaceapplication.ModelClasses;

import java.util.Date;

public class Feedbacks_ModelClass {

    String feedbackId, feedbackMessage, ownerId, cospaceName, customerId, customerImage, bookedSpaceLayout;
    Float ratingValue;
    Date timestamp;

    public Feedbacks_ModelClass() {
    }

    public Feedbacks_ModelClass(String feedbackId, String feedbackMessage, String ownerId, String cospaceName, String customerId, String customerImage, String bookedSpaceLayout, Float ratingValue, Date timestamp) {
        this.feedbackId = feedbackId;
        this.feedbackMessage = feedbackMessage;
        this.ownerId = ownerId;
        this.cospaceName = cospaceName;
        this.customerId = customerId;
        this.customerImage = customerImage;
        this.bookedSpaceLayout = bookedSpaceLayout;
        this.ratingValue = ratingValue;
        this.timestamp = timestamp;
    }

    public Float getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(Float ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getBookedSpaceLayout() {
        return bookedSpaceLayout;
    }

    public void setBookedSpaceLayout(String bookedSpaceLayout) {
        this.bookedSpaceLayout = bookedSpaceLayout;
    }

    public String getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(String feedbackId) {
        this.feedbackId = feedbackId;
    }

    public String getFeedbackMessage() {
        return feedbackMessage;
    }

    public void setFeedbackMessage(String feedbackMessage) {
        this.feedbackMessage = feedbackMessage;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getCospaceName() {
        return cospaceName;
    }

    public void setCospaceName(String cospaceName) {
        this.cospaceName = cospaceName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerImage() {
        return customerImage;
    }

    public void setCustomerImage(String customerImage) {
        this.customerImage = customerImage;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

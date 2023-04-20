package com.example.caspaceapplication.customer;

public class CustomerRegistrationModel {

    private String customersIDNum, customersEmail, customersFirstName, customersLastName, customersUsername, customersPassword, customersOrganization, customersPopulation;

    public CustomerRegistrationModel() {
    }

    public CustomerRegistrationModel(String customersIDNum, String customersEmail, String customersFirstName, String customersLastName,
                                     String customersUsername, String customersPassword, String customersOrganization, String customersPopulation) {
        this.customersIDNum = customersIDNum;
        this.customersEmail = customersEmail;
        this.customersFirstName = customersFirstName;
        this.customersLastName = customersLastName;
        this.customersUsername = customersUsername;
        this.customersPassword = customersPassword;
        this.customersOrganization = customersOrganization;
        this.customersPopulation = customersPopulation;
    }

    public String getCustomersIDNum() {
        return customersIDNum;
    }

    public void setCustomersIDNum(String ownerIDNum) {
        this.customersIDNum = ownerIDNum;
    }

    public String getCustomersOrganization() {
        return customersOrganization;
    }

    public void setCustomersOrganization(String customersOrganization) {
        this.customersOrganization = customersOrganization;
    }

    public String getCustomersPopulation() {
        return customersPopulation;
    }

    public void setCustomersPopulation(String customersPopulation) {
        this.customersPopulation = customersPopulation;
    }



    public String getCustomersEmail() {
        return customersEmail;
    }

    public void setCustomersEmail(String ownerEmail) {
        this.customersEmail = customersEmail;
    }

    public String getCustomersFirstName() {
        return customersFirstName;
    }

    public void setCustomersFirstName (String customersFirstName) {
        this.customersFirstName = customersFirstName;
    }

    public String getCustomersLastName() {
        return customersLastName;
    }

    public void setCustomersLastName(String customersLastName) {
        this.customersLastName = customersLastName;
    }

    public String getCustomersUsername() {
        return customersUsername;
    }

    public void setCustomersUsername(String customersUsername) {
        this.customersUsername = customersUsername;
    }

    public String getCustomersPassword() {
        return customersPassword;
    }

    public void setCustomersPassword(String ownerPassword) {
        this.customersPassword = customersPassword;
    }
}
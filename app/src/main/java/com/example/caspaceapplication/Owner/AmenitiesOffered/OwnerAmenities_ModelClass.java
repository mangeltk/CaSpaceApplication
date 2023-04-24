package com.example.caspaceapplication.Owner.AmenitiesOffered;

import java.util.Map;

public class OwnerAmenities_ModelClass {

    private String amenityName;

    public OwnerAmenities_ModelClass(String docId, Map<String, Object> selectedAmenities) {
    }

    public OwnerAmenities_ModelClass(String amenityName) {
        this.amenityName = amenityName;
    }

    public String getAmenityName() {
        return amenityName;
    }

    public void setAmenityName(String amenityName) {
        this.amenityName = amenityName;
    }

}

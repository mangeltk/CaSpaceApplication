package com.example.caspaceapplication.Owner;

import com.google.firebase.firestore.GeoPoint;

import java.util.Map;

public class BranchModel {

    private String cospaceStreetAddress, cospaceCityAddress, cospaceCategory, cospaceId, cospaceImage, cospaceName;
    private GeoPoint location;
    private String owner_id;
    private Map<String, OpeningHours> openingHours;
    int Likes, RankNo;

    public BranchModel() {
    }

    public BranchModel(String cospaceStreetAddress, String cospaceCityAddress, String cospaceCategory, String cospaceId, String cospaceImage, String cospaceName, GeoPoint location, String owner_id, Map<String, OpeningHours> openingHours, int likes, int rankNo) {
        this.cospaceStreetAddress = cospaceStreetAddress;
        this.cospaceCityAddress = cospaceCityAddress;
        this.cospaceCategory = cospaceCategory;
        this.cospaceId = cospaceId;
        this.cospaceImage = cospaceImage;
        this.cospaceName = cospaceName;
        this.location = location;
        this.owner_id = owner_id;
        this.openingHours = openingHours;
        Likes = likes;
        RankNo = rankNo;
    }

    public int getRankNo() {
        return RankNo;
    }

    public void setRankNo(int rankNo) {
        RankNo = rankNo;
    }

    public int getLikes() {
        return Likes;
    }

    public void setLikes(int likes) {
        Likes = likes;
    }

    public Map<String, OpeningHours> getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(Map<String, OpeningHours> openingHours) {
        this.openingHours = openingHours;
    }

    public static class OpeningHours {
        private boolean closed;
        private String openTime;
        private String closeTime;

        public OpeningHours() {
        }

        public OpeningHours(boolean closed, String openTime, String closeTime) {
            this.closed = closed;
            this.openTime = openTime;
            this.closeTime = closeTime;
        }

        // Getters and setters for all fields

        public boolean isClosed() {
            return closed;
        }

        public void setClosed(boolean closed) {
            this.closed = closed;
        }

        public String getOpenTime() {
            return openTime;
        }

        public void setOpenTime(String openTime) {
            this.openTime = openTime;
        }

        public String getCloseTime() {
            return closeTime;
        }

        public void setCloseTime(String closeTime) {
            this.closeTime = closeTime;
        }
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getCospaceStreetAddress() {
        return cospaceStreetAddress;
    }

    public void setCospaceStreetAddress(String cospaceStreetAddress) {
        this.cospaceStreetAddress = cospaceStreetAddress;
    }

    public String getCospaceCityAddress() {
        return cospaceCityAddress;
    }

    public void setCospaceCityAddress(String cospaceCityAddress) {
        this.cospaceCityAddress = cospaceCityAddress;
    }

    public String getCospaceCategory() {
        return cospaceCategory;
    }

    public void setCospaceCategory(String cospaceCategory) {
        this.cospaceCategory = cospaceCategory;
    }

    public String getCospaceId() {
        return cospaceId;
    }

    public void setCospaceId(String cospaceId) {
        this.cospaceId = cospaceId;
    }

    public String getCospaceImage() {
        return cospaceImage;
    }

    public void setCospaceImage(String cospaceImage) {
        this.cospaceImage = cospaceImage;
    }

    public String getCospaceName() {
        return cospaceName;
    }

    public void setCospaceName(String cospaceName) {
        this.cospaceName = cospaceName;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public void setLocation(GeoPoint location) {
        this.location = location;
    }
}
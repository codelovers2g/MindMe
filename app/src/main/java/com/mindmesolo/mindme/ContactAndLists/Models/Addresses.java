package com.mindmesolo.mindme.ContactAndLists.Models;

/**
 * Created by enest_09 on 11/25/2016.
 */

public class Addresses {
    private String region;

    private String addressLine4;

    private String addressType;

    private String addressLine3;

    private String addressLine2;

    private String addressLine1;

    private String attentionTo;

    private String updatedBy;

    private String city;

    private String country;

    private String createdOn;

    private String id;

    private String postalCode;

    private String createdBy;

    private String updatedOn;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAddressLine4() {
        return addressLine4;
    }

    public void setAddressLine4(String addressLine4) {
        this.addressLine4 = addressLine4;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAttentionTo() {
        return attentionTo;
    }

    public void setAttentionTo(String attentionTo) {
        this.attentionTo = attentionTo;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Override
    public String toString() {
        return "ClassPojo [region = " + region + ", addressLine4 = " + addressLine4 + ", addressType = " + addressType + ", addressLine3 = " + addressLine3 + ", addressLine2 = " + addressLine2 + ", addressLine1 = " + addressLine1 + ", attentionTo = " + attentionTo + ", updatedBy = " + updatedBy + ", city = " + city + ", country = " + country + ", createdOn = " + createdOn + ", id = " + id + ", postalCode = " + postalCode + ", createdBy = " + createdBy + ", updatedOn = " + updatedOn + "]";
    }

}
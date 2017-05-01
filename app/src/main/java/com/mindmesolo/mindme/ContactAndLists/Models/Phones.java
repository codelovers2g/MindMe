package com.mindmesolo.mindme.ContactAndLists.Models;

/**
 * Created by enest_09 on 11/25/2016.
 */

public class Phones {
    private String id;

    private String createdOn;

    private String voiceOptOutSource;

    private String smsOptOutSource;

    private String smsAllowed;

    private String phoneCountryCode;

    private String phoneNumber;

    private String createdBy;

    private String phoneType;

    private String phoneAreaCode;

    private String twilioAccountSid;

    private String updatedOn;

    private String voiceAllowed;

    private String updatedBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getVoiceOptOutSource() {
        return voiceOptOutSource;
    }

    public void setVoiceOptOutSource(String voiceOptOutSource) {
        this.voiceOptOutSource = voiceOptOutSource;
    }

    public String getSmsOptOutSource() {
        return smsOptOutSource;
    }

    public void setSmsOptOutSource(String smsOptOutSource) {
        this.smsOptOutSource = smsOptOutSource;
    }

    public String getSmsAllowed() {
        return smsAllowed;
    }

    public void setSmsAllowed(String smsAllowed) {
        this.smsAllowed = smsAllowed;
    }

    public String getPhoneCountryCode() {
        return phoneCountryCode;
    }

    public void setPhoneCountryCode(String phoneCountryCode) {
        this.phoneCountryCode = phoneCountryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getPhoneAreaCode() {
        return phoneAreaCode;
    }

    public void setPhoneAreaCode(String phoneAreaCode) {
        this.phoneAreaCode = phoneAreaCode;
    }

    public String getTwilioAccountSid() {
        return twilioAccountSid;
    }

    public void setTwilioAccountSid(String twilioAccountSid) {
        this.twilioAccountSid = twilioAccountSid;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getVoiceAllowed() {
        return voiceAllowed;
    }

    public void setVoiceAllowed(String voiceAllowed) {
        this.voiceAllowed = voiceAllowed;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", createdOn = " + createdOn + ", voiceOptOutSource = " + voiceOptOutSource + ", smsOptOutSource = " + smsOptOutSource + ", smsAllowed = " + smsAllowed + ", phoneCountryCode = " + phoneCountryCode + ", phoneNumber = " + phoneNumber + ", createdBy = " + createdBy + ", phoneType = " + phoneType + ", phoneAreaCode = " + phoneAreaCode + ", twilioAccountSid = " + twilioAccountSid + ", updatedOn = " + updatedOn + ", voiceAllowed = " + voiceAllowed + ", updatedBy = " + updatedBy + "]";
    }
}

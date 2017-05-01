package com.mindmesolo.mindme.CampaignHelper;

/**
 * Created by eNest on 7/29/2016.
 */


public class CampaignPreviewData {
    String CampaignName = null;
    String CampaignCreatedOn = null;
    String Count = null;
    String emailImage = null;
    String textImage = null;
    String callImage = null;
    String CampaignId = null;
    String activities = null;

    public CampaignPreviewData(String CampaignId, String CampaignName, String CampaignCreatedOn, String Count, String emailImage, String textImage, String callImage, String activities) {
        super();
        this.CampaignId = CampaignId;
        this.CampaignName = CampaignName;
        this.CampaignCreatedOn = CampaignCreatedOn;
        this.Count = Count;
        this.emailImage = emailImage;
        this.textImage = textImage;
        this.callImage = callImage;
        this.activities = activities;
    }


    public String getCampaignName() {
        return CampaignName;
    }

    public void setCampaignName(String CampaignName) {
        this.CampaignName = CampaignName;
    }

    public String getCampaignCreatedOn() {
        return CampaignCreatedOn;
    }

    public void setCampaignCreatedOn(String CampaignCreatedOn) {
        this.CampaignCreatedOn = CampaignCreatedOn;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String Count) {
        this.Count = Count;
    }

    public String getEmailImage() {
        return emailImage;
    }

    public void setEmailImage(String emailImage) {
        this.emailImage = emailImage;
    }

    public String getCallImage() {
        return callImage;
    }

    public void setCallImage(String callImage) {
        this.callImage = callImage;
    }

    public String getTextImage() {
        return textImage;
    }

    public void setTextImage(String textImage) {
        this.textImage = textImage;
    }

    public String getCampaignId() {
        return CampaignId;
    }

    public void setCampaignId(String campaignId) {
        CampaignId = campaignId;
    }

    public String getActivities() {
        return activities;
    }

    public void setActivities(String activities) {
        this.activities = activities;
    }
}
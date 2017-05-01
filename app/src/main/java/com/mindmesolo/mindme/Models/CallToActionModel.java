package com.mindmesolo.mindme.Models;

/**
 * Created by enest_09 on 11/2/2016.
 */
public class CallToActionModel {
    private String orgId;

    private String message;

    private String id;

    private Feedback feedback;

    private String mobilePageElementBuildId;

    private LinkCall linkCall;

    private YesNoMaybe yesNoMaybe;

    private PollItems[] pollItems;

    private Properties[] properties;

    private String type;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public String getMobilePageElementBuildId() {
        return mobilePageElementBuildId;
    }

    public void setMobilePageElementBuildId(String mobilePageElementBuildId) {
        this.mobilePageElementBuildId = mobilePageElementBuildId;
    }

    public LinkCall getLinkCall() {
        return linkCall;
    }

    public void setLinkCall(LinkCall linkCall) {
        this.linkCall = linkCall;
    }

    public YesNoMaybe getYesNoMaybe() {
        return yesNoMaybe;
    }

    public void setYesNoMaybe(YesNoMaybe yesNoMaybe) {
        this.yesNoMaybe = yesNoMaybe;
    }

    public PollItems[] getPollItems() {
        return pollItems;
    }

    public void setPollItems(PollItems[] pollItems) {
        this.pollItems = pollItems;
    }

    public Properties[] getProperties() {
        return properties;
    }

    public void setProperties(Properties[] properties) {
        this.properties = properties;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ClassPojo [orgId = " + orgId + ", message = " + message + ", id = " + id + ", feedback = " + feedback + ", mobilePageElementBuildId = " + mobilePageElementBuildId + ", linkCall = " + linkCall + ", yesNoMaybe = " + yesNoMaybe + ", pollItems = " + pollItems + ", properties = " + properties + ", type = " + type + "]";
    }
}
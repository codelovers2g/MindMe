package com.mindmesolo.mindme.Conversation;

/**
 * Created by enest_09 on 11/1/2016.
 */

public class ChatModel {
    private String orgId;

    private String createdOn;

    private String lastName;

    private String contactId;

    private String lastMessage;

    private String firstName;

    private String updatedOn;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Override
    public String toString() {
        return "ClassPojo [orgId = " + orgId + ", createdOn = " + createdOn + ", lastName = " + lastName + ", contactId = " + contactId + ", lastMessage = " + lastMessage + ", firstName = " + firstName + ", updatedOn = " + updatedOn + "]";
    }
}
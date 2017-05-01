package com.mindmesolo.mindme.Models;

/**
 * Created by enest_09 on 11/2/2016.
 */

public class LeadCaptureComponent {
    private String orgId;

    private String id;

    private LeadCaptureElementTypes[] leadCaptureElementTypes;

    private String order;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LeadCaptureElementTypes[] getLeadCaptureElementTypes() {
        return leadCaptureElementTypes;
    }

    public void setLeadCaptureElementTypes(LeadCaptureElementTypes[] leadCaptureElementTypes) {
        this.leadCaptureElementTypes = leadCaptureElementTypes;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "ClassPojo [orgId = " + orgId + ", id = " + id + ", leadCaptureElementTypes = " + leadCaptureElementTypes + ", order = " + order + "]";
    }
}
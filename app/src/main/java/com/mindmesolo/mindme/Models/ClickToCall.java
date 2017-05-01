package com.mindmesolo.mindme.Models;

/**
 * Created by enest_09 on 11/2/2016.
 */
public class ClickToCall {
    private String id;

    private String order;

    private String name;

    private Properties[] properties;

    private String orgID;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Properties[] getProperties() {
        return properties;
    }

    public void setProperties(Properties[] properties) {
        this.properties = properties;
    }

    public String getOrgID() {
        return orgID;
    }

    public void setOrgID(String orgID) {
        this.orgID = orgID;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", order = " + order + ", name = " + name + ", properties = " + properties + ", orgID = " + orgID + "]";
    }
}

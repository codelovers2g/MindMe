package com.mindmesolo.mindme.Models;

/**
 * Created by enest_09 on 11/2/2016.
 */

public class LinkCall {
    private String id;

    private String phone;

    private String email;

    private String link;

    private Properties[] properties;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Properties[] getProperties() {
        return properties;
    }

    public void setProperties(Properties[] properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", phone = " + phone + ", email = " + email + ", link = " + link + ", properties = " + properties + "]";
    }
}

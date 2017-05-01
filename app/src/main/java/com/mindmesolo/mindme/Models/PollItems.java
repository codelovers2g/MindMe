package com.mindmesolo.mindme.Models;

/**
 * Created by enest_09 on 11/2/2016.
 */
public class PollItems {
    private String id;

    private String description;

    private String link;

    private String label;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", description = " + description + ", link = " + link + ", label = " + label + "]";
    }
}
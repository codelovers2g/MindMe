package com.mindmesolo.mindme.Models;

/**
 * Created by enest_09 on 11/2/2016.
 */

public class Feedback {
    private String id;

    private String scale;

    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", scale = " + scale + ", description = " + description + "]";
    }
}
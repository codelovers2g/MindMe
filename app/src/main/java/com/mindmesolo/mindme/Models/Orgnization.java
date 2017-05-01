package com.mindmesolo.mindme.Models;

/**
 * Created by enest_09 on 11/2/2016.
 */

public class Orgnization {
    private Components[] components;

    public Components[] getComponents() {
        return components;
    }

    public void setComponents(Components[] components) {
        this.components = components;
    }

    @Override
    public String toString() {
        return "ClassPojo [components = " + components + "]";
    }
}

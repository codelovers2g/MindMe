package com.mindmesolo.mindme.CreateCampaign;

import java.util.ArrayList;

/**
 * Created by eNest on 7/12/2016.
 */
public class ChooserRouteData {
    public String title;
    public String count;
    public ArrayList<String> totalcontacts;
    public int priority;
    public int image;
    boolean selected = false;

    public ChooserRouteData(String title, String count, ArrayList totalcontacts, int priority, int image, boolean selected) {
        this.title = title;
        this.count = count;
        this.totalcontacts = totalcontacts;
        this.priority = priority;
        this.image = image;
        this.selected = selected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int position) {
        this.priority = position;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public ArrayList getTotalcontacts() {
        return totalcontacts;
    }

    public void setTotalcontacts(ArrayList totalcontacts) {
        this.totalcontacts = totalcontacts;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


}

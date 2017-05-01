package com.mindmesolo.mindme.LeadRoutes;

/**
 * Created by pc-14 on 4/8/2017.
 */

public class LeadCaptureModel {
    int Image;
    String Title;
    int LeadCount;
    String LeadCaptureDate;

    public LeadCaptureModel(String Title, int LeadCount, String LeadCaptureDate, int Image) {
        this.Title = Title;
        this.LeadCount = LeadCount;
        this.LeadCaptureDate = LeadCaptureDate;
        this.Image = Image;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getLeadCaptureDate() {
        return LeadCaptureDate;
    }

    public void setLeadCaptureDate(String leadCaptureDate) {
        LeadCaptureDate = leadCaptureDate;
    }

    public int getLeadCount() {
        return LeadCount;
    }

    public void setLeadCount(int leadCount) {
        LeadCount = leadCount;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

}

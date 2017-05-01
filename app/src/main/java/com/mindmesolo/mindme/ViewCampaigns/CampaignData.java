package com.mindmesolo.mindme.ViewCampaigns;

import android.graphics.Bitmap;

import com.mindmesolo.mindme.CreateMobilePages.models.BusinessHours;
import com.mindmesolo.mindme.CreateMobilePages.models.HoursSwitch;
import com.mindmesolo.mindme.CreateMobilePages.models.MapModel;
import com.mindmesolo.mindme.CreateMobilePages.models.SpecialOffer;
import com.mindmesolo.mindme.GettingStarted.SocialMediaModel;
import com.mindmesolo.mindme.Models.CommonModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by User1 on 7/14/2016.
 */
@SuppressWarnings("serial")
public class CampaignData implements Serializable {

    public String title;

    public int position;

    public int image;

    public Bitmap BitmapImage;

    public String ExtraDataTitle;

    public String ExtraDataParaGraph2;

    public Object CampaignObject;

    public ArrayList arrayList;

    public Object object;

    public String ExtraData;

    public String MediaType;

    public boolean isAudio;

    public String VideoExtraData;

    public String VideoExtraImageData;

    public ArrayList<CommonModel> LeadFormData;

    public ArrayList<CommonModel> LeadFormInterestsData;

    public ArrayList<SocialMediaModel> SocialMediaData;

    public ArrayList<BusinessHours> BusinessHoursData;

    public HoursSwitch hoursSwitch;

    public SpecialOffer specialOffer;

    public MapModel mapModel;

    public CampaignData() {
    }


    public CampaignData(String title, int image) {
        this.title = title;
        this.image = image;
    }

    public MapModel getMapModel() {
        return mapModel;
    }

    public void setMapModel(MapModel mapModel) {
        this.mapModel = mapModel;
    }

    public SpecialOffer getSpecialOffer() {
        return specialOffer;
    }

    public void setSpecialOffer(SpecialOffer specialOffer) {
        this.specialOffer = specialOffer;
    }

    public HoursSwitch getHoursSwitch() {
        return hoursSwitch;
    }

    public void setHoursSwitch(HoursSwitch hoursSwitch) {
        this.hoursSwitch = hoursSwitch;
    }

    public ArrayList<BusinessHours> getBusinessHoursData() {
        return BusinessHoursData;
    }

    public void setBusinessHoursData(ArrayList<BusinessHours> businessHoursData) {
        BusinessHoursData = businessHoursData;
    }

    public ArrayList<SocialMediaModel> getSocialMediaData() {
        return SocialMediaData;
    }

    public void setSocialMediaData(ArrayList<SocialMediaModel> socialMediaData) {
        SocialMediaData = socialMediaData;
    }

    public ArrayList<CommonModel> getLeadFormInterestsData() {
        return LeadFormInterestsData;
    }


    public void setLeadFormInterestsData(ArrayList<CommonModel> leadFormInterestsData) {
        LeadFormInterestsData = leadFormInterestsData;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ArrayList<CommonModel> getLeadFormData() {
        return LeadFormData;
    }

    public void setLeadFormData(ArrayList<CommonModel> leadFormData) {
        LeadFormData = leadFormData;
    }

    public String getVideoExtraData() {
        return VideoExtraData;
    }

    public void setVideoExtraData(String videoExtraData) {
        VideoExtraData = videoExtraData;
    }

    public String getVideoExtraImageData() {
        return VideoExtraImageData;
    }

    public void setVideoExtraImageData(String videoExtraImageData) {
        VideoExtraImageData = videoExtraImageData;
    }

    public boolean isAudio() {
        return isAudio;
    }

    public void setAudio(boolean audio) {
        isAudio = audio;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Bitmap getBitmapImage() {
        return BitmapImage;
    }

    public void setBitmapImage(Bitmap bitmap) {
        this.BitmapImage = bitmap;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getExtraDataTitle() {
        return ExtraDataTitle;
    }

    public void setExtraDataTitle(String extraDataTitle) {
        ExtraDataTitle = extraDataTitle;
    }

    public String getExtraDataParaGraph2() {
        return ExtraDataParaGraph2;
    }

    public void setExtraDataParaGraph2(String extraDataParaGraph2) {
        ExtraDataParaGraph2 = extraDataParaGraph2;
    }

    public Object getCampaignObject() {
        return CampaignObject;
    }

    public void setCampaignObject(Object campaignObject) {
        CampaignObject = campaignObject;
    }

    public ArrayList getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList arrayList) {
        this.arrayList = arrayList;
    }

    public String getExtraData() {
        return ExtraData;
    }

    public void setExtraData(String extraData) {
        ExtraData = extraData;
    }

    public String getMediaType() {
        return MediaType;
    }

    public void setMediaType(String mediaType) {
        MediaType = mediaType;
    }
}

package com.mindmesolo.mindme.GettingStarted;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by enest_09 on 9/23/2016.
 */
public class SocialMediaModel implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SocialMediaModel> CREATOR = new Parcelable.Creator<SocialMediaModel>() {
        @Override
        public SocialMediaModel createFromParcel(Parcel in) {
            return new SocialMediaModel(in);
        }

        @Override
        public SocialMediaModel[] newArray(int size) {
            return new SocialMediaModel[size];
        }
    };
    public String SocialMediaName;
    public String SocialMediaUrl;
    public String SocialMediaId;
    boolean isChecked;
    int SocialMediaImage;
    ;

    public SocialMediaModel(String SocialMediaName, String SocialMediaId, String SocialMediaUrl, int SocialMediaImage) {
        this.SocialMediaName = SocialMediaName;
        this.SocialMediaId = SocialMediaId;
        this.SocialMediaUrl = SocialMediaUrl;
        this.SocialMediaImage = SocialMediaImage;
    }

    ;

    public SocialMediaModel(String SocialMediaName, String SocialMediaId, boolean isChecked, int SocialMediaImage) {
        this.SocialMediaName = SocialMediaName;
        this.SocialMediaId = SocialMediaId;
        this.isChecked = isChecked;
        this.SocialMediaImage = SocialMediaImage;
    }

    protected SocialMediaModel(Parcel in) {
        SocialMediaName = in.readString();
        SocialMediaUrl = in.readString();
        SocialMediaId = in.readString();
        isChecked = in.readByte() != 0x00;
        SocialMediaImage = in.readInt();
    }

    public int getSocialMediaImage() {
        return SocialMediaImage;
    }

    public void setSocialMediaImage(int socialMediaImage) {
        this.SocialMediaImage = socialMediaImage;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getSocialMediaUrl() {
        return SocialMediaUrl;
    }

    public void setSocialMediaUrl(String socialMediaUrl) {
        this.SocialMediaUrl = socialMediaUrl;
    }

    public String getSocialMediaName() {
        return SocialMediaName;
    }

    public void setSocialMediaName(String socialMediaName) {
        this.SocialMediaName = socialMediaName;
    }

    public String getSocialMediaId() {
        return SocialMediaId;
    }

    public void setSocialMediaId(String socialMediaId) {
        this.SocialMediaId = socialMediaId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(SocialMediaName);
        dest.writeString(SocialMediaUrl);
        dest.writeString(SocialMediaId);
        dest.writeByte((byte) (isChecked ? 0x01 : 0x00));
        dest.writeInt(SocialMediaImage);
    }
}
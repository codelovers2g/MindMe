package com.mindmesolo.mindme.ViewMobilePages.Helper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User1 on 2/6/2017.
 */
public class ViewMobilePagesModel implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ViewMobilePagesModel> CREATOR = new Parcelable.Creator<ViewMobilePagesModel>() {
        @Override
        public ViewMobilePagesModel createFromParcel(Parcel in) {
            return new ViewMobilePagesModel(in);
        }

        @Override
        public ViewMobilePagesModel[] newArray(int size) {
            return new ViewMobilePagesModel[size];
        }
    };
    private String mobilePageId;
    private String mobilePageName;
    private String mobilePageUrl;
    private Integer mobilePageOpenCount;
    private Long mobilePageCreatedDate;
    private Integer mobilePageLeadCaptureCount;


    public ViewMobilePagesModel(String mobilePageId, String mobilePageName, String mobilePageUrl,
                                Integer mobilePageOpenCount, Long mobilePageCreatedDate, Integer mobilePageLeadCaptureCount) {
        this.mobilePageId = mobilePageId;
        this.mobilePageName = mobilePageName;
        this.mobilePageUrl = mobilePageUrl;
        this.mobilePageOpenCount = mobilePageOpenCount;
        this.mobilePageCreatedDate = mobilePageCreatedDate;
        this.mobilePageLeadCaptureCount = mobilePageLeadCaptureCount;
    }

    public ViewMobilePagesModel() {
    }

    protected ViewMobilePagesModel(Parcel in) {
        mobilePageId = in.readString();
        mobilePageName = in.readString();
        mobilePageUrl = in.readString();
        mobilePageOpenCount = in.readByte() == 0x00 ? null : in.readInt();
        mobilePageCreatedDate = in.readByte() == 0x00 ? null : in.readLong();
        mobilePageLeadCaptureCount = in.readByte() == 0x00 ? null : in.readInt();
    }

    public String getMobilePageId() {
        return mobilePageId;
    }

    public void setMobilePageId(String mobilePageId) {
        this.mobilePageId = mobilePageId;
    }

    public String getMobilePageName() {
        return mobilePageName;
    }

    public void setMobilePageName(String mobilePageName) {
        this.mobilePageName = mobilePageName;
    }

    public String getMobilePageUrl() {
        return mobilePageUrl;
    }

    public void setMobilePageUrl(String mobilePageUrl) {
        this.mobilePageUrl = mobilePageUrl;
    }

    public Integer getMobilePageOpenCount() {
        return mobilePageOpenCount;
    }

    public void setMobilePageOpenCount(Integer mobilePageOpenCount) {
        this.mobilePageOpenCount = mobilePageOpenCount;
    }

    public Long getMobilePageCreatedDate() {
        return mobilePageCreatedDate;
    }

    public void setMobilePageCreatedDate(Long mobilePageCreatedDate) {
        this.mobilePageCreatedDate = mobilePageCreatedDate;
    }

    public Integer getMobilePageLeadCaptureCount() {
        return mobilePageLeadCaptureCount;
    }

    public void setMobilePageLeadCaptureCount(Integer mobilePageLeadCaptureCount) {
        this.mobilePageLeadCaptureCount = mobilePageLeadCaptureCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mobilePageId);
        dest.writeString(mobilePageName);
        dest.writeString(mobilePageUrl);
        if (mobilePageOpenCount == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(mobilePageOpenCount);
        }
        if (mobilePageCreatedDate == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeLong(mobilePageCreatedDate);
        }
        if (mobilePageLeadCaptureCount == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(mobilePageLeadCaptureCount);
        }
    }
}
package com.mindmesolo.mindme.CreateMobilePages.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pc-14 on 3/8/2017.
 */

public class SpecialOffer implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SpecialOffer> CREATOR = new Parcelable.Creator<SpecialOffer>() {
        @Override
        public SpecialOffer createFromParcel(Parcel in) {
            return new SpecialOffer(in);
        }

        @Override
        public SpecialOffer[] newArray(int size) {
            return new SpecialOffer[size];
        }
    };
    public String name;
    public String description;
    public String offerAmount;
    public String offerType;
    public String offerUnit;
    public String other;
    public String expireType;
    public String expireDate;
    public String displayOfferOn;
    public String displayOfferMessage;

    public SpecialOffer() {
    }

    protected SpecialOffer(Parcel in) {
        name = in.readString();
        description = in.readString();
        offerAmount = in.readString();
        offerType = in.readString();
        offerUnit = in.readString();
        other = in.readString();
        expireType = in.readString();
        expireDate = in.readString();
        displayOfferOn = in.readString();
        displayOfferMessage = in.readString();
    }

    public static Creator<SpecialOffer> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(offerAmount);
        dest.writeString(offerType);
        dest.writeString(offerUnit);
        dest.writeString(other);
        dest.writeString(expireType);
        dest.writeString(expireDate);
        dest.writeString(displayOfferOn);
        dest.writeString(displayOfferMessage);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDisplayOfferMessage() {
        return displayOfferMessage;
    }

    public void setDisplayOfferMessage(String displayOfferMessage) {
        this.displayOfferMessage = displayOfferMessage;
    }

    public String getDisplayOfferOn() {
        return displayOfferOn;
    }

    public void setDisplayOfferOn(String displayOfferOn) {
        this.displayOfferOn = displayOfferOn;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getExpireType() {
        return expireType;
    }

    public void setExpireType(String expireType) {
        this.expireType = expireType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfferAmount() {
        return offerAmount;
    }

    public void setOfferAmount(String offerAmount) {
        this.offerAmount = offerAmount;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public String getOfferUnit() {
        return offerUnit;
    }

    public void setOfferUnit(String offerUnit) {
        this.offerUnit = offerUnit;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

}
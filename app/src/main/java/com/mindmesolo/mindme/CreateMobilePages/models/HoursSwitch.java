package com.mindmesolo.mindme.CreateMobilePages.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pc-14 on 3/7/2017.
 */
public class HoursSwitch implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<HoursSwitch> CREATOR = new Parcelable.Creator<HoursSwitch>() {
        @Override
        public HoursSwitch createFromParcel(Parcel in) {
            return new HoursSwitch(in);
        }

        @Override
        public HoursSwitch[] newArray(int size) {
            return new HoursSwitch[size];
        }
    };
    public boolean timeSwitchChecked;
    public boolean timeZoneSwitch;
    public String timeZone;

    public HoursSwitch(Parcel in) {
        timeSwitchChecked = in.readByte() != 0x00;
        timeZoneSwitch = in.readByte() != 0x00;
        timeZone = in.readString();
    }

    public HoursSwitch() {
    }

    public boolean isTimeSwitchChecked() {
        return timeSwitchChecked;
    }

    public void setTimeSwitchChecked(boolean timeSwitchChecked) {
        this.timeSwitchChecked = timeSwitchChecked;
    }

    public boolean isTimeZoneSwitch() {
        return timeZoneSwitch;
    }

    public void setTimeZoneSwitch(boolean timeZoneSwitch) {
        this.timeZoneSwitch = timeZoneSwitch;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (timeSwitchChecked ? 0x01 : 0x00));
        dest.writeByte((byte) (timeZoneSwitch ? 0x01 : 0x00));
        dest.writeString(timeZone);
    }
}
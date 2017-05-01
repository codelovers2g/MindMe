package com.mindmesolo.mindme.CreateMobilePages.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pc-14 on 3/2/2017.
 */

public class BusinessHours implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BusinessHours> CREATOR = new Parcelable.Creator<BusinessHours>() {
        @Override
        public BusinessHours createFromParcel(Parcel in) {
            return new BusinessHours(in);
        }

        @Override
        public BusinessHours[] newArray(int size) {
            return new BusinessHours[size];
        }
    };
    private Integer dayOfWeek;
    private Integer openOnHour;
    private Integer openOnMinute;
    private Integer closeOnHour;
    private Integer closeOnMinute;
    private Boolean byAppointmentOnly;
    private Boolean closed;

    ;

    public BusinessHours() {
    }


    public BusinessHours(Integer dayOfWeek, Integer openOnHour, Integer openOnMinute, Integer closeOnHour, Integer closeOnMinute, Boolean byAppointmentOnly, Boolean closed) {
        this.dayOfWeek = dayOfWeek;
        this.openOnHour = openOnHour;
        this.openOnMinute = openOnMinute;
        this.closeOnHour = closeOnHour;
        this.closeOnMinute = closeOnMinute;
        this.byAppointmentOnly = byAppointmentOnly;
        this.closed = closed;
    }

    protected BusinessHours(Parcel in) {
        dayOfWeek = in.readByte() == 0x00 ? null : in.readInt();
        openOnHour = in.readByte() == 0x00 ? null : in.readInt();
        openOnMinute = in.readByte() == 0x00 ? null : in.readInt();
        closeOnHour = in.readByte() == 0x00 ? null : in.readInt();
        closeOnMinute = in.readByte() == 0x00 ? null : in.readInt();
        byte byAppointmentOnlyVal = in.readByte();
        byAppointmentOnly = byAppointmentOnlyVal == 0x02 ? null : byAppointmentOnlyVal != 0x00;
        byte closedVal = in.readByte();
        closed = closedVal == 0x02 ? null : closedVal != 0x00;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getOpenOnHour() {
        return openOnHour;
    }

    public void setOpenOnHour(Integer openOnHour) {
        this.openOnHour = openOnHour;
    }

    public Integer getOpenOnMinute() {
        return openOnMinute;
    }

    public void setOpenOnMinute(Integer openOnMinute) {
        this.openOnMinute = openOnMinute;
    }

    public Integer getCloseOnHour() {
        return closeOnHour;
    }

    public void setCloseOnHour(Integer closeOnHour) {
        this.closeOnHour = closeOnHour;
    }

    public Integer getCloseOnMinute() {
        return closeOnMinute;
    }

    public void setCloseOnMinute(Integer closeOnMinute) {
        this.closeOnMinute = closeOnMinute;
    }

    public Boolean getByAppointmentOnly() {
        return byAppointmentOnly;
    }

    public void setByAppointmentOnly(Boolean byAppointmentOnly) {
        this.byAppointmentOnly = byAppointmentOnly;
    }

    public Boolean getClosed() {
        return closed;
    }

    public void setClosed(Boolean closed) {
        this.closed = closed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (dayOfWeek == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(dayOfWeek);
        }
        if (openOnHour == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(openOnHour);
        }
        if (openOnMinute == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(openOnMinute);
        }
        if (closeOnHour == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(closeOnHour);
        }
        if (closeOnMinute == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(closeOnMinute);
        }
        if (byAppointmentOnly == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (byAppointmentOnly ? 0x01 : 0x00));
        }
        if (closed == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (closed ? 0x01 : 0x00));
        }
    }
}

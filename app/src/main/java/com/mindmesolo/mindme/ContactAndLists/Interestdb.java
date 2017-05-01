package com.mindmesolo.mindme.ContactAndLists;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User1 on 6/3/2016.
 */
public class Interestdb implements Parcelable {
    public static final Creator<Interestdb> CREATOR = new Creator<Interestdb>() {
        @Override
        public Interestdb createFromParcel(Parcel in) {
            return new Interestdb(in);
        }

        @Override
        public Interestdb[] newArray(int size) {
            return new Interestdb[size];
        }
    };
    String code = null;
    String name = null;
    int len;
    String id = null;
    boolean selected = false;

    public Interestdb(String id, String name, int len, boolean selected) {
        super();
        this.id = id;
        this.name = name;
        this.len = len;
        this.selected = selected;
    }

    public Interestdb(String name, int len) {
        super();
        this.name = name;
        this.len = len;
    }

    protected Interestdb(Parcel in) {
        code = in.readString();
        name = in.readString();
        len = in.readInt();
        id = in.readString();
        selected = in.readByte() != 0;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getlen() {
        return len;
    }

    public void setlen(int len) {
        this.len = len;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(name);
        dest.writeString(id);
        dest.writeByte((byte) (selected ? 1 : 0));
        ;
    }
}


package com.mindmesolo.mindme.ContactAndLists;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User1 on 6/21/2016.
 */
public class TagDb implements Parcelable {
    public static final Creator<TagDb> CREATOR = new Creator<TagDb>() {
        @Override
        public TagDb createFromParcel(Parcel in) {
            return new TagDb(in);
        }

        @Override
        public TagDb[] newArray(int size) {
            return new TagDb[size];
        }
    };
    String code = null;
    String name = null;
    int len;
    String id = null;
    boolean selected = false;

    protected TagDb(Parcel in) {
        code = in.readString();
        name = in.readString();
        len = in.readInt();
        id = in.readString();
        selected = in.readByte() != 0;
    }

    public TagDb(String id, String name, int len, boolean selected) {
        super();
        this.id = id;
        this.name = name;
        this.len = len;
        this.selected = selected;
    }

    public TagDb(String name, int len) {
        super();
        this.name = name;
        this.len = len;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(name);
        dest.writeString(id);
        dest.writeByte((byte) (selected ? 1 : 0));
        ;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }


}

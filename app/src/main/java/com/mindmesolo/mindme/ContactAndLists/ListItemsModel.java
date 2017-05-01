package com.mindmesolo.mindme.ContactAndLists;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User1 on 6/18/2016.
 */
public class ListItemsModel implements Parcelable {

    public static final Creator<ListItemsModel> CREATOR = new Creator<ListItemsModel>() {
        @Override
        public ListItemsModel createFromParcel(Parcel in) {
            return new ListItemsModel(in);
        }

        @Override
        public ListItemsModel[] newArray(int size) {
            return new ListItemsModel[size];
        }
    };
    String code = null;
    String name = null;
    int len;
    String id = null;
    boolean selected = false;

    public ListItemsModel(String id, String name, int len, boolean selected) {
        super();
        this.id = id;
        this.name = name;
        this.len = len;
        this.selected = selected;
    }

    public ListItemsModel(String name, int len) {
        super();
        this.name = name;
        this.len = len;
    }

    protected ListItemsModel(Parcel in) {
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(name);
        dest.writeString(id);
        dest.writeByte((byte) (selected ? 1 : 0));
        ;
    }
}

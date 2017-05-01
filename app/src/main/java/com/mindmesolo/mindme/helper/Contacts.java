package com.mindmesolo.mindme.helper;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by eNest on 6/9/2016.
 */
public class Contacts implements Parcelable {
    public static final Creator<Contacts> CREATOR = new Creator<Contacts>() {
        @Override
        public Contacts createFromParcel(Parcel source) {
            return new Contacts(source);
        }

        @Override
        public Contacts[] newArray(int size) {
            return new Contacts[0];
        }
    };
    String code = null;
    String createON = null;
    String name = null;
    String id = null;
    String count = null;
    String email = null;
    String type = null;
    int progress = 0;
    String ImageBase64 = null;
    boolean selected = false;
    Bitmap image;
    boolean isEnable = false;
    ArrayList<String> contacts = null;

    public Contacts(String code, String type, String name, String id, String email, boolean selected) {
        super();
        this.code = code;
        this.name = name;
        this.type = type;
        this.id = id;
        this.email = email;
        this.selected = selected;
    }

    public Contacts(String id, String name, String count, boolean selected) {
        super();
        this.id = id;
        this.name = name;
        this.count = count;
        this.selected = selected;
    }

    public Contacts(String code, String name, String id, String email, boolean selected) {
        super();
        this.code = code;
        this.name = name;
        this.id = id;
        this.email = email;
        this.selected = selected;
    }

    public Contacts(String name, String count, ArrayList<String> Contacts, int progress) {
        this.name = name;
        this.count = count;
        this.contacts = Contacts;
        this.progress = progress;
    }

    public Contacts(String name, String id, String Image, String createON) {
        super();
        this.name = name;
        this.id = id;
        this.createON = createON;
        this.ImageBase64 = Image;
    }

    public Contacts(String name, String id, String Image) {
        super();
        this.name = name;
        this.id = id;
        this.ImageBase64 = Image;
    }

    public Contacts(String code, String name, ArrayList<String> contacts, String id, boolean selected) {
        super();
        this.code = code;
        this.name = name;
        this.contacts = contacts;
        this.id = id;
        this.selected = selected;
    }

    public Contacts(String name, boolean selected) {
        super();
        this.name = name;
        this.selected = selected;
    }

    public Contacts(String name, Bitmap image) {
        this.name = name;
        this.image = image;
    }


    public Contacts() {

    }

    private Contacts(Parcel in) {
        this.code = in.readString();
        this.name = in.readString();
        this.count = in.readString();
        this.type = in.readString();
        this.id = in.readString();
        this.createON = in.readString();
        this.email = in.readString();
        this.progress = in.readInt();
        this.selected = in.readByte() != 0;
        ;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(name);
        dest.writeString(count);
        dest.writeString(type);
        dest.writeString(id);
        dest.writeString(createON);
        dest.writeString(email);
        dest.writeInt(progress);
        dest.writeByte((byte) (selected ? 1 : 0));
        ;
    }

    public String getCreateON() {
        return createON;
    }

    public void setCreateON(String createON) {
        this.createON = createON;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public void setImage(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageBase64() {
        return ImageBase64;
    }

    public void setImageBase64(String imageBase64) {
        ImageBase64 = imageBase64;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public ArrayList<String> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<String> contacts) {
        this.contacts = contacts;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
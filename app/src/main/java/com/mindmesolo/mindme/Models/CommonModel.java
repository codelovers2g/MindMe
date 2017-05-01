package com.mindmesolo.mindme.Models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by enest_09 on 11/2/2016.
 */

public class CommonModel implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CommonModel> CREATOR = new Parcelable.Creator<CommonModel>() {
        @Override
        public CommonModel createFromParcel(Parcel in) {
            return new CommonModel(in);
        }

        @Override
        public CommonModel[] newArray(int size) {
            return new CommonModel[size];
        }
    };
    String Title;
    Boolean isChecked;
    String Count;
    int LocalImage;
    Bitmap Image;
    String Message;
    Boolean Required;

    public CommonModel(String Title, Boolean Required, Boolean isChecked) {
        this.Title = Title;
        this.isChecked = isChecked;
        this.Required = Required;
    }

    public CommonModel(String Title, Boolean isChecked) {
        this.Title = Title;
        this.isChecked = isChecked;
    }

    public CommonModel(String Title, String Message, Boolean isChecked) {
        this.Title = Title;
        this.Message = Message;
        this.isChecked = isChecked;
    }

    protected CommonModel(Parcel in) {
        Title = in.readString();
        byte isCheckedVal = in.readByte();
        isChecked = isCheckedVal == 0x02 ? null : isCheckedVal != 0x00;
        Count = in.readString();
        LocalImage = in.readInt();
        Image = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
        Message = in.readString();
        byte RequiredVal = in.readByte();
        Required = RequiredVal == 0x02 ? null : RequiredVal != 0x00;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getLocalImage() {
        return LocalImage;
    }

    public void setLocalImage(int localImage) {
        LocalImage = localImage;
    }

    public Boolean getRequired() {
        return Required;
    }

    public void setRequired(Boolean required) {
        Required = required;
    }

    public Bitmap getImage() {
        return Image;
    }

    public void setImage(Bitmap image) {
        Image = image;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Title);
        if (isChecked == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isChecked ? 0x01 : 0x00));
        }

        dest.writeString(Count);
        dest.writeInt(LocalImage);
        dest.writeValue(Image);
        dest.writeString(Message);
        if (Required == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (Required ? 0x01 : 0x00));
        }
    }
}




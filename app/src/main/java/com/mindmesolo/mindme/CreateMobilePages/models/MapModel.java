package com.mindmesolo.mindme.CreateMobilePages.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pc-14 on 3/10/2017.
 */
public class MapModel implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MapModel> CREATOR = new Parcelable.Creator<MapModel>() {
        @Override
        public MapModel createFromParcel(Parcel in) {
            return new MapModel(in);
        }

        @Override
        public MapModel[] newArray(int size) {
            return new MapModel[size];
        }
    };
    String media;
    double latitude;
    double longitude;
    String address;
    String name;
    String mapImage;
    String type;

    public MapModel() {
    }

    protected MapModel(Parcel in) {
        media = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        address = in.readString();
        name = in.readString();
        type = in.readString();
        mapImage = in.readString();
    }

    public String getMapImage() {
        return mapImage;
    }

    public void setMapImage(String mapImage) {
        this.mapImage = mapImage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(media);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(address);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeString(mapImage);
    }
}
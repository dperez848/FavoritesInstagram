package org.example.com.test3.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ImagesModel implements Parcelable{

    TextModel low_resolution;
    TextModel thumbnail;
    TextModel standard_resolution;

    protected ImagesModel(Parcel in) {
        low_resolution = in.readParcelable(TextModel.class.getClassLoader());
        thumbnail = in.readParcelable(TextModel.class.getClassLoader());
        standard_resolution = in.readParcelable(TextModel.class.getClassLoader());
    }

    public static final Creator<ImagesModel> CREATOR = new Creator<ImagesModel>() {
        @Override
        public ImagesModel createFromParcel(Parcel in) {
            return new ImagesModel(in);
        }

        @Override
        public ImagesModel[] newArray(int size) {
            return new ImagesModel[size];
        }
    };

    public TextModel getLow_resolution() {
        return low_resolution;
    }

    public void setLow_resolution(TextModel low_resolution) {
        this.low_resolution = low_resolution;
    }

    public TextModel getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(TextModel thumbnail) {
        this.thumbnail = thumbnail;
    }

    public TextModel getStandard_resolution() {
        return standard_resolution;
    }

    public void setStandard_resolution(TextModel standard_resolution) {
        this.standard_resolution = standard_resolution;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(low_resolution, i);
        parcel.writeParcelable(thumbnail, i);
        parcel.writeParcelable(standard_resolution, i);
    }
}

package org.example.com.test3.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TextModel implements Parcelable {
    String text, url;

    protected TextModel(Parcel in) {
        text = in.readString();
        url = in.readString();
    }

    public static final Creator<TextModel> CREATOR = new Creator<TextModel>() {
        @Override
        public TextModel createFromParcel(Parcel in) {
            return new TextModel(in);
        }

        @Override
        public TextModel[] newArray(int size) {
            return new TextModel[size];
        }
    };

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
        parcel.writeString(url);
    }
}

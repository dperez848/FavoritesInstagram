package org.example.com.test3.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FeedModel implements Parcelable {

    List<String> tags;
    ImagesModel images;
    TextModel caption;
    UserModel user;
    String created_time;
    boolean selected;

    protected FeedModel(Parcel in) {
        tags = in.createStringArrayList();
        images = in.readParcelable(ImagesModel.class.getClassLoader());
        caption = in.readParcelable(TextModel.class.getClassLoader());
        user = in.readParcelable(UserModel.class.getClassLoader());
        created_time = in.readString();
        selected = in.readByte() != 0;
    }

    public static final Creator<FeedModel> CREATOR = new Creator<FeedModel>() {
        @Override
        public FeedModel createFromParcel(Parcel in) {
            return new FeedModel(in);
        }

        @Override
        public FeedModel[] newArray(int size) {
            return new FeedModel[size];
        }
    };

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public ImagesModel getImages() {
        return images;
    }

    public void setImages(ImagesModel images) {
        this.images = images;
    }

    public TextModel getCaption() {
        return caption;
    }

    public void setCaption(TextModel caption) {
        this.caption = caption;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getStringDate(){

        long millisecond = Long.parseLong(this.created_time);
        return DateFormat.format("MM/dd/yyyy", new Date(millisecond*1000)).toString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(tags);
        parcel.writeParcelable(images, i);
        parcel.writeParcelable(caption, i);
        parcel.writeParcelable(user, i);
        parcel.writeString(created_time);
        parcel.writeByte((byte) (selected ? 1 : 0));
    }
}

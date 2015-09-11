package com.practo.ohai.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class NotificationPayload implements Parcelable {

    @SerializedName("title")
    public String title = "";

    @SerializedName("message")
    public String message = "";

    @SerializedName("image_url")
    public String imageUrl = "";

    @SerializedName("icon_url")
    public String iconUrl = "";

    @SerializedName("action1")
    public String primaryAction = "";

    @SerializedName("action2")
    public String secondaryAction = "";

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.message);
    }

    protected NotificationPayload(Parcel in) {
        this.title = in.readString();
        this.message = in.readString();
    }

    public static final Creator<NotificationPayload> CREATOR = new Creator<NotificationPayload>() {
        public NotificationPayload createFromParcel(Parcel source) {
            return new NotificationPayload(source);
        }

        public NotificationPayload[] newArray(int size) {
            return new NotificationPayload[size];
        }
    };
}

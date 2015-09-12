package com.practo.ohai.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class NotificationPayload implements Parcelable {

    @SerializedName("notification_id")
    public String notificationId = "";

    @SerializedName("content")
    public NotificationPayloadContent content;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.notificationId);
        dest.writeParcelable(this.content, 0);
    }

    protected NotificationPayload(Parcel in) {
        this.notificationId = in.readString();
        this.content = in.readParcelable(NotificationPayloadContent.class.getClassLoader());
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

package com.practo.ohai.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DeviceNotificationLog implements Parcelable {

    @SerializedName("opened")
    public String opened;

    @SerializedName("notification_id")
    public String notificationId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.opened);
        dest.writeString(this.notificationId);
    }

    protected DeviceNotificationLog(Parcel in) {
        this.opened = in.readString();
        this.notificationId = in.readString();
    }

    public static final Creator<DeviceNotificationLog> CREATOR = new Creator<DeviceNotificationLog>() {
        public DeviceNotificationLog createFromParcel(Parcel source) {
            return new DeviceNotificationLog(source);
        }

        public DeviceNotificationLog[] newArray(int size) {
            return new DeviceNotificationLog[size];
        }
    };
}

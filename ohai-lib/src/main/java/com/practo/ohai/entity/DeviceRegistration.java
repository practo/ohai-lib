package com.practo.ohai.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DeviceRegistration implements Parcelable {

    @SerializedName("mobile_number")
    public String mobileNumber;

    @SerializedName("email_id")
    public String emailId;

    @SerializedName("patient_name")
    public String name;

    @SerializedName("patient_id")
    public String patientId;

    @SerializedName("gcm_id")
    public String gcmId;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mobileNumber);
        dest.writeString(this.emailId);
        dest.writeString(this.name);
        dest.writeString(this.patientId);
        dest.writeString(this.gcmId);
    }

    protected DeviceRegistration(Parcel in) {
        this.mobileNumber = in.readString();
        this.emailId = in.readString();
        this.name = in.readString();
        this.patientId = in.readString();
        this.gcmId = in.readString();
    }

    public static final Creator<DeviceRegistration> CREATOR = new Creator<DeviceRegistration>() {
        public DeviceRegistration createFromParcel(Parcel source) {
            return new DeviceRegistration(source);
        }

        public DeviceRegistration[] newArray(int size) {
            return new DeviceRegistration[size];
        }
    };
}

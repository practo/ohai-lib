package com.practo.ohai.helper;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestTickle;
import com.practo.ohai.OhaiApplication;
import com.practo.ohai.OhaiConfig;
import com.practo.ohai.entity.DeviceRegistration;
import com.practo.ohai.utils.PractoGsonRequest;
import com.practo.ohai.utils.PreferenceUtils;

import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

public class BaseRequestHelper {

	public static final String API_URL = OhaiConfig.BASE_API_URL;

	public static final String URL_DEVICES = "/devices";

	protected Context mContext;

	protected boolean mRunAsync;

    public static final String X_AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";
    public static final String X_PROFILE_TOKEN_HEADER = "X-PROFILE-TOKEN";
    public static final String X_FABRIC_API_TOKEN_HEADER = "X-FABRIC-API-TOKEN";
    public static final String X_DROID_VERSION_HEADER = "X-DROID-VERSION";
    public static final String API_VERSION = "API-Version";
    public static final String ACCEPT = "Accept";
    public static final String ACCEPT_TYPE_JSON = "application/json";
    public static final String PARAM_NAME = "patient_name";
    public static final String PARAM_EMAIL = "email_id";
    public static final String PARAM_PHONE_NUMBER = "phone_number";
    public static final String PARAM_LOCATION = "location";
    public static final String PARAM_GCM_ID = "gcm_id";


	public static BaseRequestHelper getInstance(Context context) {
		return new BaseRequestHelper(context);
	}

	public BaseRequestHelper(Context context) {
		mContext = context;
	}

	public String getApiToken() {
		return PreferenceUtils.getApiToken(mContext);
	}

	public String getAuthToken() {
		return PreferenceUtils.getGcmRegistrationToken(mContext);
	}

	public boolean isSuccessfulResponse(NetworkResponse response) {
		switch (response.statusCode) {
			case HttpsURLConnection.HTTP_OK:
			case HttpsURLConnection.HTTP_CREATED:
			case HttpsURLConnection.HTTP_ACCEPTED:
				return true;
			default:
				return false;
		}
	}

	public void requestRegistration(JSONObject params) {
        PractoGsonRequest<DeviceRegistration> request = new PractoGsonRequest<>(Request.Method.POST,
                API_URL + URL_DEVICES, DeviceRegistration.class, null,
                params,
                null, null);
        RequestTickle volleyTickle = OhaiApplication.getInstance().getRequestTickle();
        volleyTickle.add(request);
        NetworkResponse response = volleyTickle.start();
        if (isSuccessfulResponse(response)) {
            PreferenceUtils.set(mContext, PreferenceUtils.IS_REGISTERED_TO_SERVER, Boolean.TRUE);
        } else {
            PreferenceUtils.set(mContext, PreferenceUtils.IS_REGISTERED_TO_SERVER, Boolean.FALSE);
        }
	}
 }

package com.practo.ohai;

import android.content.Context;
import android.os.Bundle;

import com.practo.ohai.helper.BaseRequestHelper;
import com.practo.ohai.services.OhaiRegistrationIntentService;
import com.practo.ohai.utils.PreferenceUtils;

public class Ohai {

    private static Ohai mOhai;
    private Context mContext;
    private Bundle mBundle;

    public static Ohai getInstance(Context context) {
        if(mOhai == null) {
            mOhai =  new Ohai(context);
        }

        return mOhai;
    }

    public Ohai setEmail(String email) {
        mBundle.putString(BaseRequestHelper.PARAM_EMAIL, email);
        return mOhai;
    }

    public Ohai setName(String name) {
        mBundle.putString(BaseRequestHelper.PARAM_NAME, name);
        return mOhai;
    }

    public Ohai setMobile(String mobile) {
        mBundle.putString(BaseRequestHelper.PARAM_MOBILE_NUMBER, mobile);
        return mOhai;
    }

    public Ohai setLocation(String city) {
        mBundle.putString(BaseRequestHelper.PARAM_LOCATION, city);
        return mOhai;
    }

    public Ohai(Context context) {
        mContext = context;
        mBundle = new Bundle();
    }

    public void start() {
        if(!PreferenceUtils.getBooleanPrefs(mContext, PreferenceUtils.IS_REGISTERED_TO_SERVER)) {
            OhaiRegistrationIntentService.register(mContext, mBundle);
        }
    }

    public void update() {
        if(PreferenceUtils.getBooleanPrefs(mContext, PreferenceUtils.IS_REGISTERED_TO_SERVER)) {
            OhaiRegistrationIntentService.update(mContext, mBundle);
        } else {
            OhaiRegistrationIntentService.register(mContext, mBundle);
        }
    }
}

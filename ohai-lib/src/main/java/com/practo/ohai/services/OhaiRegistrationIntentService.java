package com.practo.ohai.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.iid.InstanceID;
import com.practo.ohai.helper.BaseRequestHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class OhaiRegistrationIntentService extends IntentService {

    private static final String ACTION_REGISTER = "com.practo.ohai.action.register";
    private static final String ACTION_REFRESH = "com.practo.ohai.action.refresh";
    private static final String AUTHORIZED_ENTITY = "27481646441";
    private static final String SCOPE = "GCM";

    public static void register(Context context, Bundle params) {
        Intent registerIntent = new Intent(context, OhaiRegistrationIntentService.class);
        registerIntent.setAction(ACTION_REGISTER);
        registerIntent.putExtras(params);
        context.startService(registerIntent);
    }

    public static void refresh(Context context) {
        Intent registerIntent = new Intent(context, OhaiRegistrationIntentService.class);
        registerIntent.setAction(ACTION_REFRESH);
        context.startService(registerIntent);
    }

    public OhaiRegistrationIntentService() {
        super(OhaiRegistrationIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();

        try {
            if(ACTION_REGISTER.equals(action)) {
                doRegister(intent.getExtras());
            } else if(ACTION_REFRESH.equals(action)) {
                doRefresh();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doRegister(Bundle data) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject();
        String gcmRegistrationToken = InstanceID.getInstance(this).getToken(AUTHORIZED_ENTITY, SCOPE);
        String email = data.getString(BaseRequestHelper.PARAM_EMAIL, "");
        jsonObject.put(BaseRequestHelper.PARAM_EMAIL, email);
        jsonObject.put(BaseRequestHelper.PARAM_GCM_ID, gcmRegistrationToken);

        BaseRequestHelper.getInstance(this).requestRegistration(jsonObject);
    }

    private void doRefresh() throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject();
        String gcmRegistrationToken = InstanceID.getInstance(this).getToken(AUTHORIZED_ENTITY, SCOPE);
        jsonObject.put(BaseRequestHelper.PARAM_GCM_ID, gcmRegistrationToken);
        BaseRequestHelper.getInstance(this).requestRegistration(jsonObject);
    }

}

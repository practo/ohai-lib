package com.practo.ohai.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.iid.InstanceID;
import com.practo.ohai.BuildConfig;
import com.practo.ohai.helper.BaseRequestHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class OhaiRegistrationIntentService extends IntentService {

    private static final String ACTION_REGISTER = BuildConfig.APPLICATION_ID + ".action.REGISTER";
    private static final String ACTION_REFRESH = BuildConfig.APPLICATION_ID + ".action.REFRESH";
    private static final String ACTION_NOTIFICATION_CANCELLED = BuildConfig.APPLICATION_ID + ".action" +
            ".NOTIFICATION_CANCELLED";
    private static final String ACTION_NOTIFICATION_OPEN = BuildConfig.APPLICATION_ID + ".action" +
            ".NOTIFICATION_OPEN";
    private static final String ACTION_UPDATE = BuildConfig.APPLICATION_ID + ".action" +
            ".UPDATE";
    private static final String AUTHORIZED_ENTITY = "27481646441";
    private static final String SCOPE = "GCM";

    public static void register(Context context, Bundle params) {
        Intent registerIntent = new Intent(context, OhaiRegistrationIntentService.class);
        registerIntent.setAction(ACTION_REGISTER);
        registerIntent.putExtras(params);
        context.startService(registerIntent);
    }

    public static void logNotificationCancellation(Context context, Bundle params) {
        Intent logCancellationIntent = new Intent(context, OhaiRegistrationIntentService.class);
        logCancellationIntent.setAction(ACTION_NOTIFICATION_CANCELLED);
        logCancellationIntent.putExtras(params);
        context.startService(logCancellationIntent);
    }

    public static void logNotificationOpen(Context context, Bundle params) {
        Intent logCancellationIntent = new Intent(context, OhaiRegistrationIntentService.class);
        logCancellationIntent.setAction(ACTION_NOTIFICATION_OPEN);
        logCancellationIntent.putExtras(params);
        context.startService(logCancellationIntent);
    }

    public static void refresh(Context context) {
        Intent registerIntent = new Intent(context, OhaiRegistrationIntentService.class);
        registerIntent.setAction(ACTION_REFRESH);
        context.startService(registerIntent);
    }

    public static void update(Context context, Bundle params) {
        Intent updateIntent = new Intent(context, OhaiRegistrationIntentService.class);
        updateIntent.setAction(ACTION_UPDATE);
        updateIntent.putExtras(params);
        context.startService(updateIntent);
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
            } else if(ACTION_NOTIFICATION_CANCELLED.equals(action)) {
                doNotificationCancelLog(intent.getExtras());
            } else if(ACTION_UPDATE.equals(action)) {
                doUpdate(intent.getExtras());
            } else if(ACTION_NOTIFICATION_OPEN.equals(action)) {
                doNotificationOpen(intent.getExtras());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doRegister(Bundle data) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject();
        String gcmRegistrationToken = InstanceID.getInstance(this).getToken(AUTHORIZED_ENTITY, SCOPE);
        jsonObject.put(BaseRequestHelper.PARAM_EMAIL, data.getString(BaseRequestHelper.PARAM_EMAIL, ""));
        jsonObject.put(BaseRequestHelper.PARAM_GCM_ID, gcmRegistrationToken);
        jsonObject.put(BaseRequestHelper.PARAM_LOCATION, data.getString(BaseRequestHelper.PARAM_LOCATION, ""));
        jsonObject.put(BaseRequestHelper.PARAM_NAME, data.getString(BaseRequestHelper.PARAM_NAME, ""));
        jsonObject.put(BaseRequestHelper.PARAM_MOBILE_NUMBER, data.getString(BaseRequestHelper.PARAM_MOBILE_NUMBER, ""));

        BaseRequestHelper.getInstance(this).requestRegistration(jsonObject);
    }

    private void doUpdate(Bundle data) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject();
        String gcmRegistrationToken = InstanceID.getInstance(this).getToken(AUTHORIZED_ENTITY, SCOPE);
        jsonObject.put(BaseRequestHelper.PARAM_EMAIL, data.getString(BaseRequestHelper.PARAM_EMAIL, ""));
        jsonObject.put(BaseRequestHelper.PARAM_GCM_ID, gcmRegistrationToken);
        jsonObject.put(BaseRequestHelper.PARAM_LOCATION, data.getString(BaseRequestHelper.PARAM_LOCATION, ""));
        jsonObject.put(BaseRequestHelper.PARAM_NAME, data.getString(BaseRequestHelper.PARAM_NAME, ""));
        jsonObject.put(BaseRequestHelper.PARAM_MOBILE_NUMBER, data.getString(BaseRequestHelper.PARAM_MOBILE_NUMBER, ""));

        BaseRequestHelper.getInstance(this).requestUpdate(jsonObject);
    }

    private void doRefresh() throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject();
        String gcmRegistrationToken = InstanceID.getInstance(this).getToken(AUTHORIZED_ENTITY, SCOPE);
        jsonObject.put(BaseRequestHelper.PARAM_GCM_ID, gcmRegistrationToken);
        BaseRequestHelper.getInstance(this).requestRegistration(jsonObject);
    }

    private void doNotificationCancelLog(Bundle data) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(BaseRequestHelper.PARAM_NOTIFICATION_ID, data.getString(BaseRequestHelper.PARAM_NOTIFICATION_ID));
        jsonObject.put(BaseRequestHelper.PARAM_OPENED, Boolean.FALSE);
        BaseRequestHelper.getInstance(this).requestNotificationCancellationLog(jsonObject);
    }

    private void doNotificationOpen(Bundle data) throws IOException, JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(BaseRequestHelper.PARAM_NOTIFICATION_ID, data.getString(BaseRequestHelper.PARAM_NOTIFICATION_ID));
        jsonObject.put(BaseRequestHelper.PARAM_OPENED, Boolean.TRUE);
        BaseRequestHelper.getInstance(this).requestNotificationCancellationLog(jsonObject);
    }

}

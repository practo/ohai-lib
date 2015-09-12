package com.practo.ohai.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;
import com.google.gson.Gson;
import com.practo.ohai.BuildConfig;
import com.practo.ohai.R;
import com.practo.ohai.entity.NotificationPayload;
import com.practo.ohai.entity.NotificationPayloadContent;
import com.practo.ohai.helper.BaseRequestHelper;
import com.practo.ohai.receivers.NotificationBroadcastReceiver;
import com.practo.ohai.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class OhaiGcmListenerService extends GcmListenerService {

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private static final int NOTIFICATION_ID = 1998;
    private static final int REQUEST_CODE_ACTION_PI = 1999;
    private static final int REQUEST_CODE_DELETE_PI = 2000;
    private static final String ACTION_NOTIFICATION_CANCELLED = BuildConfig.APPLICATION_ID + ".action" +
            ".NOTIFICATION_CANCELLED";
    private static final String MESSAGE = "message";

    public OhaiGcmListenerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this)
                        .setAutoCancel(Boolean.TRUE)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setSmallIcon(R.drawable.ic_notify);
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        try {
            String message = data.getString(MESSAGE, "");
            if(!Utils.isEmptyString(message)) {
                Gson gson = new Gson();
                NotificationPayload notificationPayload = gson.fromJson(message, NotificationPayload.class);
                showNotification(notificationPayload.content, notificationPayload.notificationId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onMessageReceived(from, data);
    }

    private void showNotification(NotificationPayloadContent notificationPayloadContent, String notificationId) throws
            IOException {
        String title = notificationPayloadContent.title;
        if(!Utils.isEmptyString(title)) {
            mBuilder.setContentTitle(title);
        }

        String message = notificationPayloadContent.message;
        if(!Utils.isEmptyString(message)) {
            mBuilder.setContentText(message);
        }

        String iconUrl = notificationPayloadContent.iconUrl;
        if(!Utils.isEmptyString(iconUrl)) {
            setNotificationBigImage(iconUrl);
        }

        String imageUrl = notificationPayloadContent.imageUrl;
        if(!Utils.isEmptyString(imageUrl)) {
            setStyle(title, message, imageUrl);
        }

        String primaryAction = notificationPayloadContent.primaryAction;
        if(!Utils.isEmptyString(primaryAction)) {
            mBuilder.setContentIntent(getActionIntent(""));
        }

        if(!Utils.isEmptyString(notificationId)) {
            mBuilder.setDeleteIntent(getDeleteIntent(notificationId));
        }

        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void setNotificationBigImage(String url) throws IOException {
        if(!Utils.isEmptyString(url)) {
            URL imageUrl = new URL(url);
            Bitmap bmp = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
            mBuilder.setLargeIcon(bmp);
        }
    }

    private PendingIntent getActionIntent(String action) {
        Intent intent = new Intent(action);
        return PendingIntent.getActivity(this, REQUEST_CODE_ACTION_PI, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getDeleteIntent(String notificationId) {
        Bundle extras = new Bundle();
        extras.putString(BaseRequestHelper.PARAM_NOTIFICATION_ID, notificationId);
        Intent intent = new Intent(this, NotificationBroadcastReceiver.class);
        intent.setAction(ACTION_NOTIFICATION_CANCELLED);
        intent.putExtras(extras);
        return PendingIntent.getBroadcast(this, REQUEST_CODE_DELETE_PI, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private void setStyle(String title, String summary, String url) {
        Bitmap bigPicture = null;
        NotificationCompat.BigPictureStyle notificationStyle = new
                NotificationCompat.BigPictureStyle();
        notificationStyle.setBigContentTitle(title);
        notificationStyle.setSummaryText(summary);

        try {
            bigPicture = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        notificationStyle.bigPicture(bigPicture);
        mBuilder.setStyle(notificationStyle);
    }
}

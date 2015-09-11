package com.practo.ohai.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.practo.ohai.services.OhaiRegistrationIntentService;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    public NotificationBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        OhaiRegistrationIntentService.logNotificationCancellation(context, intent.getExtras());
    }
}

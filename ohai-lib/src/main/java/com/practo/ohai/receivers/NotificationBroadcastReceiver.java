package com.practo.ohai.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.practo.ohai.services.OhaiGcmListenerService;
import com.practo.ohai.services.OhaiRegistrationIntentService;
import com.practo.ohai.utils.Utils;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    public NotificationBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if(Utils.isEmptyString(action)) {
            return;
        }

        if(OhaiGcmListenerService.ACTION_NOTIFICATION_CANCELLED.equals(action)) {
            OhaiRegistrationIntentService.logNotificationCancellation(context, intent.getExtras());
        } else if(OhaiGcmListenerService.ACTION_NOTIFICATION_VIEW.equals(action)) {
            OhaiRegistrationIntentService.logNotificationOpen(context, intent.getExtras());
            Intent viewIntent = new Intent(Intent.ACTION_VIEW);
            viewIntent.setData(intent.getData());
            viewIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(viewIntent);
        }
    }
}

package com.practo.ohai.services;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

public class OhaiGcmListenerService extends GcmListenerService {

    public OhaiGcmListenerService() {
    }

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
    }
}

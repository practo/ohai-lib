package com.practo.ohai.services;

import com.google.android.gms.iid.InstanceIDListenerService;

public class OhaiInstantListenerService extends InstanceIDListenerService {

    public OhaiInstantListenerService() {
    }

    @Override
    public void onTokenRefresh() {
        OhaiRegistrationIntentService.refresh(this);
    }
}

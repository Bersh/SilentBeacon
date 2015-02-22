package com.yalantis.silentbeacon;

import android.app.Application;
import android.content.Intent;

import com.yalantis.silentbeacon.service.SilentBeaconService;

public class SilentApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, SilentBeaconService.class));
    }
}

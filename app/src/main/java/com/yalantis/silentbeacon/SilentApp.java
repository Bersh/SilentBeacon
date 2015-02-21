package com.yalantis.silentbeacon;

import android.app.Application;
import android.content.Intent;

public class SilentApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, SilentBeaconService.class));
    }
}

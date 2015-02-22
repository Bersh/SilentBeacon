package com.yalantis.silentbeacon.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yalantis.silentbeacon.service.SilentBeaconService;


public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, SilentBeaconService.class));
    }
}

package com.yalantis.silentbeacon.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.yalantis.silentbeacon.service.SilentBeaconService;

public class RingerModeChangedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.getRingerMode() > AudioManager.RINGER_MODE_VIBRATE) {
            context.startService(new Intent(context, SilentBeaconService.class));
        }
    }
}

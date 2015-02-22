package com.yalantis.silentbeacon.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;

import com.yalantis.silentbeacon.Constants;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;


public class SilentBeaconService extends Service implements BeaconConsumer {
    private BeaconManager beaconManager;
    private AudioManager audioManager;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        beaconManager.setBackgroundMode(true);
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        beaconManager.bind(this);
    }

    @Override
    public void onDestroy() {
        beaconManager.unbind(this);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.d(Constants.LOG_TAG, "I just saw an beacon for the first time!");
                Log.d(Constants.LOG_TAG, "Region id - " + region.getUniqueId());

                if (audioManager.getRingerMode() != AudioManager.RINGER_MODE_VIBRATE) {
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    Log.d(Constants.LOG_TAG, "RINGER_MODE_VIBRATE set");
                }
                boolean switchSoundOn = sharedPreferences.getBoolean(Constants.SHARED_PREFERENCES_KEY_TURN_SOUND_ON, false);
                if (!switchSoundOn) {
                    stopSelf();
                }
            }

            @Override
            public void didExitRegion(Region region) {
                boolean switchSoundOn = sharedPreferences.getBoolean(Constants.SHARED_PREFERENCES_KEY_TURN_SOUND_ON, false);
                if (switchSoundOn) {
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }

                Log.d(Constants.LOG_TAG, "I no longer see a beacon");
                Log.d(Constants.LOG_TAG, "Region id - " + region.getUniqueId());
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
            }
        });

        try {
            Identifier identifier = Identifier.parse("E2C56DB5-DFFB-48D2-B060-D0F5A71096E0"); //mini beacon
            Identifier identifier2 = Identifier.parse("F7826DA6-4FA2-4E98-8024-BC5B71E0893E"); //kontakt
            beaconManager.startMonitoringBeaconsInRegion(new Region(Constants.REGION1_ID, identifier, null, null));
            beaconManager.startMonitoringBeaconsInRegion(new Region(Constants.REGION2_ID, identifier2, null, null));
        } catch (RemoteException e) {
            Log.e(Constants.LOG_TAG, e.getMessage(), e);
        }
    }
}

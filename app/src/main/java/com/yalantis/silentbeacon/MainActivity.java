package com.yalantis.silentbeacon;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnStartService = (Button) findViewById(R.id.btn_start_service);
        Button btnStopService = (Button) findViewById(R.id.btn_stop_service);
        Switch swSwitchSoundOn = (Switch) findViewById(R.id.sw_switch_sound_on);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        swSwitchSoundOn.setChecked(sharedPreferences.getBoolean(Constants.SHARED_PREFERENCES_KEY_TURN_SOUND_ON, false));

        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MainActivity.this, SilentBeaconService.class));
            }
        });

        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, SilentBeaconService.class));
            }
        });

        swSwitchSoundOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked != sharedPreferences.getBoolean(Constants.SHARED_PREFERENCES_KEY_TURN_SOUND_ON, false)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(Constants.SHARED_PREFERENCES_KEY_TURN_SOUND_ON, isChecked);
                    editor.apply();
                }
            }
        });
    }

}

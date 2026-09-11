package com.example.adbfreereceiver;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

/** Home-screen shortcut target. Toggles the existing receiver and exits immediately. */
public class ReceiverShortcutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ReceiverService.running) {
            Intent stop = new Intent(this, ReceiverService.class);
            stop.setAction(ReceiverService.ACTION_STOP);
            startService(stop);
        } else {
            Intent start = new Intent(this, ReceiverService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(start);
            } else {
                startService(start);
            }
        }
        finish();
    }
}

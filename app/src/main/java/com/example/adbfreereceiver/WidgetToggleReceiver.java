package com.example.adbfreereceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class WidgetToggleReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
        if (!ReceiverWidgetProvider.ACTION_TOGGLE.equals(intent.getAction())) return;

        if (ReceiverService.running) {
            Intent stop = new Intent(context, ReceiverService.class);
            stop.setAction(ReceiverService.ACTION_STOP);
            context.startService(stop);
        } else {
            Intent start = new Intent(context, ReceiverService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) context.startForegroundService(start);
            else context.startService(start);
        }
    }
}

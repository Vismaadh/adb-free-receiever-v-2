package com.example.adbfreereceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class WidgetToggleReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!ReceiverWidgetProvider.ACTION_TOGGLE.equals(intent.getAction())) {
            return;
        }

        if (ReceiverService.running) {
            context.stopService(new Intent(context, ReceiverService.class));
            ReceiverWidgetProvider.updateWidgets(context, false);
        } else {
            Intent serviceIntent = new Intent(context, ReceiverService.class);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(serviceIntent);
            } else {
                context.startService(serviceIntent);
            }

            // ReceiverService sets running=true during startServer().
            ReceiverWidgetProvider.updateWidgets(context, ReceiverService.running);
        }
    }
}

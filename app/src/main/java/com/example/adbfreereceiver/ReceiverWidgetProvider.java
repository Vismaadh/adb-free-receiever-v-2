package com.example.adbfreceiver;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class ReceiverWidgetProvider extends AppWidgetProvider {
    public static final String ACTION_TOGGLE = "com.example.adbfreceiver.WIDGET_TOGGLE";

    public static void updateWidgets(Context context, boolean running) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        ComponentName component = new ComponentName(context, ReceiverWidgetProvider.class);
        for (int id : manager.getAppWidgetIds(component)) {
            updateWidget(context, manager, id, running);
        }
    }

    private static void updateWidget(Context context, AppWidgetManager manager,
                                     int widgetId, boolean running) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_receiver);

        // Keep all visual geometry in widget_receiver.xml.
        // No runtime resizing or launcher-specific positioning is applied here.

        views.setInt(R.id.widget_tile, "setBackgroundResource",
                running ? R.drawable.widget_receiver_on : R.drawable.widget_receiver_off);
        views.setImageViewResource(R.id.widget_icon, R.drawable.receiver_glyph_exact);
        views.setTextViewText(R.id.widget_label,
                context.getString(R.string.widget_receiver_label));
        views.setContentDescription(R.id.widget_tile,
                running ? "Receiver is ON. Tap to turn it off."
                        : "Receiver is OFF. Tap to turn it on.");

        Intent toggle = new Intent(context, WidgetToggleReceiver.class);
        toggle.setAction(ACTION_TOGGLE);
        PendingIntent pi = PendingIntent.getBroadcast(
                context, widgetId, toggle,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_tile, pi);

        manager.updateAppWidget(widgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        boolean state = ReceiverService.running ||
                context.getSharedPreferences("receiver_state", Context.MODE_PRIVATE)
                        .getBoolean("running", false);
        for (int id : ids) {
            updateWidget(context, manager, id, state);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager manager,
                                          int appWidgetId,
                                          android.os.Bundle newOptions) {
        boolean state = ReceiverService.running ||
                context.getSharedPreferences("receiver_state", Context.MODE_PRIVATE)
                        .getBoolean("running", false);
        updateWidget(context, manager, appWidgetId, state);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ReceiverService.ACTION_STATE_CHANGED.equals(intent.getAction())) {
            updateWidgets(context, intent.getBooleanExtra("running", false));
        }
    }
}

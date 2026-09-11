package com.example.adbfreereceiver;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class ReceiverWidgetProvider extends AppWidgetProvider {
    public static final String ACTION_TOGGLE = "com.example.adbfreereceiver.WIDGET_TOGGLE";

    public static void updateWidgets(Context context, boolean running) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        ComponentName component = new ComponentName(context, ReceiverWidgetProvider.class);
        for (int id : manager.getAppWidgetIds(component)) {
            updateWidget(context, manager, id, running);
        }
    }

    private static void updateWidget(Context context, AppWidgetManager manager, int widgetId, boolean running) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_receiver);
        views.setInt(R.id.widget_icon, "setBackgroundResource",
                running ? R.drawable.widget_receiver_on : R.drawable.widget_receiver_off);
        views.setImageViewResource(R.id.widget_icon,
                running ? R.drawable.widget_receiver_on_icon : R.drawable.widget_receiver_off_icon);
        views.setContentDescription(R.id.widget_icon,
                running ? "Receiver is ON. Tap to turn it off." : "Receiver is OFF. Tap to turn it on.");

        Intent toggle = new Intent(context, WidgetToggleReceiver.class);
        toggle.setAction(ACTION_TOGGLE);
        android.app.PendingIntent pi = android.app.PendingIntent.getBroadcast(
                context, widgetId, toggle,
                android.app.PendingIntent.FLAG_UPDATE_CURRENT | android.app.PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_icon, pi);
        manager.updateAppWidget(widgetId, views);
    }

    @Override public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        boolean state = ReceiverService.running ||
                context.getSharedPreferences("receiver_state", Context.MODE_PRIVATE)
                        .getBoolean("running", false);
        for (int id : ids) updateWidget(context, manager, id, state);
    }

    @Override public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ReceiverService.ACTION_STATE_CHANGED.equals(intent.getAction())) {
            updateWidgets(context, intent.getBooleanExtra("running", false));
        }
    }
}

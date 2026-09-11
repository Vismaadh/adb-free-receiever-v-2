package com.example.adbfreereceiver;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class ReceiverWidgetProvider extends AppWidgetProvider {

    public static final String ACTION_TOGGLE =
            "com.example.adbfreereceiver.WIDGET_TOGGLE";

    public static void updateWidgets(Context context, boolean running) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        ComponentName component = new ComponentName(context, ReceiverWidgetProvider.class);
        int[] ids = manager.getAppWidgetIds(component);

        for (int id : ids) {
            updateWidget(context, manager, id, running);
        }
    }

    private static void updateWidget(Context context,
                                     AppWidgetManager manager,
                                     int widgetId,
                                     boolean running) {
        RemoteViews views = new RemoteViews(
                context.getPackageName(),
                R.layout.widget_receiver);

        views.setTextViewText(
                R.id.widget_status,
                running ? "ON" : "OFF");

        views.setContentDescription(
                R.id.widget_status,
                running ? "Receiver is ON. Tap to turn it off."
                        : "Receiver is OFF. Tap to turn it on.");

        views.setInt(
                R.id.widget_status,
                "setBackgroundResource",
                running ? R.drawable.widget_on : R.drawable.widget_off);

        Intent toggle = new Intent(context, WidgetToggleReceiver.class);
        toggle.setAction(ACTION_TOGGLE);

        android.app.PendingIntent pendingIntent =
                android.app.PendingIntent.getBroadcast(
                        context,
                        widgetId,
                        toggle,
                        android.app.PendingIntent.FLAG_UPDATE_CURRENT
                                | android.app.PendingIntent.FLAG_IMMUTABLE);

        views.setOnClickPendingIntent(R.id.widget_status, pendingIntent);
        manager.updateAppWidget(widgetId, views);
    }

    @Override
    public void onUpdate(Context context,
                         AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        boolean running = ReceiverService.running;
        for (int id : appWidgetIds) {
            updateWidget(context, appWidgetManager, id, running);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if ("com.example.adbfreereceiver.RECEIVER_STATE_CHANGED"
                .equals(intent.getAction())) {
            boolean running = intent.getBooleanExtra("running", false);
            updateWidgets(context, running);
        }
    }
}

package com.example.adbfreereceiver;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.TypedValue;
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

        // Use the launcher-supplied 1x1 widget bounds instead of assuming one
        // fixed 64x68dp cell. This keeps the tile+label group centered across
        // MIUI/HyperOS versions and different icon densities.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            android.os.Bundle options = manager.getAppWidgetOptions(widgetId);
            int widthDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, 0);
            int heightDp = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, 0);
            if (widthDp > 0 && heightDp > 0) {
                float density = context.getResources().getDisplayMetrics().density;
                float labelH = 18f;
                float gap = 2f;
                float maxTile = Math.min(widthDp, heightDp - labelH - gap);
                // Keep a little breathing room so the tile does not touch the
                // widget-cell edges. Never shrink below a usable 40dp tile.
                float tile = Math.max(40f, Math.min(48f, maxTile * 0.94f));
                float icon = tile * (30f / 48f);
                float labelW = Math.min(72f, Math.max(56f, tile + 16f));

                views.setViewLayoutWidth(R.id.widget_tile, TypedValue.COMPLEX_UNIT_DIP, tile);
                views.setViewLayoutHeight(R.id.widget_tile, TypedValue.COMPLEX_UNIT_DIP, tile);
                views.setViewLayoutWidth(R.id.widget_icon, TypedValue.COMPLEX_UNIT_DIP, icon);
                views.setViewLayoutHeight(R.id.widget_icon, TypedValue.COMPLEX_UNIT_DIP, icon);
                views.setViewLayoutWidth(R.id.widget_label, TypedValue.COMPLEX_UNIT_DIP, labelW);
            }
        }

        views.setInt(R.id.widget_tile, "setBackgroundResource",
                running ? R.drawable.widget_receiver_on : R.drawable.widget_receiver_off);
        views.setImageViewResource(R.id.widget_icon, R.drawable.receiver_glyph_exact);
        views.setTextViewText(R.id.widget_label, context.getString(R.string.widget_receiver_label));
        views.setContentDescription(R.id.widget_tile,
                running ? "Receiver is ON. Tap to turn it off." : "Receiver is OFF. Tap to turn it on.");

        Intent toggle = new Intent(context, WidgetToggleReceiver.class);
        toggle.setAction(ACTION_TOGGLE);
        PendingIntent pi = PendingIntent.getBroadcast(
                context, widgetId, toggle,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_tile, pi);
        manager.updateAppWidget(widgetId, views);
    }

    @Override public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        boolean state = ReceiverService.running ||
                context.getSharedPreferences("receiver_state", Context.MODE_PRIVATE)
                        .getBoolean("running", false);
        for (int id : ids) updateWidget(context, manager, id, state);
    }

    @Override public void onAppWidgetOptionsChanged(Context context, AppWidgetManager manager,
                                                      int appWidgetId, android.os.Bundle newOptions) {
        boolean state = ReceiverService.running ||
                context.getSharedPreferences("receiver_state", Context.MODE_PRIVATE)
                        .getBoolean("running", false);
        updateWidget(context, manager, appWidgetId, state);
    }

    @Override public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ReceiverService.ACTION_STATE_CHANGED.equals(intent.getAction())) {
            updateWidgets(context, intent.getBooleanExtra("running", false));
        }
    }
}

package com.example.adbfreereceiver;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import java.util.Collections;

public final class ReceiverShortcutHelper {
    private static final String ID = "receiver_toggle";
    private ReceiverShortcutHelper() {}

    public static void update(Context context, boolean running) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) return;
        ShortcutManager sm = context.getSystemService(ShortcutManager.class);
        if (sm == null) return;
        Intent intent = new Intent(context, ReceiverShortcutActivity.class);
        intent.setAction("com.example.adbfreereceiver.RECEIVER_SHORTCUT_TOGGLE");
        int icon = running ? R.drawable.widget_receiver_on_icon : R.drawable.widget_receiver_off_icon;
        ShortcutInfo info = new ShortcutInfo.Builder(context, ID)
                .setShortLabel(context.getString(R.string.widget_receiver_label))
                .setLongLabel(context.getString(R.string.widget_receiver_label))
                .setIcon(Icon.createWithResource(context, icon))
                .setIntent(intent)
                .build();
        sm.setDynamicShortcuts(Collections.singletonList(info));
    }
}

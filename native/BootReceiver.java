package com.fajr.azan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context ctx, Intent intent) {
        long t = AlarmScheduler.saved(ctx);
        if (t <= 0) return;
        long day = 24L * 60 * 60 * 1000;
        long now = System.currentTimeMillis();
        while (t <= now) t += day;
        AlarmScheduler.schedule(ctx, t);
    }
}

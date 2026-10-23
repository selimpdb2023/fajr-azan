package com.fajr.azan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context ctx, Intent intent) {
        long fired = AlarmScheduler.saved(ctx);
        // পরের দিনের জন্য আগেই সেট করে রাখা হয়; অ্যাপ খুললে সঠিক সময় দিয়ে আবার হালনাগাদ হয়
        if (fired > 0) AlarmScheduler.schedule(ctx, fired + 24L * 60 * 60 * 1000);
        ContextCompat.startForegroundService(ctx, new Intent(ctx, AlarmService.class));
    }
}

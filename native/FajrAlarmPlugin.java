package com.fajr.azan;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

@CapacitorPlugin(name = "FajrAlarm")
public class FajrAlarmPlugin extends Plugin {

    @PluginMethod
    public void schedule(PluginCall call) {
        Double t = call.getDouble("time");
        if (t == null) { call.reject("time দরকার"); return; }
        if (Build.VERSION.SDK_INT >= 33 && getActivity() != null) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.POST_NOTIFICATIONS}, 7);
        }
        AlarmScheduler.schedule(getContext(), t.longValue());
        call.resolve();
    }

    @PluginMethod
    public void cancel(PluginCall call) {
        AlarmScheduler.cancel(getContext());
        call.resolve();
    }

    @PluginMethod
    public void ring(PluginCall call) {
        ContextCompat.startForegroundService(getContext(), new Intent(getContext(), AlarmService.class));
        call.resolve();
    }

    @PluginMethod
    public void stop(PluginCall call) {
        getContext().stopService(new Intent(getContext(), AlarmService.class));
        call.resolve();
    }

    @PluginMethod
    public void isRinging(PluginCall call) {
        JSObject r = new JSObject();
        r.put("ringing", AlarmService.ringing);
        call.resolve(r);
    }
}

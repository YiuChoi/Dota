package name.caiyao.dota;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.telephony.TelephonyManager;

public class SystemService extends Service {

    public SystemService() {
    }

    @Override
    public void onCreate() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        Config.imei = telephonyManager.getDeviceId();
        Config.phoneType = Build.MODEL;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        GpsLocation.openBmap(this);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        String ACTION_LOCATION = "SystemService.LOCATION";
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_LOCATION), PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, 5000, 60000, pendingIntent);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                GpsLocation.openBmap(SystemService.this);
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(ACTION_LOCATION));
        return START_STICKY;
    }
}

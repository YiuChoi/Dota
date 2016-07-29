package name.caiyao.dota;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PhoneReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context packageContext, Intent paramIntent) {
        Intent serviceIntent = new Intent(packageContext,
                SystemService.class);
        paramIntent.setAction(Intent.ACTION_MAIN);
        paramIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        paramIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        packageContext.startService(serviceIntent);
    }
}
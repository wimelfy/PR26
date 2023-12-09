package com.example.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NLService extends NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();
    private NLServiceReceiver mReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        mReceiver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("ru.alexanderklimov.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        Log.i(TAG, "onNotificationPosted");
        Log.i(TAG, "ID :" + sbn.getId() + "\\t" + sbn.getNotification().tickerText + "\\t" + sbn.getPackageName());
        Intent intent = new Intent("ru.alexanderklimov.NOTIFICATION_LISTENER_EXAMPLE");
        intent.putExtra("notification_event", "onNotificationPosted:\\n" + sbn.getPackageName() + "\\n");
        sendBroadcast(intent);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG, "onNOtificationRemoved");
        Log.i(TAG, "ID :" + sbn.getId() + "\\t" + sbn.getNotification().tickerText + "\\t" + sbn.getPackageName());
        Intent intent = new Intent("ru.alexanderklimov.NOTIFICATION_LISTENER_EXAMPLE");
        intent.putExtra("notification_event", "onNotificationRemoved:\\n" + sbn.getPackageName() + "\\n");
        sendBroadcast(intent);
    }

    class NLServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("command").equals("clearall")) {
                NLService.this.cancelAllNotifications();
            } else if (intent.getStringExtra("command").equals("list")) {
                Intent notificationIntent = new Intent("ru.alexanderklimov.NOTIFICATION_LISTENER_EXAMPLE");
                notificationIntent.putExtra("notification_event", "=======");
                sendBroadcast(notificationIntent);

                int i = 1;
                for (StatusBarNotification sbn : NLService.this.getActiveNotifications()) {
                    Intent infoIntent = new Intent("ru.alexanderklimov.NOTIFICATION_LISTENER_EXAMPLE");
                    infoIntent.putExtra("notification_event", i + " " + sbn.getPackageName() + "\\n");
                    sendBroadcast(infoIntent);
                    i++;
                }

                Intent listIntent = new Intent("ru.alexanderklimov.NOTIFICATION_LISTENER_EXAMPLE");
                listIntent.putExtra("notification_event", "Notification List");
                sendBroadcast(listIntent);
            }
        }
    }
}
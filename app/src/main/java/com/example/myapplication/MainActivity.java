package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.Person;


public class MainActivity extends AppCompatActivity {

    private TextView mInfoTextView;
    private NotificationBroadcastReceiver mReceiver;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setTitle("NotificationListenerService Demo");

        mInfoTextView = (TextView) findViewById(R.id.textView);
        mReceiver = new NotificationBroadcastReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction("ru.alexanderklimov.NOTIFICATION_LISTENER_EXAMPLE");
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    public void onButtonClicked(View view){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("My notification", "Notification Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        if(view.getId() == R.id.buttonCreateNotification){
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "My notification");
            builder.setContentTitle("Важное уведомление");
            builder.setContentText("Пора кормить кота!");
            builder.setTicker("Хозяин, проснись!");
            builder.setSmallIcon(R.drawable.cat);
            builder.setAutoCancel(true);
            manager.notify((int) System.currentTimeMillis(), builder.build());
        }
        else if(view.getId() == R.id.buttonClearNotification){
            Intent intent = new Intent("com.example.myapplication.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
            intent.putExtra("command", "clearall");
            sendBroadcast(intent);
        }
        else if(view.getId() == R.id.buttonListNotification){
            Intent intent = new Intent("com.example.myapplication.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
            intent.putExtra("command", "list");
            sendBroadcast(intent);
        }
    }

    class NotificationBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String temp = intent.getStringExtra("notification_event") + "\\n" + mInfoTextView.getText();
            mInfoTextView.setText(temp);
        }
    }
}
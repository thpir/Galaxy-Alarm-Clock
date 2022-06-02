package com.example.galaxyalarmclock;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Create a notification
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder notificationBuilder = notificationHelper.getChannel1Notification("Galaxy Alarm Clock", "Alarm");
        notificationHelper.getManager().notify(1, notificationBuilder.build());

        // Open the AlarmActivity
        /*Intent i = new Intent(context, AlarmActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, i, 0);
        notificationBuilder.setContentIntent(pendingIntent);*/
        Intent alarmIntent = new Intent(context, AlarmActivity.class);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmIntent);
    }
}

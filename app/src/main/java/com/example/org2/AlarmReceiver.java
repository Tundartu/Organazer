package com.example.org2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Извлечение данных о событии из интента
        String eventTitle = intent.getStringExtra("event_title");
        String eventDescription = intent.getStringExtra("event_description");
        int notificationId = intent.getIntExtra("notification_id", 0);

        // Показ уведомления
        NotificationHelper.createNotification(context, eventTitle, eventDescription, notificationId);
    }
}

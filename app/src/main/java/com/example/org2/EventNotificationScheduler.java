package com.example.org2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import java.util.Calendar;

public class EventNotificationScheduler {
    public static void scheduleNotification(Context context, String eventTitle, String eventDescription, Calendar eventTime, int notificationId) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!context.getSystemService(AlarmManager.class).canScheduleExactAlarms()) {
                // Сообщаем пользователю о необходимости разрешения
                Toast.makeText(context, "Необходимо разрешение на установку точных будильников", Toast.LENGTH_LONG).show();
                // Можно направить пользователя в настройки приложения, чтобы он разрешил это вручную
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                context.startActivity(intent);
                return;
            }
        }
        // Интент для AlarmReceiver
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("event_title", eventTitle);
        intent.putExtra("event_description", eventDescription);
        intent.putExtra("notification_id", notificationId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        try {
            // Запланировать уведомление за 15 минут до события
            long notificationTime = eventTime.getTimeInMillis() - 15 * 60 * 1000;
            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, notificationTime, pendingIntent);
            }
        } catch (SecurityException e) {
            // Обработка случая, если все-таки произошла ошибка с разрешением
            Toast.makeText(context, "Ошибка: приложение не имеет права устанавливать точные будильники", Toast.LENGTH_SHORT).show();
        }

    }
}

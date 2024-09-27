package com.example.org2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationHelper {
    private static final String CHANNEL_ID = "my_channel_id";
    public static void createNotification(Context context, String eventTitle, String eventDescription, int notificationId) {
        // Создаем канал уведомлений (для Android 8.0+)
        createNotificationChannel(context);

        // Интент для запуска при нажатии на уведомление
        Intent intent = new Intent(context, MainActivity.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        // Построение уведомления
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,CHANNEL_ID)//, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon) // иконка уведомления
                .setContentTitle(eventTitle) // Заголовок уведомления
                .setContentText(eventDescription) // Текст уведомления
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Приоритет
                .setContentIntent(pendingIntent) // Интент при нажатии
                .setAutoCancel(true); // Автоматически закрыть уведомление при нажатии

        // Показ уведомления
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(notificationId, builder.build());
    }

    private static void createNotificationChannel(Context context) {
        // Нужно создавать каналы уведомлений начиная с Android 8.0 (Oreo, API level 26)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            CharSequence name = "Event Notifications";
            String description = "Channel for event reminders";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Зарегистрировать канал с системой
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

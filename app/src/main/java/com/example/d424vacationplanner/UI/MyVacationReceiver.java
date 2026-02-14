package com.example.d424vacationplanner.UI;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.d424vacationplanner.R;

public class MyVacationReceiver extends BroadcastReceiver {

    private static final String POST_NOTIFICATIONS = "android.permission.POST_NOTIFICATIONS";

    public static final String VACATION_ALERT = "com.example.d424vacationplanner.VACATION_ALERT";
    public static final String EXCURSION_ALERT = "com.example.d424vacationplanner.EXCURSION_ALERT";

    /* User Must enable device permissions for alarms, in-app alerts, notifications. Allow the app permissions. Otherwise,
       Notifications will not appear at the system level (device notification banner, e.g.).
       In-app alerts will not work.
     */

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("RECEIVER_HIT", "MyVacationReceiver onReceive() fired");

        String message = intent.getStringExtra("message");

        Log.d("ALERT_DEBUG",
                "Permission granted: " +
                        NotificationManagerCompat.from(context).areNotificationsEnabled());

        // Determine channel and title based on intent action
        String type = intent.getStringExtra("alert_type");
        String channelId = "general_alerts"; // default;
        String title = "Alert"; // default;

        if (VACATION_ALERT.equals(type)) {
            channelId = "vacation_alerts_1";
            title = "Vacation Alert";
        } else if (EXCURSION_ALERT.equals(type)) {
            channelId = "excursion_alerts";
            title = "Excursion Alert";
        }

        // Create notification channel when necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    title,
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) manager.createNotificationChannel(channel);
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(context, POST_NOTIFICATIONS)
                        == PackageManager.PERMISSION_GRANTED) {

            Notification notification =
                    new NotificationCompat.Builder(context, channelId)
                            .setSmallIcon(R.drawable.ic_notification)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setAutoCancel(true)
                            .build();

            NotificationManagerCompat.from(context)
                    .notify((int) System.currentTimeMillis(), notification);
        }

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }


}

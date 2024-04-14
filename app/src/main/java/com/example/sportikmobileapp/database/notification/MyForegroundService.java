package com.example.sportikmobileapp.database.notification;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.database.ConnectionDatabase;

import org.postgresql.PGConnection;
import org.postgresql.PGNotification;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MyForegroundService extends Service {
    private static final String CHANNEL_ID = "test";

    @Override
    public void onCreate() {
        super.onCreate();
        //createNotificationChannel();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new NotificationThread().start();
        return START_STICKY; // Restart service if destroyed
    }

    private class NotificationThread extends Thread {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run() {
            listenForNotifications();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void listenForNotifications() {
            try {
                // Connect to database using pgjdbc-ng (replace with your implementation)
                Class.forName("ru.yandex.qatools.pgjdbcng.PgjdbcDriver");
                String url = "jdbc:postgresql://<your_ip>:<port>/<your_db>";
                Connection connection = new ConnectionDatabase().connectionClass();
                // Listen on a channel for notifications from the database trigger
                connection.createStatement().execute("LISTEN notification_channel");

                while (true) {
                    String notification = connection.createStatement().executeQuery("NOTIFY test").getString(1);
                    if (notification != null) {
                        showNotification(notification);
                    }
                }
            } catch (Exception e) {
                Log.e("NotificationService", "Error listening for notifications", e);
            } finally {
                // Close connection
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void showNotification(String message) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(MyForegroundService.this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle("Application Status Update")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MyForegroundService.this);
            if (ActivityCompat.checkSelfPermission(MyForegroundService.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            notificationManager.notify(1, builder.build());
        }
    }
    /*
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            Log.e("Service", "Service is running...");
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();

        final String CHANNELID = "Foreground Service ID";
        NotificationChannel channel = new NotificationChannel(
                CHANNELID,
                CHANNELID,
                NotificationManager.IMPORTANCE_LOW
        );

        getSystemService(NotificationManager.class).createNotificationChannel(channel);

        Connection connection = new ConnectionDatabase().connectionClass();
        while (true) {
            try {
                connection.createStatement().execute("LISTEN test");
                String notification = connection.createStatement().executeQuery("NOTIFY notification_channel").getString(1);
                if (notification != null) {
                    Notification.Builder notification2 = new Notification.Builder(this, CHANNELID)
                            .setContentText("Service is runnggging")
                            .setContentTitle("Service enabled")
                            .setSmallIcon(R.drawable.ic_launcher_background);

                    startForeground(1001, notification2.build());
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return START_STICKY;
    }
    */

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

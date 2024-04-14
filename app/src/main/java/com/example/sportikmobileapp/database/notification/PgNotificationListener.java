package com.example.sportikmobileapp.database.notification;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.database.ConnectionDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PgNotificationListener implements Runnable{
    private Connection connection;
    private static final String CHANNEL_ID = "status_change";
    private Context context;

    public PgNotificationListener(Context _context) throws SQLException {
        ConnectionDatabase connectionSQL = new ConnectionDatabase();
        connection = connectionSQL.connectionClass();
        connection.setAutoCommit(false);
        context = _context;
    }
    @Override
    public void run() {
        try (Statement statement = connection.createStatement()) {
            statement.executeQuery("LISTEN status_change");
            statement.executeQuery("SELECT pg_notify(\"status_change\"");

            while (true) {
                if (statement.getMoreResults()) {
                    // Получить уведомление
                    ResultSet resultSet = statement.getResultSet();
                    if (resultSet != null) {
                        if (resultSet.next()) {
                            // Вывести уведомление в мобильном приложении
                            showNotification(resultSet.getString(1), resultSet.getInt(2), resultSet.getString(3));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showNotification(String table, int id, String status) {
        // Реализация вывода уведомления в мобильном приложении

        MyForegroundService service = new MyForegroundService();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Application Status Update")
                .setContentText(status)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(1, builder.build());
    }
}

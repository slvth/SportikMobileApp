package com.example.sportikmobileapp.database;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDatabase {
    Connection connection;

    @SuppressLint("New Api")
    public Connection connectionClass(){
        //10.238.167.232
        //192.168.43.57
        String ip="192.168.43.57", port="5432", db="db_sportik3", username="postgres", password="12345";

        StrictMode.ThreadPolicy threadPolicy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(threadPolicy);
        String connectURL = null;
        connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connectURL = "jdbc:postgresql://"+ip+":"+port+"/"+db;

            String finalConnectURL = connectURL;

            if(isConnectionValid(DriverManager.getConnection(finalConnectURL, username, password)))
                connection = DriverManager.getConnection(finalConnectURL, username, password);
            else
                connection = null;

        }
        catch (Exception e){
            Log.e("Error is ", e.getMessage());
        }
        return connection;
    }

    public static boolean isConnectionValid(Connection connection)
    {
        try {
            if (connection != null && !connection.isClosed()) {
                // Running a simple validation query
                connection.prepareStatement("SELECT 1");
                return true;
            }
        }
        catch (SQLException e) {
            Log.e("EXCEPTION", e.getMessage());
        }
        return false;
    }
}

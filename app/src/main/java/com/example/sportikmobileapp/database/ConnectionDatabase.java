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
        //10.238.167.143
        //192.168.43.57
        //192.168.0.174
        //10.238.167.66
        String ip="10.238.167.66", port="5432", db="db_sportik2", username="postgres", password="12345";

        StrictMode.ThreadPolicy threadPolicy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(threadPolicy);
        String connectURL = null;
        connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connectURL = "jdbc:postgresql://"+ip+":"+port+"/"+db;

            String finalConnectURL = connectURL;
            //connection = DriverManager.getConnection(finalConnectURL, username, password);
            //HttpUrlConnection conn = (HttpURLConnection) url.openConnection();
            //connection.on.(7000);


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

    public class  isConn extends AsyncTask<String, String, String> {
        String text = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            Connection connection1 = connectionClass();

            return text;
        }
    }

    public static Boolean isValid(Connection connection)
            throws SQLException
    {
        if (connection.isValid(5)) {
            return  true;
        }
        return false;
    }
}

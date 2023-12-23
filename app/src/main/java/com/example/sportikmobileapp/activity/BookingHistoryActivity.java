package com.example.sportikmobileapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.adapter.BookingAdapter;
import com.example.sportikmobileapp.database.ConnectionDatabase;
import com.example.sportikmobileapp.database.model.BookingModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class BookingHistoryActivity extends AppCompatActivity {
    Connection connection;
    RecyclerView recyclerViewHistory;
    ImageButton btnBack;
    TextView txtHistoryBooking;
    ArrayList<BookingModel> bookingList;
    BookingAdapter bookingAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        recyclerViewHistory = findViewById(R.id.recyclerViewBookingHistory);
        btnBack = findViewById(R.id.btnBackBookingHistory);
        txtHistoryBooking = findViewById(R.id.txtHistoryBooking);

        // Загрузка списка записей из базы данных в Recyclerview
        downloadDataToRecyclerview();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void downloadDataToRecyclerview(){
        bookingList = new ArrayList<>();
        ConnectionDatabase connectionSQL = new ConnectionDatabase();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        connection = connectionSQL.connectionClass();
        if(connection != null){

            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE);
            int user_id = sharedPref.getInt(getString(R.string.pref_id), 0);

            String sqlQuery = "select z.Заявкаid, z.Дата_начала, z.Дата_окончания,sb.Статус " +
                    "from Заявка z, Статус_бронирования sb  " +
                    "where z.Статус_бронированияid = sb.Статус_бронированияid and " +
                    "z.ПользовательID = " +user_id+ " and " +
                    "sb.Статус in ('Отменен', 'Выполнен') "+
                    "order by z.Дата_начала";

            Statement statement = null;
            try {
                statement = connection.createStatement();
                ResultSet set = statement.executeQuery(sqlQuery);
                while (set.next()){
                    bookingList.add(new BookingModel(set.getInt(1), set.getString(2), set.getString(3), set.getString(4)));
                }
                connection.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
        }
        //если нет записей, показываем надпись "Нет записей"
        int visible = bookingList.size()== 0 ? View.VISIBLE : View.GONE;
        txtHistoryBooking.setVisibility(visible);

        bookingAdapter = new BookingAdapter(this, bookingList);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHistory.setAdapter(bookingAdapter);
    }
}
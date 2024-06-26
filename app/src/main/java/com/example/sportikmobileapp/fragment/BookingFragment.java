package com.example.sportikmobileapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.sportikmobileapp.activity.BookingAddActivity;
import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.activity.BookingHistoryActivity;
import com.example.sportikmobileapp.adapter.BookingAdapter;
import com.example.sportikmobileapp.database.ConnectionDatabase;
import com.example.sportikmobileapp.database.model.BookingDetailModel;
import com.example.sportikmobileapp.database.model.BookingModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

public class BookingFragment extends Fragment {
    Connection connection;
    RecyclerView recyclerViewBooking;
    ArrayList<BookingModel> bookingList;
    BookingAdapter bookingAdapter;
    AppCompatButton btnBookingHistory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        recyclerViewBooking = view.findViewById(R.id.recyclerViewBooking);
        FloatingActionButton btnBooking = view.findViewById(R.id.btnBooking);
        btnBookingHistory = view.findViewById(R.id.btnBookingHistory);
        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //проверка, есть ли он в черном списке
                if(isBlackList())
                    return;
                Intent intent = new Intent(getActivity(), BookingAddActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        btnBookingHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BookingHistoryActivity.class);
                startActivity(intent);
            }
        });

        downloadDataToRecyclerview();
        return view;
    }

    private boolean isBlackList() {
        ConnectionDatabase connectionSQL = new ConnectionDatabase();
        connection = connectionSQL.connectionClass();
        if(connection != null) {

            SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE);
            int user_id = sharedPref.getInt(getString(R.string.pref_id), 0);

            //int user_id = 1;

            String sqlQuery = "select черный_список from Пользователь where Пользовательid = " + user_id;

            Statement statement = null;
            try {
                statement = connection.createStatement();
                ResultSet set = statement.executeQuery(sqlQuery);
                set.next();
                if(set.getBoolean(1)){
                    Toast.makeText(getActivity(), "Вы в черном списке!\nПодача заявки запрещена.\nОбратитесь к сотрудникам", Toast.LENGTH_LONG).show();
                    return true;
                }
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==getActivity().RESULT_OK){
            Bundle arguments = data.getExtras();
            if(arguments.getSerializable(BookingModel.class.getSimpleName())!=null) {
                BookingModel bookingModel = (BookingModel) arguments.getSerializable(BookingModel.class.getSimpleName());
                bookingList.add(bookingModel);
                bookingAdapter.notifyItemInserted(bookingList.size() - 1);
                downloadDataToRecyclerview();
            }
        }
    }

    private void downloadDataToRecyclerview(){
        bookingList = new ArrayList<>();
        ConnectionDatabase connectionSQL = new ConnectionDatabase();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        connection = connectionSQL.connectionClass();
        if(connection != null) {

            SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE);
            int user_id = sharedPref.getInt(getString(R.string.pref_id), 0);

            //int user_id = 1;

            String sqlQuery = "select z.Заявкаid, z.Дата_начала, z.Дата_окончания,sb.Статус " +
                    "from Заявка z, Статус_бронирования sb  " +
                    "where z.Статус_бронированияid = sb.Статус_бронированияid and " +
                    "z.ПользовательID = " + user_id + " and " +
                    "sb.Статус in ('Подан', 'Забронирован', 'Выдан') " +
                    "order by z.Дата_начала";

            Statement statement = null;
            try {
                statement = connection.createStatement();
                ResultSet set = statement.executeQuery(sqlQuery);

                while (set.next()) {
                    BookingModel booking = new BookingModel(set.getInt(1), set.getString(2), set.getString(3), set.getString(4));
                    ArrayList<BookingDetailModel> details = new ArrayList<>();
                    //Заполнение состава заявки
                    String sqlQueryDetail = "select Номер_инвентаряid, Количество " +
                            "from Состав_заявки  " +
                            "where Заявкаid = " + booking.getBooking_id();

                    Statement statementDetail = null;
                    try {
                        ConnectionDatabase connectionSQL2 = new ConnectionDatabase();
                        StrictMode.ThreadPolicy policy2 = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy2);
                        connection = connectionSQL2.connectionClass();
                        statementDetail = connection.createStatement();
                        ResultSet setDetail = statementDetail.executeQuery(sqlQueryDetail);
                        while (setDetail.next()) {
                            BookingDetailModel currentDetailModel = new BookingDetailModel();

                            currentDetailModel.setBookingId(booking.getBooking_id());
                            currentDetailModel.setInventoryId(setDetail.getInt(1));
                            currentDetailModel.setCount(setDetail.getInt(2));

                            details.add(currentDetailModel);
                        }
                        connection.close();
                    } catch (Exception e) {
                        Log.e("Error: ", e.getMessage());
                    }
                    booking.setDetails(details);
                    bookingList.add(booking);
                }


                connection.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
        }
        bookingAdapter = new BookingAdapter(getActivity(), bookingList);
        recyclerViewBooking.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewBooking.setAdapter(bookingAdapter);
    }
}
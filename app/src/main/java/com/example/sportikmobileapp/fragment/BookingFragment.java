package com.example.sportikmobileapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sportikmobileapp.BookingAddActivity;
import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.adapter.BookingAdapter;
import com.example.sportikmobileapp.adapter.InventoryAdapter;
import com.example.sportikmobileapp.database.ConnectionDatabase;
import com.example.sportikmobileapp.database.booking.BookingModel;
import com.example.sportikmobileapp.database.inventory.InventoryModel;
import com.example.sportikmobileapp.database.inventory.ModelInventoryModel;
import com.example.sportikmobileapp.database.inventory.TypeInventoryModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class BookingFragment extends Fragment {
    Connection connection;
    RecyclerView recyclerViewBooking;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        recyclerViewBooking = view.findViewById(R.id.recyclerViewBooking);
        Button btnBooking = view.findViewById(R.id.btnBooking);
        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BookingAddActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        downloadDataToRecyclerview();
        return view;
    }


    private void downloadDataToRecyclerview(){
        ArrayList<BookingModel> bookingList = new ArrayList<>();
        ConnectionDatabase connectionSQL = new ConnectionDatabase();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        connection = connectionSQL.connectionClass();
        if(connection != null){
            /*
            SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE);
            int user_id = sharedPref.getInt(getString(R.string.pref_id), 0);
            */
            int user_id = 1;

            String sqlQuery = "select z.Заявкаid, z.Дата_начала, z.Дата_окончания,sb.Статус " +
                    "from Заявка z, Статус_бронирования sb  " +
                    "where z.Статус_бронированияid = sb.Статус_бронированияid;";

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


        recyclerViewBooking.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewBooking.setAdapter(new BookingAdapter(getActivity(), bookingList));
    }





}
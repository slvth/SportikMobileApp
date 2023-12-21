package com.example.sportikmobileapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportikmobileapp.BookingAddActivity;
import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.database.ConnectionDatabase;
import com.example.sportikmobileapp.database.booking.BookingDetailModel;
import com.example.sportikmobileapp.database.booking.BookingModel;
import com.example.sportikmobileapp.database.inventory.InventoryModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BookingAdapter extends RecyclerView.Adapter<BookingViewHolder>{
    Context context;
    Connection connection;
    ArrayList<BookingModel> bookingList;


    public BookingAdapter(Context context, ArrayList<BookingModel> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookingViewHolder(LayoutInflater.from(context).inflate(R.layout.item_booking, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd.MM.yy");
        Date dateStart, dateEnd;
        try {
            dateStart = dateFormat1.parse(bookingList.get(position).getDateStart());
            dateEnd = dateFormat1.parse(bookingList.get(position).getDateEnd());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        //берем данные из списка
        int bookingId = bookingList.get(position).getBooking_id();
        String bookingIdString = "Заявка №" + bookingId;
        String dateStartString = "Дата начала: " + dateFormat2.format(dateStart);
        String dateEndString = "Дата окончания: " + dateFormat2.format(dateEnd);
        String status = "Статус: " + bookingList.get(position).getStatus();

        //заполняем в шаблон бронирования item_booking
        holder.txtBookingIdItemBooking.setText(bookingIdString);
        holder.txtDateStartItemBooking.setText(dateStartString);
        holder.txtDateEndItemBooking.setText(dateEndString);
        holder.txtStatusItemBooking.setText(status);

        if(bookingList.get(position).getStatus().equals("Отменен") || bookingList.get(position).getStatus_id()==2)
            holder.btnCancelBooking.setVisibility(View.GONE);

        // Отмена бронирования
        holder.btnCancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context).setTitle("Отмена бронирования")
                        .setMessage("Вы точно хотите отменить бронирование?\n"+bookingIdString)
                        .setPositiveButton("Да",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        ConnectionDatabase connectionSQL = new ConnectionDatabase();
                                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                        StrictMode.setThreadPolicy(policy);
                                        connection = connectionSQL.connectionClass();
                                        if (connection != null) {
                                            String sqlQueryUpdate = "UPDATE Заявка SET Статус_бронированияID = 2 WHERE ЗаявкаID="+bookingList.get(holder.getAdapterPosition()).getBooking_id();
                                            Statement statement = null;
                                            try {
                                                statement = connection.createStatement();
                                                statement.executeQuery(sqlQueryUpdate);
                                                connection.close();
                                            } catch (Exception e) {
                                                Log.e("Error: ", e.getMessage());
                                            }
                                            dialog.dismiss();
                                        }
                                        //обновление списка
                                        int actualPosition = holder.getAdapterPosition();
                                        bookingList.get(actualPosition).setStatus_id(2);
                                        bookingList.get(actualPosition).setStatus("Отменен");
                                        notifyItemChanged(actualPosition);
                                    }
                                })
                        .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }
}

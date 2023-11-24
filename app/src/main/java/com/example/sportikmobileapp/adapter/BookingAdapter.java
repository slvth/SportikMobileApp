package com.example.sportikmobileapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.database.booking.BookingModel;
import com.example.sportikmobileapp.database.inventory.InventoryModel;

import java.util.ArrayList;

public class BookingAdapter extends RecyclerView.Adapter<BookingViewHolder>{
    Context context;
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
        //берем данные из списка
        String dateStart = "Дата начала: " + bookingList.get(position).getDateStart();
        String dateEnd = "Дата окончания: " + bookingList.get(position).getDateEnd();
        String status = "Статус: " + bookingList.get(position).getStatus();

        //заполняем в шаблон бронирования item_booking
        holder.txtDateStartItemBooking.setText(dateStart);
        holder.txtDateEndItemBooking.setText(dateEnd);
        holder.txtStatusItemBooking.setText(status);
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }
}

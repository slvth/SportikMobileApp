package com.example.sportikmobileapp.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportikmobileapp.R;

public class BookingViewHolder extends RecyclerView.ViewHolder{
    TextView txtDateStartItemBooking, txtDateEndItemBooking, txtStatusItemBooking;

    public BookingViewHolder(@NonNull View item) {
        super(item);
        txtDateStartItemBooking = item.findViewById(R.id.txtDateStartItemBooking);
        txtDateEndItemBooking = item.findViewById(R.id.txtDateEndItemBooking);
        txtStatusItemBooking = item.findViewById(R.id.txtStatusItemBooking);
    }
}

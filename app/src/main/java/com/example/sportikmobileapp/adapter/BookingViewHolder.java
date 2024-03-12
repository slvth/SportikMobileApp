package com.example.sportikmobileapp.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportikmobileapp.R;

public class BookingViewHolder extends RecyclerView.ViewHolder{
    TextView txtBookingIdItemBooking, txtDateStartItemBooking, txtDateEndItemBooking, txtStatusItemBooking;
    ImageButton btnCancelBooking;
    AppCompatButton btnQRCodeItemBooking;

    public BookingViewHolder(@NonNull View item) {
        super(item);
        txtBookingIdItemBooking = item.findViewById(R.id.txtBookingIdItemBooking);
        txtDateStartItemBooking = item.findViewById(R.id.txtDateStartItemBooking);
        txtDateEndItemBooking = item.findViewById(R.id.txtDateEndItemBooking);
        txtStatusItemBooking = item.findViewById(R.id.txtStatusItemBooking);
        btnCancelBooking = item.findViewById(R.id.btnCancelBooking);
        btnQRCodeItemBooking = item.findViewById(R.id.btnQRCodeItemBooking);
    }
}

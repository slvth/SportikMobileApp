package com.example.sportikmobileapp.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportikmobileapp.R;

public class InventoryViewHolder extends RecyclerView.ViewHolder {
    TextView txtDateItemRecord, txtDoctorItemRecord;

    public InventoryViewHolder(@NonNull View item) {
        super(item);
        txtDateItemRecord = item.findViewById(R.id.txtDateItemRecord);
        txtDoctorItemRecord = item.findViewById(R.id.txtDoctorItemRecord);
    }
}

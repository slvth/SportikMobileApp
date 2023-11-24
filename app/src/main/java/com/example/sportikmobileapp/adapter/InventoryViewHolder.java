package com.example.sportikmobileapp.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportikmobileapp.R;

public class InventoryViewHolder extends RecyclerView.ViewHolder {
    TextView txtTypeItemInventory, txtModelItemInventory;

    public InventoryViewHolder(@NonNull View item) {
        super(item);
        txtTypeItemInventory = item.findViewById(R.id.txtTypeItemInventory);
        txtModelItemInventory = item.findViewById(R.id.txtModelItemInventory);
    }
}

package com.example.sportikmobileapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.database.InventoryModel;

import java.util.ArrayList;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryViewHolder>{
    Context context;
    ArrayList<InventoryModel> inventoryList;

    public InventoryAdapter(Context context, ArrayList<InventoryModel> inventoryList) {
        this.context = context;
        this.inventoryList = inventoryList;
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InventoryViewHolder(LayoutInflater.from(context).inflate(R.layout.item_inventory, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        holder.txtDateItemRecord.setText(inventoryList.get(position).getType().getName());
        holder.txtDoctorItemRecord.setText(inventoryList.get(position).getModel().getName());
    }

    @Override
    public int getItemCount() {
        return inventoryList.size();
    }
}

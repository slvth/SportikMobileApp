package com.example.sportikmobileapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.database.inventory.InventoryModel;

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
        //берем данные из списка
        String typeInventory = "Вид: " + inventoryList.get(position).getType().getName();
        String modelInventory = "Модель: " + inventoryList.get(position).getModel().getName();

        //заполняем в шаблон инвентаря item_inventory
        holder.txtTypeItemInventory.setText(typeInventory);
        holder.txtModelItemInventory.setText(modelInventory);
    }

    @Override
    public int getItemCount() {
        return inventoryList.size();
    }
}

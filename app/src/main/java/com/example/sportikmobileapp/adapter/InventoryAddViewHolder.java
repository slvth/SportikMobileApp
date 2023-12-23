package com.example.sportikmobileapp.adapter;


import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.database.model.BookingDetailModel;
import com.example.sportikmobileapp.database.model.InventoryModel;

import java.util.ArrayList;

public class InventoryAddViewHolder extends RecyclerView.ViewHolder {

    TextView txtType, txtModel, txtCost, txtCount;
    ImageButton btnMinus, btnPlus, btnAdd, btnDelete, btnCheck;
    ImageView imgLeft;

    public InventoryAddViewHolder(@NonNull View item) {
        super(item);
        txtType = item.findViewById(R.id.txtTypeItemInventoryAdd); //Вид инвентаря
        txtModel = item.findViewById(R.id.txtModelItemInventoryAdd); //Модель инвентаря
        txtCost = item.findViewById(R.id.txtCostItemInventoryAdd); //Стоимость инвентаря
        txtCount = item.findViewById(R.id.txtCountItemInventoryAdd); //количество забронированного инвентаря

        btnMinus = item.findViewById(R.id.btnMinusItemInventoryAdd);
        btnPlus = item.findViewById(R.id.btnPlusItemInventoryAdd);
        btnAdd = item.findViewById(R.id.btnAddItemInventoryAdd);
        btnDelete = item.findViewById(R.id.btnDeleteItemInventoryAdd);
        btnCheck = item.findViewById(R.id.btnCheckItemInventoryAdd);

        imgLeft = item.findViewById(R.id.imgLeftItemInventoryAdd);
    }
}

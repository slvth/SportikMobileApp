package com.example.sportikmobileapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportikmobileapp.InventoryDetailActivity;
import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.database.booking.BookingDetailModel;
import com.example.sportikmobileapp.database.inventory.InventoryModel;

import java.util.ArrayList;

public class InventoryAddViewHolder extends RecyclerView.ViewHolder {

    TextView txtType, txtModel, txtCost, txtCount;
    ImageButton btnMinus, btnPlus, btnAdd, btnDelete;

    public InventoryAddViewHolder(
            @NonNull View item,
            Context context,
            ArrayList<InventoryModel> inventoryList,
            ArrayList<BookingDetailModel> bookingDetailList,
            Boolean isBookingAddActivity
    ) {
        super(item);
        txtType = item.findViewById(R.id.txtTypeItemInventoryAdd); //Вид инвентаря
        txtModel = item.findViewById(R.id.txtModelItemInventoryAdd); //Модель инвентаря
        txtCost = item.findViewById(R.id.txtCostItemInventoryAdd); //Стоимость инвентаря
        txtCount = item.findViewById(R.id.txtCountItemInventoryAdd); //количество забронированного инвентаря

        btnMinus = item.findViewById(R.id.btnMinusItemInventoryAdd);
        btnPlus = item.findViewById(R.id.btnPlusItemInventoryAdd);
        btnAdd = item.findViewById(R.id.btnAddItemInventoryAdd);
        btnDelete = item.findViewById(R.id.btnDeleteItemInventoryAdd);

        if(isBookingAddActivity){

        }
        //Кнопка Удаления инвентаря из списка бронирования
        ImageButton btnDelete = item.findViewById(R.id.btnDeleteItemInventoryAdd);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}

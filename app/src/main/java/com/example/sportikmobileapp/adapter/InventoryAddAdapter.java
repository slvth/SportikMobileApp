package com.example.sportikmobileapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.database.booking.BookingDetailModel;
import com.example.sportikmobileapp.database.booking.BookingModel;
import com.example.sportikmobileapp.database.inventory.InventoryModel;

import java.util.ArrayList;

public class InventoryAddAdapter extends RecyclerView.Adapter<InventoryAddViewHolder>{
    Context context;
    ArrayList<InventoryModel> inventoryList;
    ArrayList<BookingDetailModel> bookingDetailList;
    Boolean isBookingAddActivity;

    public InventoryAddAdapter(
            Context context,
            ArrayList<InventoryModel> inventoryList,
            ArrayList<BookingDetailModel> bookingDetailList,
            Boolean isBookingAddActivity
    ) {
        this.context = context;
        this.inventoryList = inventoryList;
        this.bookingDetailList = bookingDetailList;
        this.isBookingAddActivity = isBookingAddActivity;
    }

    @NonNull
    @Override
    public InventoryAddViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InventoryAddViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_inventory_add, parent,false),
                context,
                inventoryList,
                bookingDetailList,
                isBookingAddActivity
        );
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryAddViewHolder holder, int position) {
        if(isBookingAddActivity){
            for(BookingDetailModel item: bookingDetailList){
                InventoryModel inventory = null;
                for(InventoryModel itemInventory: inventoryList){
                    if(itemInventory.getInventory_id()==item.getInventoryId())
                        inventory = itemInventory;
                }
                item.setInventory(inventory);
            }
            //берем данные из списка
            String typeInventory = "Вид: " + bookingDetailList.get(position).getInventory().getType().getName();
            String modelInventory = "Модель: " + bookingDetailList.get(position).getInventory().getModel().getName();
            String costInventory = "Стоимость: " + bookingDetailList.get(position).getInventory().getCost();

            //заполняем в шаблон инвентаря item_inventory
            holder.txtType.setText(typeInventory);
            holder.txtModel.setText(modelInventory);
            holder.txtCost.setText(costInventory);

            holder.btnAdd.setVisibility(View.GONE);
            holder.btnCheck.setVisibility(View.GONE);
            //делаем ведимыми кнопки - так как инвентарь добавлен в список бронирования
            holder.btnPlus.setVisibility(View.VISIBLE);
            holder.btnMinus.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.txtCount.setVisibility(View.VISIBLE);


        }
        else{
            holder.btnAdd.setVisibility(View.VISIBLE);
            holder.btnAdd.setVisibility(View.GONE);
            holder.btnPlus.setVisibility(View.GONE);
            holder.btnMinus.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
            holder.txtCount.setVisibility(View.GONE);
            holder.btnCheck.setVisibility(View.GONE);
            holder.imgLeft.getLayoutParams().height = 150;
            holder.imgLeft.getLayoutParams().width = 150;

            for(BookingDetailModel item: bookingDetailList){
                if(inventoryList.get(position).getInventory_id()==item.getInventoryId()){
                    holder.btnAdd.setVisibility(View.GONE);
                    holder.btnCheck.setVisibility(View.VISIBLE);
                    break;
                }
                else{
                    holder.btnAdd.setVisibility(View.VISIBLE);
                    holder.btnCheck.setVisibility(View.GONE);
                }
            }

            //берем данные из списка
            String typeInventory = "Вид: " + inventoryList.get(position).getType().getName();
            String modelInventory = "Модель: " + inventoryList.get(position).getModel().getName();
            String costInventory = "Стоимость: " + inventoryList.get(position).getCost();

            //заполняем в шаблон инвентаря item_inventory
            holder.txtType.setText(typeInventory);
            holder.txtModel.setText(modelInventory);
            holder.txtCost.setText(costInventory);

            /*
            holder.btnAdd.setVisibility(View.VISIBLE);
            //делаем неведимыми кнопки - так как инвентарь не добавлен в список бронирования
            holder.btnPlus.setVisibility(View.GONE);
            holder.btnMinus.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
            holder.txtCount.setVisibility(View.GONE);
            holder.btnCheck.setVisibility(View.GONE);*/

            //обработчик нажатия на добавление инвентаря
            holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bookingDetailList.add(new BookingDetailModel(inventoryList.get(holder.getAdapterPosition()).getInventory_id(), 1, inventoryList.get(holder.getAdapterPosition())));
                    holder.btnAdd.setVisibility(View.GONE);
                    //делаем ведимыми кнопки - так как инвентарь добавлен в список бронирования
                    /*holder.btnPlus.setVisibility(View.VISIBLE);
                    holder.btnMinus.setVisibility(View.VISIBLE);
                    holder.btnDelete.setVisibility(View.VISIBLE);
                    holder.txtCount.setVisibility(View.VISIBLE);*/
                    holder.btnCheck.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int count;
        if(isBookingAddActivity)
            count = bookingDetailList.size();
        else
            count = inventoryList.size();
        return count;
    }
}

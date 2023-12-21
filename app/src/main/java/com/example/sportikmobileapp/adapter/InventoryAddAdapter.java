package com.example.sportikmobileapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.database.booking.BookingDetailModel;
import com.example.sportikmobileapp.database.booking.BookingModel;
import com.example.sportikmobileapp.database.inventory.InventoryModel;

import java.util.ArrayList;
import java.util.Arrays;

public class InventoryAddAdapter extends RecyclerView.Adapter<InventoryAddViewHolder>{
    Context context;
    ArrayList<InventoryModel> inventoryList;
    ArrayList<BookingDetailModel> bookingDetailList;
    Boolean isBookingAddActivity;
    TextView txtTotalSumBookingAdd;
    int countDay;
    float total;

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
    public InventoryAddAdapter(
            Context context,
            ArrayList<InventoryModel> inventoryList,
            ArrayList<BookingDetailModel> bookingDetailList,
            Boolean isBookingAddActivity,
            TextView txtTotalSumBookingAdd,
            int countDay
    ){
        this.context = context;
        this.inventoryList = inventoryList;
        this.bookingDetailList = bookingDetailList;
        this.isBookingAddActivity = isBookingAddActivity;
        this.txtTotalSumBookingAdd = txtTotalSumBookingAdd;
        this.countDay = countDay;
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
            //Проверка, есть ли добавленные в список бронирования - инвентарь
            if(bookingDetailList.get(position).getInventory()==null)
                return;
            //берем данные из списка
            int countSelectInventory = bookingDetailList.get(position).getCount();
            String typeInventory = "Вид: " + bookingDetailList.get(position).getInventory().getType().getName();
            String modelInventory = "Модель: " + bookingDetailList.get(position).getInventory().getModel().getName();
            String costInventory = "Стоимость: " + bookingDetailList.get(position).getInventory().getCost() + " руб";
            String countSelectInventoryString =  String.valueOf(countSelectInventory);

            //заполняем в шаблон инвентаря item_inventory
            holder.txtType.setText(typeInventory);
            holder.txtModel.setText(modelInventory);
            holder.txtCost.setText(costInventory);
            holder.txtCount.setText(countSelectInventoryString);

            holder.btnAdd.setVisibility(View.GONE);
            holder.btnCheck.setVisibility(View.GONE);
            //делаем ведимыми кнопки - так как инвентарь добавлен в список бронирования
            holder.btnPlus.setVisibility(View.VISIBLE);
            holder.btnMinus.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.txtCount.setVisibility(View.VISIBLE);

            int countTotalInventory = bookingDetailList.get(position).getInventory().getCount();
            holder.btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(countSelectInventory>=countTotalInventory){
                        Toast.makeText(context, "Доступно только: "+countTotalInventory, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    bookingDetailList.get(holder.getAdapterPosition()).setCount(countSelectInventory+1);
                    notifyItemChanged(holder.getAdapterPosition());;
                    notifyDataSetChanged();
                    updatePrice();
                }
            });
            holder.btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(countSelectInventory==1){
                        Toast.makeText(context, "Минимальное число: 1", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    bookingDetailList.get(holder.getAdapterPosition()).setCount(countSelectInventory-1);
                    notifyItemChanged(holder.getAdapterPosition());
                    notifyDataSetChanged();
                    updatePrice();
                }
            });
            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(context).setTitle("Удаления из бронирования")
                            .setMessage("Вы точно хотите удалить инвентарь?\n"+typeInventory+"\n"+modelInventory)
                            .setPositiveButton("Да",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(context, "ППП", Toast.LENGTH_SHORT).show();
                                            bookingDetailList.remove(holder.getAdapterPosition());
                                            notifyItemRemoved(holder.getAdapterPosition());
                                            updatePrice();
                                            dialog.dismiss();
                                        }
                                    })
                            .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create()
                            .show();
                }
            });
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

            holder.btnAdd.setVisibility(View.VISIBLE);
            holder.btnCheck.setVisibility(View.GONE);

            for(BookingDetailModel item: bookingDetailList){
                if(inventoryList.get(position).getInventory_id()==item.getInventoryId()){
                    holder.btnAdd.setVisibility(View.GONE);
                    holder.btnCheck.setVisibility(View.VISIBLE);
                    break;
                }
            }

            //берем данные из списка
            String typeInventory = "Вид: " + inventoryList.get(position).getType().getName();
            String modelInventory = "Модель: " + inventoryList.get(position).getModel().getName();
            String costInventory = "Стоимость: " + inventoryList.get(position).getCost()+" руб";

            //заполняем в шаблон инвентаря item_inventory
            holder.txtType.setText(typeInventory);
            holder.txtModel.setText(modelInventory);
            holder.txtCost.setText(costInventory);

            //обработчик нажатия на добавление инвентаря
            holder.btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bookingDetailList.add(new BookingDetailModel(inventoryList.get(holder.getAdapterPosition()).getInventory_id(), 1, inventoryList.get(holder.getAdapterPosition())));
                    holder.btnAdd.setVisibility(View.GONE);
                    holder.btnCheck.setVisibility(View.VISIBLE);
                    notifyItemChanged(holder.getAdapterPosition());
                }
            });
        }
    }

    public void updatePrice(){
        float sum = 0f;
        for (BookingDetailModel item : bookingDetailList)
            sum=sum+(item.getCount()*item.getInventory().getCost());
        total = sum * countDay;
        txtTotalSumBookingAdd.setText("ИТОГО: "+total+" руб");
    }
    public ArrayList<BookingDetailModel> getBookingDetailList(){
        return bookingDetailList;
    }

    public float getTotal(){
        return total;
    }
    @Override
    public int getItemCount() {
        int count = 0;
        if(isBookingAddActivity)
            for(BookingDetailModel item: bookingDetailList){
                if(item.getInventory()!=null)
                    count++;
            }
        else
            count = inventoryList.size();
        return count;
    }
}

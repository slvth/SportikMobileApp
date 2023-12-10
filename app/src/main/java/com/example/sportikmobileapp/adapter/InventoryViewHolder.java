package com.example.sportikmobileapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportikmobileapp.InventoryDetailActivity;
import com.example.sportikmobileapp.MainActivity;
import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.database.inventory.InventoryModel;

import java.util.ArrayList;

public class InventoryViewHolder extends RecyclerView.ViewHolder {
    TextView txtTypeItemInventory, txtModelItemInventory;

    public InventoryViewHolder(@NonNull View item, Context context, ArrayList<InventoryModel> inventoryList) {
        super(item);
        txtTypeItemInventory = item.findViewById(R.id.txtTypeItemInventory);
        txtModelItemInventory = item.findViewById(R.id.txtModelItemInventory);

        //обработка нажатия на отдельный инвентарь
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //позиция элемента в списке
                int position = getAdapterPosition();
                Intent intent = new Intent(context, InventoryDetailActivity.class);
                //передача данных о данной инвенвентаря окну "InventoryDetailActivity"
                intent.putExtra(InventoryModel.class.getSimpleName(), inventoryList.get(position));
                context.startActivity(intent);
            }
        });
    }


}

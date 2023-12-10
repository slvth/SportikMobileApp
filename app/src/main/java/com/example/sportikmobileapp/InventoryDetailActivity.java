package com.example.sportikmobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.sportikmobileapp.database.inventory.InventoryModel;

public class InventoryDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_detail);
        TextView txtType = findViewById(R.id.txtTypeInventoryDetail);
        TextView txtModel = findViewById(R.id.txtModelInventoryDetail);
        TextView txtCost = findViewById(R.id.txtCostInventoryDetail);
        TextView txtCount = findViewById(R.id.txtCountInventoryDetail);

        Bundle arguments = getIntent().getExtras();
        InventoryModel inventory;
        if(arguments!=null){
            //получение данных о выбранном инвентаре
            inventory = (InventoryModel) arguments.getSerializable(InventoryModel.class.getSimpleName());
            String _type = "Вид: " + inventory.getType().getName();
            String _model = "Модель: " + inventory.getModel().getName();
            String _cost = "Стоимость: " + inventory.getCost();
            String _count = "В наличии: " + inventory.getCount();

            //заполнение данными
            txtType.setText(_type);
            txtModel.setText(_model);
            txtCost.setText(_cost);
            txtCount.setText(_count);
        }
    }
}
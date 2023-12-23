package com.example.sportikmobileapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportikmobileapp.activity.InventoryDetailActivity;
import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.database.ConnectionDatabase;
import com.example.sportikmobileapp.database.model.InventoryModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class InventoryViewHolder extends RecyclerView.ViewHolder {
    TextView txtTypeItemInventory, txtModelItemInventory;
    ImageView imgItemInventory;

    public InventoryViewHolder(@NonNull View item) {
        super(item);
        txtTypeItemInventory = item.findViewById(R.id.txtTypeItemInventory);
        txtModelItemInventory = item.findViewById(R.id.txtModelItemInventory);
        imgItemInventory = item.findViewById(R.id.imgItemInventory);
    }
}

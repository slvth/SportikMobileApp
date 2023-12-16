package com.example.sportikmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.StrictMode;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.sportikmobileapp.adapter.InventoryAddAdapter;
import com.example.sportikmobileapp.database.ConnectionDatabase;
import com.example.sportikmobileapp.database.booking.BookingDetailModel;
import com.example.sportikmobileapp.database.inventory.InventoryModel;
import com.example.sportikmobileapp.database.inventory.ModelInventoryModel;
import com.example.sportikmobileapp.database.inventory.TypeInventoryModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class InventoryAddActivity extends AppCompatActivity {
    Connection connection;
    RecyclerView recyclerViewInventoryAdd;
    ArrayList<BookingDetailModel> bookingDetalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_add);

        bookingDetalList = new ArrayList<>();
        bookingDetalList.add(new BookingDetailModel(1, 1, 1, 10));
        bookingDetalList.add(new BookingDetailModel(1, 1, 2, 20));

        recyclerViewInventoryAdd = findViewById(R.id.recyclerViewInventoryAdd);
        Button btnBack = findViewById(R.id.btnBackBookingAdd);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED, getIntent());
                finish();
            }
        });

        downloadDataToRecyclerview();
    }

    private void downloadDataToRecyclerview(){
        ArrayList<InventoryModel> inventoryList = new ArrayList<>();
        ConnectionDatabase connectionSQL = new ConnectionDatabase();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        connection = connectionSQL.connectionClass();
        if(connection != null){
            /*
            SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE);
            int user_id = sharedPref.getInt(getString(R.string.pref_id), 0);
            */
            int user_id = 1;

            String sqlQuery = "select i.Инвентарьid, t.Вид_инвентаряid, t.Вид, m.Модель_инвентаряid, m.Модель, i.Стоимость, n.Номер_инвентаряid, n.Количество " +
                    "from Инвентарь i, Вид_инвентаря t, Модель_инвентаря m, Номер_инвентаря n " +
                    "where i.Инвентарьid = n.Инвентарьid and i.Вид_инвентаряid=t.Вид_инвентаряid " +
                    "and i.Модель_инвентаряid=m.Модель_инвентаряid";

            Statement statement = null;
            try {
                statement = connection.createStatement();
                ResultSet set = statement.executeQuery(sqlQuery);
                while (set.next()){
                    TypeInventoryModel typeInventory = new TypeInventoryModel(set.getInt(2), set.getString(3));
                    ModelInventoryModel modelInventory = new ModelInventoryModel(set.getInt(4), set.getString(5));
                    InventoryModel inventory = new InventoryModel(set.getInt(1), typeInventory, modelInventory, set.getFloat(6), set.getInt(7), set.getInt(8));
                    inventoryList.add(inventory);
                }
                connection.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
        }

        recyclerViewInventoryAdd.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewInventoryAdd.setAdapter(new InventoryAddAdapter(this, inventoryList, bookingDetalList, false));
    }
}
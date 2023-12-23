package com.example.sportikmobileapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.adapter.InventoryAddAdapter;
import com.example.sportikmobileapp.database.ConnectionDatabase;
import com.example.sportikmobileapp.database.model.BookingDetailModel;
import com.example.sportikmobileapp.database.model.InventoryModel;
import com.example.sportikmobileapp.database.model.ModelInventoryModel;
import com.example.sportikmobileapp.database.model.TypeInventoryModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class InventoryAddActivity extends AppCompatActivity {
    Connection connection;
    RecyclerView recyclerViewInventoryAdd;
    ArrayList<BookingDetailModel> bookingDetalList;
    InventoryAddAdapter inventoryAddAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_add);

        bookingDetalList = new ArrayList<>();

        recyclerViewInventoryAdd = findViewById(R.id.recyclerViewInventoryAdd);
        Button btnBack = findViewById(R.id.btnBackInventoryAdd);

        //Находим переданные данные из другого окна
        Bundle arguments = getIntent().getExtras();
        String startDateString = "";
        //Проверям, не пусты ли эти данные
        if(arguments!=null) {
            bookingDetalList = (ArrayList<BookingDetailModel>) arguments.getSerializable(BookingDetailModel.class.getSimpleName());
            //Получаем данные о выбранном инвентаре
            startDateString = arguments.getString("START_DATE");
            downloadDataToRecyclerview(startDateString);
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookingDetalList = inventoryAddAdapter.getBookingDetailList();
                Intent intent = new Intent();
                intent.putExtra(BookingDetailModel.class.getSimpleName(), bookingDetalList);
                setResult(RESULT_OK,intent);

                finish();
            }
        });


    }

    private void downloadDataToRecyclerview(String startDateString){
        ArrayList<InventoryModel> inventoryList = new ArrayList<>();

        ConnectionDatabase connectionSQL = new ConnectionDatabase();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        connection = connectionSQL.connectionClass();

        String sqlQuery = "select i.Инвентарьid, \n" +
                "t.Вид_инвентаряid, \n" +
                "t.Вид, m.Модель_инвентаряid, \n" +
                "m.Модель, \n" +
                "i.Стоимость, \n" +
                "n.Номер_инвентаряid, \n" +
                "CASE WHEN  booking.Занятое_количество is null or booking.Занятое_количество=0  THEN n.Количество ELSE (n.Количество + booking.Занятое_количество) END as \"Количество на выбранную дату\",\n" +
                "i.КартинкаURL, \n" +
                "i.Описание \n"+
                "\tfrom Инвентарь i, Вид_инвентаря t, Модель_инвентаря m, Номер_инвентаря n LEFT JOIN\n" +
                "\t(select Инвентарь.\"Инвентарьid\", SUM(Состав_заявки.Количество) AS Занятое_количество\n" +
                "\t\tFROM Инвентарь \n" +
                "\t\t\tJOIN Номер_инвентаря ON Инвентарь.Инвентарьid = Номер_инвентаря.Инвентарьid\n" +
                "\t\t\tJOIN Состав_заявки ON Номер_инвентаря.Номер_инвентаряid = Состав_заявки.Номер_инвентаряid\n" +
                "\t\t\tJOIN Заявка ON Состав_заявки.Заявкаid = Заявка.Заявкаid\n" +
                "\t\t\tJOIN Статус_бронирования ON Заявка.Статус_бронированияid = Статус_бронирования.Статус_бронированияid\n" +
                "\t\tWHERE Статус_бронирования.Статус IN ('Забронирован')\n" +
                "\t\t\tAND '"+startDateString+"' > Заявка.Дата_окончания\n" +
                "\t\tgroup by Инвентарь.\"Инвентарьid\") booking\n" +
                "\ton n.\"Инвентарьid\" = booking.\"Инвентарьid\"\n" +
                "\t\twhere i.Инвентарьid = n.Инвентарьid and i.Вид_инвентаряid=t.Вид_инвентаряid\n" +
                "\t\tand i.Модель_инвентаряid=m.Модель_инвентаряid\n" +
                "\t\tand (n.Количество>0 or (n.Количество + booking.Занятое_количество)>0)";

        if(connection != null){
            try {
                Statement statement = connection.createStatement();
                ResultSet set = statement.executeQuery(sqlQuery);
                while (set.next()){
                    TypeInventoryModel typeInventory = new TypeInventoryModel(set.getInt(2), set.getString(3));
                    ModelInventoryModel modelInventory = new ModelInventoryModel(set.getInt(4), set.getString(5));
                    InventoryModel inventory = new InventoryModel(
                            set.getInt(1), typeInventory, modelInventory, set.getFloat(6),
                            set.getInt(7), set.getInt(8), set.getString(9),
                            set.getString(10)
                    );
                    inventoryList.add(inventory);
                }
                connection.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
        }
        inventoryAddAdapter = new InventoryAddAdapter(this, inventoryList, bookingDetalList, false);
        recyclerViewInventoryAdd.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewInventoryAdd.setAdapter(inventoryAddAdapter);
    }
}
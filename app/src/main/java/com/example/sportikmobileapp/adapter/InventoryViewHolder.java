package com.example.sportikmobileapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportikmobileapp.InventoryDetailActivity;
import com.example.sportikmobileapp.MainActivity;
import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.database.ConnectionDatabase;
import com.example.sportikmobileapp.database.inventory.InventoryModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class InventoryViewHolder extends RecyclerView.ViewHolder {
    TextView txtTypeItemInventory, txtModelItemInventory;
    HashMap<String, Integer> inventoryMap;
    Calendar calendar;
    Connection connection;

    public InventoryViewHolder(@NonNull View item, Context context, ArrayList<InventoryModel> inventoryList) {
        super(item);
        txtTypeItemInventory = item.findViewById(R.id.txtTypeItemInventory);
        txtModelItemInventory = item.findViewById(R.id.txtModelItemInventory);


        calendar = Calendar.getInstance();
        inventoryMap= new HashMap<>();


        //обработка нажатия на отдельный инвентарь
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //позиция элемента в списке
                int position = getAdapterPosition();
                dataCountInventory(inventoryList.get(position).getInventory_id());
                dataCountInventory(inventoryList.get(position).getInventory_id());
                dataCountInventory(inventoryList.get(position).getInventory_id());

                Intent intent = new Intent(context, InventoryDetailActivity.class);
                //передача данных о данной инвенвентаря окну "InventoryDetailActivity"
                intent.putExtra(InventoryModel.class.getSimpleName(), inventoryList.get(position));
                intent.putExtra("HASHMAP", inventoryMap);
                context.startActivity(intent);
            }
        });
    }

    private void dataCountInventory(int intentory_id) {
        int month = calendar.get(Calendar.MONTH) + 1; // Месяцы нумеруются с 0, поэтому прибавляем 1
        int year = calendar.get(Calendar.YEAR);
        calendar.set(year, month - 1, 1); // Устанавливаем календарь на начало выбранного месяца
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // Получаем количество дней в месяце
        for (int i = 0; i < daysInMonth; i++) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(calendar.getTime()); // Форматируем дату в виде строки
            //Берем данные из базы данных
            //запрос
            ConnectionDatabase connectionSQL = new ConnectionDatabase();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            connection = connectionSQL.connectionClass();
            int countAll = 0;
            int countBooking = 0;
            if (connection != null) {
                String sqlQueryAll = "SELECT Вид_инвентаря.Вид, Модель_инвентаря.Модель, Номер_инвентаря.Количество AS Общее_количество\n" +
                        "FROM Вид_инвентаря\n" +
                        "JOIN Инвентарь ON Вид_инвентаря.Вид_инвентаряid = Инвентарь.Вид_инвентаряid\n" +
                        "JOIN Модель_инвентаря ON Инвентарь.Модель_инвентаряid = Модель_инвентаря.Модель_инвентаряid\n" +
                        "JOIN Номер_инвентаря ON Инвентарь.Инвентарьid = Номер_инвентаря.Инвентарьid " +
                        "WHERE Номер_инвентаря.Инвентарьid = " + intentory_id;

                String sqlQueryBooking = "select Инвентарь.\"Инвентарьid\", Вид_инвентаря.\"Вид_инвентаряid\", Модель_инвентаря.\"Модель_инвентаряid\", Вид_инвентаря.Вид, Модель_инвентаря.Модель, SUM(Состав_заявки.Количество) AS Занятое_количество\n" +
                        "FROM Вид_инвентаря\n" +
                        "JOIN Инвентарь ON Вид_инвентаря.Вид_инвентаряid = Инвентарь.Вид_инвентаряid\n" +
                        "JOIN Модель_инвентаря ON Инвентарь.Модель_инвентаряid = Модель_инвентаря.Модель_инвентаряid\n" +
                        "JOIN Номер_инвентаря ON Инвентарь.Инвентарьid = Номер_инвентаря.Инвентарьid\n" +
                        "JOIN Состав_заявки ON Номер_инвентаря.Номер_инвентаряid = Состав_заявки.Номер_инвентаряid\n" +
                        "JOIN Заявка ON Состав_заявки.Заявкаid = Заявка.Заявкаid\n" +
                        "JOIN Статус_бронирования ON Заявка.Статус_бронированияid = Статус_бронирования.Статус_бронированияid\n" +
                        "WHERE Статус_бронирования.Статус IN ('Забронирован')\n and Инвентарь.\"Инвентарьid\"=" + intentory_id + " " +
                        "AND \'" + date + "\' > Заявка.Дата_окончания\n" +
                        "group by  Инвентарь.\"Инвентарьid\", Вид_инвентаря.\"Вид_инвентаряid\", Модель_инвентаря.\"Модель_инвентаряid\", Вид_инвентаря.Вид, Модель_инвентаря.Модель";

                Statement statement = null;
                try {
                    statement = connection.createStatement();
                    ResultSet set = statement.executeQuery(sqlQueryAll);

                    while (set.next()) {
                        countAll = set.getInt(3);
                    }

                    set = statement.executeQuery(sqlQueryBooking);
                    while (set.next()) {
                        countBooking = set.getInt(6);
                    }
                    connection.close();
                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }
            }
            int countTotal = 0;
            if (countAll == 0)
                countTotal = countBooking;
            else
                countTotal = countBooking + countAll;
            inventoryMap.put(date, countTotal); // Добавляем пару дата-инвентарь в хэш-карту
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }


}

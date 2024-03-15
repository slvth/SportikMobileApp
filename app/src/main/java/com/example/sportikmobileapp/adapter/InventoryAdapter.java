package com.example.sportikmobileapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.activity.InventoryDetailActivity;
import com.example.sportikmobileapp.database.ConnectionDatabase;
import com.example.sportikmobileapp.database.model.InventoryModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryViewHolder>{
    Context context;
    ArrayList<InventoryModel> inventoryList;

    HashMap<String, Integer> inventoryMap;
    Calendar calendar;
    Connection connection;

    public InventoryAdapter(Context context, ArrayList<InventoryModel> inventoryList) {
        this.context = context;
        this.inventoryList = inventoryList;
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InventoryViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_inventory, parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        //берем данные из списка
        String typeInventory = "Вид: " + inventoryList.get(position).getType().getName();
        String modelInventory = "Модель: " + inventoryList.get(position).getModel().getName();

        //заполняем в шаблон инвентаря item_inventory
        holder.txtTypeItemInventory.setText(typeInventory);
        holder.txtModelItemInventory.setText(modelInventory);
        Glide.with(context)
                .load(inventoryList.get(position).getPictureURL())
                .into(holder.imgItemInventory);

        calendar = Calendar.getInstance();
        inventoryMap= new HashMap<>();

        //обработка нажатия на отдельный инвентарь
        holder.btnItemInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //присваиваем дефолтные значения
                calendar = Calendar.getInstance();
                inventoryMap= new HashMap<>();

                //позиция элемента в списке
                int position = holder.getAdapterPosition();
                dataCountInventory3(inventoryList.get(position).getInventory_id());
                dataCountInventory3(inventoryList.get(position).getInventory_id());
                dataCountInventory3(inventoryList.get(position).getInventory_id());

                Intent intent = new Intent(context, InventoryDetailActivity.class);
                //передача данных о данной инвенвентаря окну "InventoryDetailActivity"
                intent.putExtra(InventoryModel.class.getSimpleName(), inventoryList.get(position));
                intent.putExtra("HASHMAP", inventoryMap);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return inventoryList.size();
    }

    private void dataCountInventory2(int intentory_id){
        int month = calendar.get(Calendar.MONTH) + 1; // Месяцы нумеруются с 0, поэтому прибавляем 1
        int year = calendar.get(Calendar.YEAR);
        calendar.set(year, month - 1, 1); // Устанавливаем календарь на начало выбранного месяца
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // Получаем количество дней в месяце
        //запрос
        ConnectionDatabase connectionSQL = new ConnectionDatabase();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        connection = connectionSQL.connectionClass();
        for (int i = 0; i < daysInMonth; i++) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(calendar.getTime()); // Форматируем дату в виде строки
            //Берем данные из базы данных
            int countAll = 0;
            if (connection != null) {
                /*String sqlQuerySelect = "select i.Инвентарьid, \n" +
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
                        "\t\t\tAND '"+date+"' > Заявка.Дата_окончания\n" +
                        "\t\tgroup by Инвентарь.\"Инвентарьid\") booking\n" +
                        "\ton n.\"Инвентарьid\" = booking.\"Инвентарьid\"\n" +
                        "\t\twhere i.Инвентарьid = n.Инвентарьid and i.Вид_инвентаряid=t.Вид_инвентаряid\n" +
                        "\t\tand i.Инвентарьid = "+intentory_id+" \n"+
                        "\t\tand i.Модель_инвентаряid=m.Модель_инвентаряid\n" +
                        "\t\tand (n.Количество>0 or (n.Количество + booking.Занятое_количество)>0)";*/
                /*String sqlQuerySelect = "select i.Инвентарьid, \n" +
                        "CASE WHEN  booking.Занятое_количество is null or booking.Занятое_количество=0  THEN n.Количество ELSE (n.Количество + booking.Занятое_количество) END as \"Количество на выбранную дату\"\n" +
                        "\tfrom Инвентарь i, Номер_инвентаря n LEFT JOIN\n" +
                        "\t(select Инвентарь.\"Инвентарьid\", SUM(Состав_заявки.Количество) AS Занятое_количество\n" +
                        "\t\tFROM Инвентарь \n" +
                        "\t\t\tJOIN Номер_инвентаря ON Инвентарь.Инвентарьid = Номер_инвентаря.Инвентарьid\n" +
                        "\t\t\tJOIN Состав_заявки ON Номер_инвентаря.Номер_инвентаряid = Состав_заявки.Номер_инвентаряid\n" +
                        "\t\t\tJOIN Заявка ON Состав_заявки.Заявкаid = Заявка.Заявкаid\n" +
                        "\t\t\tJOIN Статус_бронирования ON Заявка.Статус_бронированияid = Статус_бронирования.Статус_бронированияid\n" +
                        "\t\tWHERE Статус_бронирования.Статус IN ('Забронирован')\n" +
                        "\t\t\tAND '"+date+"' > Заявка.Дата_окончания\n" +
                        "\t\tgroup by Инвентарь.\"Инвентарьid\") booking\n" +
                        "\ton n.\"Инвентарьid\" = booking.\"Инвентарьid\"\n" +
                        "\t\twhere i.Инвентарьid = n.Инвентарьid \n" +
                        "\t\tand i.Инвентарьid = "+intentory_id+" \n"+
                        "\t\tand (n.Количество>0 or (n.Количество + booking.Занятое_количество)>0)";*/
                String sqlQuerySelect = "select i.Инвентарьid, n.Количество + booking_outside.Занятое_количество - booking.Занятое_количество as \"Количество на выбранную дату\" " +
                        "\tfrom Инвентарь i, Номер_инвентаря n \n" +
                        "\tLEFT join (\n" +
                        "\t\t\tselect Инвентарь.\"Инвентарьid\" , SUM(Состав_заявки.Количество) AS Занятое_количество\n" +
                        "\t\t\tFROM Инвентарь \n" +
                        "\t\t\t\tJOIN Номер_инвентаря ON Инвентарь.Инвентарьid = Номер_инвентаря.Инвентарьid\n" +
                        "\t\t\t\tJOIN Состав_заявки ON Номер_инвентаря.Номер_инвентаряid = Состав_заявки.Номер_инвентаряid\n" +
                        "\t\t\t\tJOIN Заявка ON Состав_заявки.Заявкаid = Заявка.Заявкаid\n" +
                        "\t\t\t\tJOIN Статус_бронирования ON Заявка.Статус_бронированияid = Статус_бронирования.Статус_бронированияid\n" +
                        "\t\t\tWHERE Статус_бронирования.Статус IN ('Забронирован', 'Выдан')\n" +
                        "\t\t\t\tAND ('"+date+"' >= Заявка.\"Дата_начала\" and '"+date+"' <= Заявка.\"Дата_окончания\" )\n" +
                        "\t\t\tgroup by Инвентарь.\"Инвентарьid\") booking on n.\"Инвентарьid\" = booking.\"Инвентарьid\"\n" +
                        "\t\tjoin (select Инвентарь.\"Инвентарьid\" , SUM(Состав_заявки.Количество) AS Занятое_количество\n" +
                        "\t\t\tFROM Инвентарь \n" +
                        "\t\t\t\tJOIN Номер_инвентаря ON Инвентарь.Инвентарьid = Номер_инвентаря.Инвентарьid\n" +
                        "\t\t\t\tJOIN Состав_заявки ON Номер_инвентаря.Номер_инвентаряid = Состав_заявки.Номер_инвентаряid\n" +
                        "\t\t\t\tJOIN Заявка ON Состав_заявки.Заявкаid = Заявка.Заявкаid\n" +
                        "\t\t\t\tJOIN Статус_бронирования ON Заявка.Статус_бронированияid = Статус_бронирования.Статус_бронированияid\n" +
                        "\t\t\tWHERE Статус_бронирования.Статус IN ('Забронирован', 'Выдан')\n" +
                        "\t\t\t\tAND ('"+date+"' > Заявка.\"Дата_окончания\" )\n" +
                        "\t\t\tgroup by Инвентарь.\"Инвентарьid\") booking_outside  ON booking.\"Инвентарьid\" = booking_outside.\"Инвентарьid\" \n" +
                        "\twhere i.Инвентарьid = n.Инвентарьid\n" +
                        "\t\tand i.Инвентарьid = "+intentory_id+"\n" +
                        "\t\tand (n.Количество>0 or (n.Количество + booking.Занятое_количество)>0)";
                Statement statement = null;
                try {
                    statement = connection.createStatement();
                    ResultSet set = statement.executeQuery(sqlQuerySelect);

                    while (set.next()) {
                        countAll = set.getInt(2);
                    }
                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }
            }

            inventoryMap.put(date, countAll); // Добавляем пару дата-инвентарь в хэш-карту
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        try {
            connection.close();
        }
        catch (Exception e){
            Log.e("Error: ", e.getMessage());
        }
    }

    private void dataCountInventory3(int intentory_id) {
        int month = calendar.get(Calendar.MONTH) + 1; // Месяцы нумеруются с 0, поэтому прибавляем 1
        int year = calendar.get(Calendar.YEAR);
        calendar.set(year, month - 1, 1); // Устанавливаем календарь на начало выбранного месяца
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // Получаем количество дней в месяце
        //запрос
        ConnectionDatabase connectionSQL = new ConnectionDatabase();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        connection = connectionSQL.connectionClass();
        for (int i = 0; i < daysInMonth; i++) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(calendar.getTime()); // Форматируем дату в виде строки
            //Берем данные из базы данных
            int countAll = 0;
            int countBookingInside = 0;
            int countBookingOutside = 0;
            if (connection != null) {
                String sqlQueryAll = "SELECT Вид_инвентаря.Вид, Модель_инвентаря.Модель, Номер_инвентаря.Количество AS Общее_количество\n" +
                        "FROM Вид_инвентаря\n" +
                        "JOIN Инвентарь ON Вид_инвентаря.Вид_инвентаряid = Инвентарь.Вид_инвентаряid\n" +
                        "JOIN Модель_инвентаря ON Инвентарь.Модель_инвентаряid = Модель_инвентаря.Модель_инвентаряid\n" +
                        "JOIN Номер_инвентаря ON Инвентарь.Инвентарьid = Номер_инвентаря.Инвентарьid " +
                        "WHERE Номер_инвентаря.Инвентарьid = " + intentory_id;

                String sqlQueryBookingInside = "select з.\"Заявкаid\", сб.\"Статус\", з.\"Дата_начала\", з.\"Дата_окончания\", \n" +
                        "ни.\"Инвентарьid\", сз.\"Количество\"\n" +
                        "from \"Заявка\" з, \"Состав_заявки\" сз, \"Номер_инвентаря\" ни, \"Статус_бронирования\" сб\n" +
                        "where '"+date+"' >= з.\"Дата_начала\" and '"+date+"' <= з.\"Дата_окончания\"\n" +
                        "and з.\"Заявкаid\" = сз.\"Заявкаid\" \n" +
                        "and сз.\"Номер_инвентаряid\" = ни.\"Номер_инвентаряid\"\n" +
                        "and ни.\"Инвентарьid\" = "+intentory_id+"\n" +
                        "and сб.\"Статус_бронированияid\" = з.\"Статус_бронированияid\"\n" +
                        "and сб.\"Статус\" IN ('Забронирован')";

                String sqlQueryBookingOutside = "select з.\"Заявкаid\", сб.\"Статус\", з.\"Дата_начала\", з.\"Дата_окончания\", \n" +
                        "ни.\"Инвентарьid\", сз.\"Количество\"\n" +
                        "from \"Заявка\" з, \"Состав_заявки\" сз, \"Номер_инвентаря\" ни, \"Статус_бронирования\" сб\n" +
                        "where '"+date+"' > з.\"Дата_окончания\"\n" +
                        "\tand з.\"Заявкаid\" = сз.\"Заявкаid\" \n" +
                        "\tand сз.\"Номер_инвентаряid\" = ни.\"Номер_инвентаряid\"\n" +
                        "\tand ни.\"Инвентарьid\" = "+intentory_id+"\n" +
                        "\tand сб.\"Статус_бронированияid\" = з.\"Статус_бронированияid\"\n" +
                        "\tand сб.\"Статус\" IN ('Забронирован')";

                Statement statement = null;
                try {
                    statement = connection.createStatement();
                    ResultSet set = statement.executeQuery(sqlQueryAll);

                    while (set.next()) {
                        countAll = set.getInt(3);
                    }

                    set = statement.executeQuery(sqlQueryBookingInside);
                    while (set.next()) {
                        countBookingInside = set.getInt(6);
                    }

                    set = statement.executeQuery(sqlQueryBookingOutside);
                    while (set.next()) {
                        countBookingOutside = set.getInt(6);
                    }
                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }
            }

            int countTotal = countAll - countBookingInside + countBookingOutside;
            inventoryMap.put(date, countTotal); // Добавляем пару дата-инвентарь в хэш-карту
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        try {
            connection.close();
        }
        catch (Exception e){
            Log.e("Error: ", e.getMessage());
        }
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

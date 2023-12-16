package com.example.sportikmobileapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.database.ConnectionDatabase;
import com.example.sportikmobileapp.database.inventory.InventoryModel;
import com.example.sportikmobileapp.database.inventory.ModelInventoryModel;
import com.example.sportikmobileapp.database.inventory.TypeInventoryModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

// Создаем свой класс-адаптер, наследуясь от BaseAdapter
public class CustomCalendarAdapter extends BaseAdapter {
    // Объявляем необходимые поля
    private Context context;
    private Calendar calendar;
    private LayoutInflater inflater;
    private int firstDay;
    private int maxDays;
    private int month;
    private int year;
    private SimpleDateFormat dateFormat;
    private HashMap<String, Integer> inventoryMap; // Хэш-карта для хранения количества инвентаря для каждой даты

    Connection connection;

    // Конструктор адаптера
    public CustomCalendarAdapter(Context context, int month, int year, HashMap<String, Integer> _inventoryMap) {
        this.context = context;
        this.month = month;
        this.year = year;
        calendar = Calendar.getInstance();
        inflater = LayoutInflater.from(context);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        inventoryMap = new HashMap<>(); // Инициализируем хэш-карту
        inventoryMap = _inventoryMap; // Инициализируем хэш-карту

        //populateInventoryMap(); // Заполняем хэш-карту данными из базы данных
        refreshDays(); // Обновляем дни в календаре
    }

    // Метод для заполнения хэш-карты данными из базы данных
    /*
    private void populateInventoryMap() {
        // Здесь вы можете использовать любой способ получения данных из базы данных, например, с помощью Cursor или SQLiteOpenHelper
        // Для простоты примера мы просто заполним хэш-карту случайными числами от 0 до 10
        Random random = new Random();
        calendar.set(year, month - 1, 1); // Устанавливаем календарь на начало выбранного месяца
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // Получаем количество дней в месяце
        for (int i = 0; i < daysInMonth; i++) {
            String date = dateFormat.format(calendar.getTime()); // Форматируем дату в виде строки
            //Берем данные из базы данных
            //запрос
            ConnectionDatabase connectionSQL = new ConnectionDatabase();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            connection = connectionSQL.connectionClass();
            int countAll = 0;
            int countBooking = 0;
            if(connection != null){
                String sqlQueryAll = "SELECT Вид_инвентаря.Вид, Модель_инвентаря.Модель, Номер_инвентаря.Количество AS Общее_количество\n" +
                        "FROM Вид_инвентаря\n" +
                        "JOIN Инвентарь ON Вид_инвентаря.Вид_инвентаряid = Инвентарь.Вид_инвентаряid\n" +
                        "JOIN Модель_инвентаря ON Инвентарь.Модель_инвентаряid = Модель_инвентаря.Модель_инвентаряid\n" +
                        "JOIN Номер_инвентаря ON Инвентарь.Инвентарьid = Номер_инвентаря.Инвентарьid " +
                        "WHERE Номер_инвентаря.Инвентарьid = "+1;

                String sqlQueryBooking = "select Инвентарь.\"Инвентарьid\", Вид_инвентаря.\"Вид_инвентаряid\", Модель_инвентаря.\"Модель_инвентаряid\", Вид_инвентаря.Вид, Модель_инвентаря.Модель, SUM(Состав_заявки.Количество) AS Занятое_количество\n" +
                        "FROM Вид_инвентаря\n" +
                        "JOIN Инвентарь ON Вид_инвентаря.Вид_инвентаряid = Инвентарь.Вид_инвентаряid\n" +
                        "JOIN Модель_инвентаря ON Инвентарь.Модель_инвентаряid = Модель_инвентаря.Модель_инвентаряid\n" +
                        "JOIN Номер_инвентаря ON Инвентарь.Инвентарьid = Номер_инвентаря.Инвентарьid\n" +
                        "JOIN Состав_заявки ON Номер_инвентаря.Номер_инвентаряid = Состав_заявки.Номер_инвентаряid\n" +
                        "JOIN Заявка ON Состав_заявки.Заявкаid = Заявка.Заявкаid\n" +
                        "JOIN Статус_бронирования ON Заявка.Статус_бронированияid = Статус_бронирования.Статус_бронированияid\n" +
                        "WHERE Статус_бронирования.Статус IN ('Забронирован')\n and Инвентарь.\"Инвентарьid\"="+1+" "+
                        "AND \'"+date+"\' > Заявка.Дата_окончания\n" +
                        "group by  Инвентарь.\"Инвентарьid\", Вид_инвентаря.\"Вид_инвентаряid\", Модель_инвентаря.\"Модель_инвентаряid\", Вид_инвентаря.Вид, Модель_инвентаря.Модель";

                Statement statement = null;
                try {
                    statement = connection.createStatement();
                    ResultSet set = statement.executeQuery(sqlQueryAll);

                    while (set.next()){
                        countAll = set.getInt(3);
                    }

                    set = statement.executeQuery(sqlQueryBooking);
                    while (set.next()){
                        countBooking = set.getInt(6);
                    }
                    connection.close();
                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }
            }
            int countTotal = 0;
            if(countAll == 0)
                countTotal = countBooking;
            else
                countTotal = countBooking+countAll;


            int inventory_count = random.nextInt(11); // Генерируем случайное число от 0 до 10
            inventoryMap.put(date, countTotal); // Добавляем пару дата-инвентарь в хэш-карту
            calendar.add(Calendar.DAY_OF_MONTH, 1); // Переходим к следующему дню
        }
    }
    */

    // Метод для обновления дней в календаре
    public void refreshDays() {

        // Устанавливаем календарь на начало выбранного месяца
        calendar.set(year, month - 1, 1);
        // Получаем номер первого дня недели в месяце (от 1 до 7)
        firstDay = calendar.get(Calendar.DAY_OF_WEEK);
        if(firstDay == 1)
            firstDay = 7; //воскресенье
        else
            firstDay = calendar.get(Calendar.DAY_OF_WEEK) - 1; //остальные дни
        // Получаем количество дней в месяце
        maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        // Оповещаем адаптер об изменении данных
        notifyDataSetChanged();
    }

    // Метод для получения количества элементов в адаптере
    @Override
    public int getCount() {
        return maxDays + firstDay - 1; // Возвращаем сумму дней в месяце и пустых ячеек до первого дня
    }

    // Метод для получения элемента по позиции
    @Override
    public Object getItem(int position) {
        return null; // В нашем случае нам не нужно возвращать конкретный объект, поэтому возвращаем null
    }

    // Метод для получения идентификатора элемента по позиции
    @Override
    public long getItemId(int position) {
        return 0; // В нашем случае нам не нужно возвращать конкретный идентификатор, поэтому возвращаем 0
    }

    // Метод для получения представления элемента по позиции
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Если convertView равен null, то создаем новое представление из нашего макета custom_calendar_day
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.custom_calendar_day, parent, false);
        }
        // Получаем ссылки на элементы макета: TextView для даты и инвентаря
        TextView dateTextView = convertView.findViewById(R.id.date_text_view);
        TextView inventoryTextView = convertView.findViewById(R.id.inventory_text_view);

        // Если позиция меньше номера первого дня недели, то это пустая ячейка
        if (position < firstDay - 1) {
            // Скрываем элементы макета
            dateTextView.setVisibility(View.GONE);
            inventoryTextView.setVisibility(View.GONE);
        } else {
            // Иначе это ячейка с датой
            // Показываем элементы макета
            dateTextView.setVisibility(View.VISIBLE);
            inventoryTextView.setVisibility(View.VISIBLE);
            // Вычисляем номер дня в месяце
            int day = position - firstDay + 2;
            // Устанавливаем текст для даты
            dateTextView.setText(String.valueOf(day));
            // Устанавливаем календарь на этот день
            calendar.set(year, month - 1, day);
            // Форматируем дату в виде строки
            String date = dateFormat.format(calendar.getTime());
            // Получаем количество инвентаря из хэш-карты по дате
            int inventory = inventoryMap.get(date);
            // Устанавливаем текст для инвентаря
            inventoryTextView.setText(String.valueOf(inventory));
        }
        // Возвращаем готовое представление
        return convertView;
    }
}
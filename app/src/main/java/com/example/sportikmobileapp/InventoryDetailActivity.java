package com.example.sportikmobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.sportikmobileapp.adapter.CustomCalendarAdapter;
import com.example.sportikmobileapp.database.inventory.InventoryModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class InventoryDetailActivity extends AppCompatActivity {
    private Button prevButton, nextButton;
    private TextView monthTextView;
    private GridView gridView;
    private CustomCalendarAdapter adapter;
    private Calendar calendar;
    private int month, year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_detail);
        TextView txtType = findViewById(R.id.txtTypeInventoryDetail);
        TextView txtModel = findViewById(R.id.txtModelInventoryDetail);
        TextView txtCost = findViewById(R.id.txtCostInventoryDetail);
        TextView txtCount = findViewById(R.id.txtCountInventoryDetail);

        // Получаем ссылки на элементы макета
        prevButton = findViewById(R.id.prev_button);
        nextButton = findViewById(R.id.next_button);
        monthTextView = findViewById(R.id.month_text_view);
        gridView = findViewById(R.id.grid_view);

        //Находим переданные данные из другого окна
        Bundle arguments = getIntent().getExtras();
        InventoryModel inventory;
        //Проверям, не пусты ли эти данные
        if(arguments!=null){

            //Получаем данные о выбранном инвентаре
            inventory = (InventoryModel) arguments.getSerializable(InventoryModel.class.getSimpleName());
            String _type = "Вид: " + inventory.getType().getName();
            String _model = "Модель: " + inventory.getModel().getName();
            String _cost = "Стоимость: " + inventory.getCost();
            String _count = "В наличии: " + inventory.getCount();

            ////Hashmap - список с ключом и его значением; ключ - это дата, значение - это количество инвентаря
            HashMap<String, Integer> _inventoryMap = (HashMap<String, Integer>) arguments.get("HASHMAP");

            // Создаем календарь и устанавливаем его на текущий месяц и год
            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            month = calendar.get(Calendar.MONTH) + 1; // Месяцы нумеруются с 0, поэтому прибавляем 1
            year = calendar.get(Calendar.YEAR);

            // Создаем наш адаптер и устанавливаем его для GridView
            adapter = new CustomCalendarAdapter(this, month, year, _inventoryMap);
            gridView.setAdapter(adapter);

            // Устанавливаем текст для TextView с названием месяца
            monthTextView.setText(getMonthName(month) + " " + year);

            //Заполняем данными TextView
            txtType.setText(_type);
            txtModel.setText(_model);
            txtCost.setText(_cost);
            txtCount.setText(_count);

            // Устанавливаем слушатели для кнопок переключения месяцев
            prevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar_today = Calendar.getInstance();
                    int month_today = calendar_today.get(Calendar.MONTH) + 1;
                    int year_today = calendar_today.get(Calendar.YEAR);
                    int month_current = month;
                    int year_current = year;
                    // Переходим к предыдущему месяцу
                    if (month_current == 1) { // Если это январь, то переходим к декабрю предыдущего года
                        month_current = 12;
                        year_current--;
                    } else { // Иначе просто уменьшаем номер месяца на 1
                        month_current--;
                    }
                    if(month_today>month_current && year_today==year_current)
                        return;
                    /*
                    if(year_today==year_current && month_current == month_today+3)
                        return;
                    else if(year_today!=year_current && 12-(month_today - month_current)==3)
                        return;*/

                    // Переходим к предыдущему месяцу
                    if (month == 1 ) { // Если это январь, то переходим к декабрю предыдущего года
                        month = 12;
                        year--;

                    } else { // Иначе просто уменьшаем номер месяца на 1
                        month--;
                    }
                    // Обновляем адаптер и TextView
                    adapter = new CustomCalendarAdapter(InventoryDetailActivity.this, month, year, _inventoryMap);
                    gridView.setAdapter(adapter);
                    monthTextView.setText(getMonthName(month) + " " + year);
                }
            });

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar_today = Calendar.getInstance();
                    int month_today = calendar_today.get(Calendar.MONTH) + 1;
                    int year_today = calendar_today.get(Calendar.YEAR);
                    int month_current = month;
                    int year_current = year;
                    // Переходим к предыдущему месяцу
                    if (month_current == 12) { // Если это январь, то переходим к декабрю предыдущего года
                        month_current = 1;
                        year_current++;
                    } else { // Иначе просто уменьшаем номер месяца на 1
                        month_current++;
                    }

                    if(year_today==year_current && month_current-3 == month_today)
                        return;
                    else if(year_today!=year_current && 12-(month_today - month_current)==3)
                        return;

                    // Переходим к следующему месяцу
                    if (month == 12) { // Если это декабрь, то переходим к январю следующего года
                        month = 1;
                        year++;
                    } else { // Иначе просто увеличиваем номер месяца на 1
                        month++;
                    }

                    // Обновляем адаптер и TextView
                    adapter = new CustomCalendarAdapter(InventoryDetailActivity.this, month, year, _inventoryMap);
                    gridView.setAdapter(adapter);
                    monthTextView.setText(getMonthName(month) + " " + year);
                }
            });
        }

    }

    // Метод для получения названия месяца по его номеру
    private String getMonthName(int month) {
        switch (month) {
            case 1:
                return "Январь";
            case 2:
                return "Февраль";
            case 3:
                return "Март";
            case 4:
                return "Апрель";
            case 5:
                return "Май";
            case 6:
                return "Июнь";
            case 7:
                return "Июль";
            case 8:
                return "Август";
            case 9:
                return "Сентябрь";
            case 10:
                return "Октябрь";
            case 11:
                return "Ноябрь";
            case 12:
                return "Декабрь";
            default:
                return "";
        }
    }

}


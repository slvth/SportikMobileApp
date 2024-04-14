package com.example.sportikmobileapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.adapter.InventoryAddAdapter;
import com.example.sportikmobileapp.database.ConnectionDatabase;
import com.example.sportikmobileapp.database.model.BookingDetailModel;
import com.example.sportikmobileapp.database.model.BookingModel;
import com.example.sportikmobileapp.database.model.InventoryModel;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class BookingAddActivity extends AppCompatActivity {
    Connection connection;
    InventoryAddAdapter inventoryAddAdapter;
    RecyclerView recyclerViewBookingAdd;
    ArrayList<BookingDetailModel> bookingDetalList;

    AppCompatButton btnOpenInventoryAdd, btnSaveBookingAdd;
    TextView txtStartBookingAdd, txtTotalSumBookingAdd, txtAgreement;
    ImageButton btnDate, btnBack;
    CheckBox checkBoxAgreement;
    //EditText edtCoundDay;
    Long startDate, endDate; //Начало бронирования, миллисекунды
    int countDay;
    Float totalSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_add);

        recyclerViewBookingAdd = findViewById(R.id.recyclerViewBookingAdd);
        txtStartBookingAdd = findViewById(R.id.txtStartBookingAdd);
        txtTotalSumBookingAdd = findViewById(R.id.txtTotalSumBookingAdd);
        btnBack = findViewById(R.id.btnBackBookingAdd);
        btnOpenInventoryAdd = findViewById(R.id.btnOpenInventoryAdd);
        btnSaveBookingAdd = findViewById(R.id.btnSaveBookingAdd);
        btnDate = findViewById(R.id.btnDateBookingAdd);
        //edtCoundDay= findViewById(R.id.edtCoundDayBookingAdd);
        checkBoxAgreement = findViewById(R.id.checkBoxAgreement);
        txtAgreement = findViewById(R.id.txtAgreement);

        defaultValues();

        if(startDate!=null)
            downloadDataToRecyclerview();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED, getIntent());
                finish();
            }
        });

        //Обработка нажатия кнопки для добавления инвентаря в бронирование
        btnOpenInventoryAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startDate==null || startDate==0){
                    Toast.makeText(BookingAddActivity.this, "Сначала выберите дату бронирования!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(startDate);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String startDateString = dateFormat.format(calendar.getTime()); // Форматируем дату в виде строки

                Intent intent = new Intent(view.getContext(), InventoryAddActivity.class);
                intent.putExtra("START_DATE", startDateString);
                intent.putExtra(BookingDetailModel.class.getSimpleName(), bookingDetalList);
                startActivityForResult(intent, 100);
            }
        });

        //Обработка нажатия кнопки для выбора дат бронирования
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startDate!=null && endDate!=null && !bookingDetalList.isEmpty())
                    // Отображение диалогового окна для выбора дат - начало и окончание
                    new AlertDialog.Builder(BookingAddActivity.this).setTitle("Изменение даты")
                            .setMessage("Вы точно хотите изменить дату?\nИзменение даты приведет к очистке добавленного инвентаря")
                            .setPositiveButton("Да",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            DatePickerdialog();
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
                else
                    DatePickerdialog();
            }
        });

        btnSaveBookingAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBooking();
            }
        });

        txtAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDocument("dogovor");
            }
        });
    }

    private void openDocument(String name) {
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        File file = new File(name);
        String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString());
        String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        if (extension.equalsIgnoreCase("") || mimetype == null) {
            // if there is no extension or there is no definite mimetype, still try to open the file
            intent.setDataAndType(Uri.fromFile(file), "text/*");
        } else {
            intent.setDataAndType(Uri.fromFile(file), mimetype);
        }
        // custom message for the intent
        startActivity(Intent.createChooser(intent, "Choose an Application:"));
    }

    private void defaultValues(){
        bookingDetalList = new ArrayList<>();
        totalSum = 0f;
        countDay = 0;
        //edtCoundDay.setText(String.valueOf(countDay));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            Bundle arguments = data.getExtras();
            if (arguments.getSerializable(BookingDetailModel.class.getSimpleName()) != null) {
                bookingDetalList = (ArrayList<BookingDetailModel>) arguments.getSerializable(BookingDetailModel.class.getSimpleName());
                inventoryAddAdapter.notifyItemInserted(bookingDetalList.size() - 1);
                downloadDataToRecyclerview();
            }
        }
    }

    private void downloadDataToRecyclerview(){
        ArrayList<InventoryModel> inventoryList = new ArrayList<>();
        inventoryAddAdapter = new InventoryAddAdapter(this, inventoryList, bookingDetalList, true,  txtTotalSumBookingAdd, countDay);
        recyclerViewBookingAdd.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewBookingAdd.setAdapter(inventoryAddAdapter);
        updateTotalSum();
    }

    //Метод для получение финальной цены бронирования
    private void updateTotalSum(){
        totalSum = 0f;
        int countDay = (int) (TimeUnit.MILLISECONDS.toDays(endDate - startDate) + 1);
        for(BookingDetailModel item: bookingDetalList)
            totalSum = (totalSum + (item.getCount()*item.getInventory().getCost())) ;
        totalSum = totalSum*countDay;
        txtTotalSumBookingAdd.setText("ИТОГО: "+totalSum+" руб");
    }

    private void saveBooking(){
        if(bookingDetalList.size()==0 || bookingDetalList.isEmpty() || startDate==null || endDate==null){
            Toast.makeText(this, "Заполните все данные!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!checkBoxAgreement.isChecked()){
            Toast.makeText(this, "Примите соглашение!", Toast.LENGTH_SHORT).show();
            return;
        }
        ConnectionDatabase connectionSQL = new ConnectionDatabase();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        connection = connectionSQL.connectionClass();
        if (connection != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar today = Calendar.getInstance();
            String todayString = dateFormat.format(today.getTime());
            String startDateString = dateFormat.format(new Date(startDate));
            String endDateString = dateFormat.format(new Date(endDate));
            totalSum = inventoryAddAdapter.getTotal()!=0 ? inventoryAddAdapter.getTotal() : totalSum;

            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE);
            int user_id = sharedPref.getInt(getString(R.string.pref_id), 0);

            String sqlQueryInsert = "INSERT INTO Заявка (Дата_подачи, Дата_начала, Дата_окончания, ПользовательID, СотрудникID, Итоговая_стоимость, Статус_бронированияID) VALUES\n" +
                    "('"+todayString+"', '"+startDateString+"', '"+endDateString+"', "+user_id+", null, "+totalSum+", 4)  RETURNING ЗаявкаID";
            Statement statement = null;
            try {
                statement = connection.createStatement();
                ResultSet set = statement.executeQuery(sqlQueryInsert);
                set.next();
                int _id =  set.getInt(1);
                for(BookingDetailModel item : bookingDetalList){
                    statement = connection.createStatement();
                    String sqlQueryInsert2= "INSERT INTO Состав_заявки (Номер_инвентаряID, Количество, ЗаявкаID) VALUES " +
                            "("+item.getInventory().getInventory_number()+", "+item.getCount()+", "+_id+")";
                    statement.execute(sqlQueryInsert2);
                }
                Toast.makeText(this, "Заявка №"+_id+": подана", Toast.LENGTH_SHORT).show();
                connection.close();

                BookingModel bookingModel = new BookingModel(_id, todayString, startDateString, endDateString, 1, 0, totalSum, "Подан", bookingDetalList);
                Intent intent = new Intent();
                intent.putExtra(BookingModel.class.getSimpleName(), bookingModel);
                setResult(RESULT_OK,intent);
                finish();
            } catch (Exception e) {
                Toast.makeText(this,  e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("Error: ", e.getMessage());
            }
        }
    }


    private void DatePickerdialog() {
        Date today = Calendar.getInstance().getTime();
        Calendar calendarMax = Calendar.getInstance();
        calendarMax.add( Calendar.MONTH, 2);
        long minDate = today.getTime();
        long maxDate = calendarMax.getTime().getTime();
        CalendarConstraints constraints = new CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
                .setStart(minDate)
                .setEnd(maxDate)
                .build();
        // Создание конструктора MaterialDatePicker для выбора диапазона дат.
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Выберите даты бронирования:");
        builder.setCalendarConstraints(constraints);

        // Создание диалога выбора даты
        MaterialDatePicker<Pair<Long, Long>> datePicker = builder.build();
        datePicker.addOnPositiveButtonClickListener(selection -> {
            // Очистка данных, дефолтные значения
            defaultValues();

            // Получение выбранных дат начала и окончания
            startDate = selection.first;
            endDate = selection.second;

            // Форматирование выбранных дат в виде строк
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
            String startDateString = sdf.format(new Date(startDate));
            String endDateString = sdf.format(new Date(endDate));

            // Создание строки диапазона дат
            String selectedDateRange = "Начало: "+startDateString + "\nКонец: " + endDateString;
            countDay = (int) (TimeUnit.MILLISECONDS.toDays(endDate - startDate) + 1);
            String countDayString = String.valueOf(countDay); //прибавление +1, чтобы он учитывал и текущий день

            // Отображение выбранного диапазона дат в TextView
            txtStartBookingAdd.setText(selectedDateRange);
            //edtCoundDay.setText(countDayString);

            downloadDataToRecyclerview();
        });

        // Отображение диалогового окна выбора даты
        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }

}
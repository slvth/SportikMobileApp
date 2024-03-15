package com.example.sportikmobileapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.database.ConnectionDatabase;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RegistrationActivity extends AppCompatActivity {
Connection connection;
    LinearLayout linearStep1, linearStep2;
    TextInputEditText edtLogin, edtPassword, edtSurname, edtName, edtPatronymic, edtEmail, edtPhone, edtPassport;
    AppCompatButton btnNextStep, btnRegistration;
    ImageButton imgClose, imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Области - шаг1, шаг2
        linearStep1 = findViewById(R.id.linearStep1);
        linearStep2 = findViewById(R.id.linearStep2);

        //Кнопки
        btnNextStep = findViewById(R.id.btnNextStep);
        btnRegistration = findViewById(R.id.btnRegistration);
        imgClose = findViewById(R.id.imgClose);
        imgBack = findViewById(R.id.imgBack);

        //Поля ввода
        edtLogin = findViewById(R.id.edtLogin);
        edtPassword = findViewById(R.id.edtPassword);
        edtSurname = findViewById(R.id.edtSurname);
        edtName = findViewById(R.id.edtName);
        edtPatronymic = findViewById(R.id.edtPatronymic);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassport = findViewById(R.id.edtPassport);

        // Скрытие области шаг2 -> показ области шаг1
        linearStep1.setVisibility(View.VISIBLE);
        linearStep2.setVisibility(View.GONE);

        // Обработка нажатия на кнопку "Далее", который переводит к шагу 2
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Проверка на заполненность
                if(!isValidateStep1())
                    return;
                // Проверка на уникальность логина
                if(isNewLogin()){
                    // Скрытие области шаг1 -> показ области шаг2
                    linearStep1.setVisibility(View.GONE);
                    linearStep2.setVisibility(View.VISIBLE);
                }
            }
        });

        //Обработка нажатия на кнопку "Зарегистрироваться"
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Проверка на заполненность
                if(!isValidateStep2())
                    return;
                //Добавление нового пользователя системы и нового пользователя
                addNewUser();
            }
        });

        //Обработка нажатия на кнопку "Назад" в шаге1
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Закрытие окна
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        //Обработка нажатия на кнопку "Назад" в шаге2
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Скрытие области шаг2 -> показ области шаг1
                linearStep1.setVisibility(View.VISIBLE);
                linearStep2.setVisibility(View.GONE);
            }
        });

    }

    private boolean isValidateStep1(){
        if (edtLogin.length() == 0) {
            String error = "Заполните поле \"Логин\"";
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            edtLogin.setError(error);
            return false;
        }
        if (edtPassword.length() == 0) {
            String error = "Заполните поле \"Пароль\"!";
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            edtPassword.setError(error);
            return false;
        }

        return true;
    }

    private boolean isValidateStep2(){
        if (edtSurname.length() == 0) {
            String error = "Заполните поле \"Фамилия\"";
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            edtSurname.setError(error);
            return false;
        }

        if (edtName.length() == 0) {
            String error = "Заполните поле \"Имя\"!";
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            edtName.setError(error);
            return false;
        }

        if (edtEmail.length() == 0) {
            String error = "Заполните поле \"Почта\"!";
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            edtEmail.setError(error);
            return false;
        }

        if (edtPhone.length() == 0) {
            String error = "Заполните поле \"Телефон\"!";
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            edtPhone.setError(error);
            return false;
        }

        if (edtPassport.length() == 0) {
            String error = "Заполните поле \"Паспорт\"!";
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            edtPassport.setError(error);
            return false;
        }

        return true;
    }

    private boolean isNewLogin(){
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        connection = connectionDatabase.connectionClass();

        if(connection!=null){
            String login = edtLogin.getText().toString();
            String sqlQueryFindLogin = "Select * from Пользователь_системы where Логин = '"+login+"'";

            try {
                Statement statement = connection.createStatement();
                statement.executeQuery(sqlQueryFindLogin);
                ResultSet set = statement.getResultSet();
                set.next();
                if(set.getRow()>0){
                    Toast.makeText(this, "Такой логин уже существует. Выберите другой!", Toast.LENGTH_SHORT).show();
                    return false;
                }
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    private void addNewUser(){
        ConnectionDatabase connectionDatabase = new ConnectionDatabase();
        connection = connectionDatabase.connectionClass();
        if(connection!=null){
            String login = edtLogin.getText().toString();
            String password = edtPassword.getText().toString();

            String surname = edtSurname.getText().toString();
            String name = edtName.getText().toString();
            String patronymic = edtPatronymic.length()==0 ? "" : edtPatronymic.getText().toString();
            String email = edtEmail.getText().toString();
            String phone = edtPhone.getText().toString();
            String passport = edtPassport.getText().toString();

            //Запрос на добавление нового пользователя системы, таблица "Пользователь_системы"
            //РольID = 2 (Роль "Пользователь")
            String sqlQueryInsert1 = "Insert into Пользователь_системы (Логин, Пароль, РольID) values " +
                    "( '"+login+"', '"+password+"', "+2+" ) returning Пользователь_системыid";

            try {
                Statement statement = connection.createStatement();
                statement.executeQuery(sqlQueryInsert1);
                ResultSet set = statement.getResultSet();
                set.next();
                int account_id = set.getInt(1);

                //Запрос на добавление нового пользователя, таблица "Пользователь"
                String sqlQueryInsert2 = "Insert into Пользователь (Фамилия, Имя, Отчество, Телефон, " +
                        "Паспорт, Почта, Пользователь_системыID) values " +
                        "('"+surname+"', " +
                        "'"+name+"', " +
                        "'"+patronymic+"', " +
                        "'"+phone+"', " +
                        "'"+passport+"', " +
                        "'"+email+"', " +
                        ""+account_id+"" +
                        ") returning ПользовательID";

                statement.executeQuery(sqlQueryInsert2);
                set = statement.getResultSet();
                set.next();
                int user_id = set.getInt(1);
                connection.close();

                //Сохранение id пользователя в память программы
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt(getString(R.string.pref_id), user_id);
                editor.apply();

                Toast.makeText(this, "Успешная регистрация!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
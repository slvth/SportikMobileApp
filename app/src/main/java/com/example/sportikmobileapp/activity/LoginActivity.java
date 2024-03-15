package com.example.sportikmobileapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sportikmobileapp.MainActivity;
import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.database.ConnectionDatabase;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginActivity extends AppCompatActivity {
    Connection connection;
    TextInputEditText edtLogin, edtPassword;
    TextView txtRegistration;

    AppCompatButton btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtLogin = findViewById(R.id.edtLoginAdress);
        edtPassword = findViewById(R.id.edtPasswordAddress);
        txtRegistration = findViewById(R.id.txtRegistration);
        btnLogin = findViewById(R.id.btnLogin);

        //логин и пароль пользователя_системы
        //edtLogin.setText("user1");
        //edtPassword.setText("qwerty");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        txtRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(LoginActivity.this, RegistrationActivity.class),101);
            }
        });
    }
    private void signIn(){
        if(!isValidate()) //проверка на заполненность
            return;
        ConnectionDatabase connectionSQL = new ConnectionDatabase();
        connection = connectionSQL.connectionClass();
        int user_id = -1;

        String login = edtLogin.getText().toString();
        String password = edtPassword.getText().toString();
        String sqlQueryFindUser = "select u.Пользовательid from Пользователь u, Пользователь_системы account, Роль r " +
                "where u.Пользователь_системыid = account.Пользователь_системыid and account.Рольid=r.Рольid and r.Вид='Пользователь' and " +
                "account.Логин='"+login+"' and account.Пароль='"+password+"' and r.Вид = 'Пользователь'";

        if(connection != null) {
            try {
                Statement statement = connection.createStatement();
                ResultSet set = statement.executeQuery(sqlQueryFindUser);
                while (set.next()) {
                    user_id = set.getInt(1);
                }
                connection.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            if(user_id==-1) { //проверка, есть ли такой пользователь
                Toast.makeText(this, "Неправильный логин или пароль!", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.pref_id), user_id);
            editor.apply();

            Toast.makeText(this, "Успешная авторизация!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private Boolean isValidate(){
        if(edtLogin.getText().toString().equals("") || edtPassword.getText().toString().equals("")){
            Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}
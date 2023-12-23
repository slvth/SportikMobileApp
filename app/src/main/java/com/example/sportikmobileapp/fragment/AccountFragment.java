package com.example.sportikmobileapp.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sportikmobileapp.activity.LoginActivity;
import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.database.ConnectionDatabase;
import com.example.sportikmobileapp.database.model.UserModel;
import com.santalu.maskara.widget.MaskEditText;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class AccountFragment extends Fragment {
    Connection connection;
    UserModel user = new UserModel();
    LinearLayout linearMainButtons, linearEditButtons, linearAccount, linearFIO;
    TextView txtUserFIO, txtUserID;
    MaskEditText edtAccountPhone,edtAccountPassport;
    EditText edtAccountSurname, edtAccountName, edtAccountMiddleName, edtAccountEmail;
    Button btnExitAccount, btnEditAccount, btnAccountCancel, btnAccountSave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        linearMainButtons = view.findViewById(R.id.linearMainButtons);
        linearEditButtons = view.findViewById(R.id.linearEditButtons);
        linearAccount = view.findViewById(R.id.linearAccount);
        linearFIO = view.findViewById(R.id.linearFIO);

        txtUserFIO = view.findViewById(R.id.txtAccountFIO);
        txtUserID = view.findViewById(R.id.txtAccountID);

        edtAccountPhone = view.findViewById(R.id.edtAccountPhone);
        edtAccountPassport = view.findViewById(R.id.edtAccountPassport);
        edtAccountEmail = view.findViewById(R.id.edtAccountEmail);

        edtAccountSurname = view.findViewById(R.id.edtAccountSurname);
        edtAccountName = view.findViewById(R.id.edtAccountName);
        edtAccountMiddleName = view.findViewById(R.id.edtAccountMiddleName);

        btnExitAccount = view.findViewById(R.id.btnExitAccount);
        btnEditAccount = view.findViewById(R.id.btnEditAccount);
        btnAccountCancel = view.findViewById(R.id.btnAccountCancel);
        btnAccountSave = view.findViewById(R.id.btnAccountSave);
        btnExitAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = requireActivity().getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE);
                sharedPref.edit().remove(getString(R.string.pref_id)).commit();

                startActivity(new Intent(getActivity(), LoginActivity.class));
                requireActivity().finish();
            }
        });
        btnEditAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //режим редактирования включен
                enabledAll(true);
            }
        });

        btnAccountCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enabledAll(false);

                //загрузка данных из бд
                downloadData();

                //загрузка данных в View
                setDataInView();
            }
        });

        btnAccountSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //проверка на заполненность
                if(!isValidate())
                    return;

                //сохранение изменений
                saveEdit();
            }
        });

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //режим редактирования отключен
        enabledAll(false);

        //загрузка данных из бд
        downloadData();

        //загрузка данных в View
        setDataInView();
    }

    private void enabledAll(Boolean isEdit){
        edtAccountPhone.setEnabled(isEdit);
        edtAccountPassport.setEnabled(isEdit);
        edtAccountEmail.setEnabled(isEdit);

        //Transition transition = new Slide(Gravity.TOP);
        Transition transition = new Fade();
        transition.setDuration(500);
        transition.addTarget(R.id.linearMainButtons);

        //Transition transition2 = new Slide(Gravity.TOP);
        Transition transition2 = new Fade();
        transition2.setDuration(500);
        transition2.addTarget(R.id.linearEditButtons);

        if(isEdit){
            TransitionManager.beginDelayedTransition(linearAccount, transition2);
            linearEditButtons.setVisibility(View.VISIBLE);
            TransitionManager.beginDelayedTransition(linearAccount, transition);
            linearMainButtons.setVisibility(View.GONE);

            linearFIO.setVisibility(View.VISIBLE);
        }
        else{
            TransitionManager.beginDelayedTransition(linearAccount, transition);
            linearMainButtons.setVisibility(View.VISIBLE);
            TransitionManager.beginDelayedTransition(linearAccount, transition2);
            linearEditButtons.setVisibility(View.GONE);

            linearFIO.setVisibility(View.GONE);
        }
    }

    private void downloadData(){
        ConnectionDatabase connectionSQL = new ConnectionDatabase();
        connection = connectionSQL.connectionClass();

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE);
        int user_id = sharedPref.getInt(getString(R.string.pref_id), 0);

        //user_id = 2;
        if(connection != null){
            String sqlQuery = "select Фамилия, Имя, Отчество, " +
                    "Телефон, Паспорт, Почта " +
                    "from Пользователь where Пользовательid = "+user_id;

            Statement statement = null;
            try {
                statement = connection.createStatement();
                ResultSet set = statement.executeQuery(sqlQuery);
                while (set.next()){
                    String surname = set.getString(1);
                    String name = set.getString(2);
                    String middleName = set.getString(3);
                    String phone = set.getString(4);
                    String passport = set.getString(5);
                    String email = set.getString(6);
                    user = new UserModel(user_id, surname, name, middleName, phone, passport, email);
                }
                connection.close();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
        }
    }

    // Присваивание значений
    private void setDataInView(){
        txtUserFIO.setText(user.getFio());
        txtUserID.setText(String.valueOf(user.getUser_id()));

        edtAccountSurname.setText(user.getSurname());
        edtAccountName.setText(user.getName());
        edtAccountMiddleName.setText(user.getPatronymic());
        edtAccountPhone.setText(user.getPhone());
        edtAccountPassport.setText(user.getPassport());
        edtAccountEmail.setText(user.getEmail());
    }

    private boolean isValidate() {
        if (edtAccountSurname.length() == 0) {
            String error = "Заполните поле \"Фамилия\"";
            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            edtAccountSurname.setError(error);
            return false;
        }

        if (edtAccountName.length() == 0) {
            String error = "Заполните поле \"Имя\"!";
            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            edtAccountName.setError(error);
            return false;
        }

        if (edtAccountPhone.length() == 0) {
            String error = "Заполните поле \"Телефон\"!";
            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            edtAccountPhone.setError(error);
            return false;
        }

        if (edtAccountPassport.length() == 0) {
            String error = "Заполните поле \"Паспорт\"!";
            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            edtAccountPassport.setError(error);
            return false;
        }

        if (edtAccountEmail.length() == 0) {
            String error = "Заполните поле \"Почта\"!";
            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            edtAccountEmail.setError(error);
            return false;
        }
        // after all validation return true.
        return true;
    }

    private void saveEdit(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Изменение данных");
        builder.setMessage("Вы точно хотите сохранить изменения?");
        builder.setCancelable(true);
        builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() { // Кнопка Да
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ConnectionDatabase connectionSQL = new ConnectionDatabase();
                connection = connectionSQL.connectionClass();

                if(connection != null){
                    Statement statement = null;

                    String surname, name, middleName, gender, phone, passport, email;

                    surname = edtAccountSurname.getText().toString();
                    name = edtAccountName.getText().toString();
                    middleName = edtAccountMiddleName.getText().toString();
                    phone = edtAccountPhone.getText().toString();
                    passport = edtAccountPassport.getText().toString();
                    email = edtAccountEmail.getText().toString();

                    String sqlQueryUpdateUser = "UPDATE Пользователь " +
                            "SET Фамилия='"+surname+"', " +
                            "Имя='"+name+"', " +
                            "Отчество='"+middleName+"', " +
                            "Телефон='"+phone+"', " +
                            "Паспорт='"+passport+"', " +
                            "Почта='"+email+"' " +
                            "WHERE Пользовательid="+user.getUser_id();

                    try {
                        statement = connection.createStatement();
                        statement.executeQuery(sqlQueryUpdateUser);
                        connection.close();

                    } catch (Exception e) {
                        Log.e("Error: ", e.getMessage());
                    }
                }
                Toast.makeText(getActivity(), "Успешное сохранение!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();

                downloadData();
                setDataInView();
                enabledAll(false);
            }
        });
        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() { // Кнопка Нет
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Отпускает диалоговое окно
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
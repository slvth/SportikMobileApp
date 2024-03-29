package com.example.sportikmobileapp.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;

import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.adapter.InventoryAdapter;
import com.example.sportikmobileapp.database.ConnectionDatabase;
import com.example.sportikmobileapp.database.model.InventoryModel;
import com.example.sportikmobileapp.database.model.ModelInventoryModel;
import com.example.sportikmobileapp.database.model.TypeInventoryModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class InventoryFragment extends Fragment {
    Connection connection;
    RecyclerView recyclerViewInventory;
    EditText editTextSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        recyclerViewInventory = view.findViewById(R.id.recyclerViewInventory);
        editTextSearch = view.findViewById(R.id.editTextSearch);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                downloadDataToRecyclerview();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editTextSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                downloadDataToRecyclerview();
                return true;
            }
            return false;
        });
        //editTextSearch.addTextChangedListener;

        downloadDataToRecyclerview();
        return view;
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

            /*
            String sqlQuery = "select i.Инвентарьid, t.Вид_инвентаряid, t.Вид, m.Модель_инвентаряid, m.Модель, i.Стоимость, n.Номер_инвентаряid, n.Количество" +
                    "from Инвентарь i, Вид_инвентаря t, Модель_инвентаря m, Номер_инвентаря n " +
                    "where i.Инвентарьid = n.Инвентарьid and i.Вид_инвентаряid=t.Вид_инвентаряid " +
                    "and i.Модель_инвентаряid=m.Модель_инвентаряid";*/

            String sqlQuery = "";
            if(editTextSearch.getText().length()>0)
                sqlQuery = "select i.Инвентарьid, t.Вид_инвентаряid, t.Вид, m.Модель_инвентаряid, m.Модель, i.Стоимость, n.Номер_инвентаряid, n.Количество, i.КартинкаURL, i.Описание " +
                        "from Инвентарь i, Вид_инвентаря t, Модель_инвентаря m, Номер_инвентаря n " +
                        "where i.Инвентарьid = n.Инвентарьid and i.Вид_инвентаряid=t.Вид_инвентаряid " +
                        "and i.Модель_инвентаряid=m.Модель_инвентаряid " +
                        "and ((lower(t.Вид||' '||m.Модель) LIKE '%"+editTextSearch.getText().toString().toLowerCase()+"%') " +
                        "or (lower(m.Модель||' '||t.Вид) LIKE '%"+editTextSearch.getText().toString().toLowerCase()+"%'))";
            else
                sqlQuery = "select i.Инвентарьid, t.Вид_инвентаряid, t.Вид, m.Модель_инвентаряid, m.Модель, i.Стоимость, n.Номер_инвентаряid, n.Количество, i.КартинкаURL, i.Описание " +
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
                    //InventoryModel inventory = new InventoryModel(set.getInt(1), typeInventory, modelInventory, set.getFloat(6), set.getInt(7), set.getInt(8));
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

        recyclerViewInventory.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewInventory.setAdapter(new InventoryAdapter(getActivity(), inventoryList));

    }

}
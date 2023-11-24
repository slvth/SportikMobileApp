package com.example.sportikmobileapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sportikmobileapp.R;
import com.example.sportikmobileapp.adapter.InventoryAdapter;
import com.example.sportikmobileapp.database.inventory.InventoryModel;
import com.example.sportikmobileapp.database.inventory.ModelInventoryModel;
import com.example.sportikmobileapp.database.inventory.TypeInventoryModel;

import java.util.ArrayList;

public class InventoryFragment extends Fragment {
    RecyclerView recyclerViewInventory;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        recyclerViewInventory = view.findViewById(R.id.recyclerViewInventory);

        ArrayList<InventoryModel> inventoryList = new ArrayList<>();
        ArrayList<TypeInventoryModel> typeList = new ArrayList<>();
        ArrayList<ModelInventoryModel> modelList = new ArrayList<>();

        typeList.add(new TypeInventoryModel(1, "Велосипед"));
        typeList.add(new TypeInventoryModel(2, "Лук"));
        typeList.add(new TypeInventoryModel(2, "Мяч"));

        modelList.add(new ModelInventoryModel(1, "Горный"));
        modelList.add(new ModelInventoryModel(2, "Обычный"));
        modelList.add(new ModelInventoryModel(3, "Скоростной"));

        inventoryList.add(new InventoryModel(1, typeList.get(0), modelList.get(0), 100));
        inventoryList.add(new InventoryModel(1, typeList.get(1), modelList.get(2), 100));
        inventoryList.add(new InventoryModel(1, typeList.get(0), modelList.get(2), 100));
        inventoryList.add(new InventoryModel(1, typeList.get(2), modelList.get(2), 100));

        recyclerViewInventory.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewInventory.setAdapter(new InventoryAdapter(getActivity(), inventoryList));
        return view;
    }


}
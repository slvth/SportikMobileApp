package com.example.sportikmobileapp.database.inventory;

import java.io.Serializable;

public class InventoryModel implements Serializable {
    private int inventory_id;
    private TypeInventoryModel type;
    private ModelInventoryModel model;
    private float cost;

    private int inventory_number; //инвентарный номер
    private int count; //в наличии

    public InventoryModel() {
    }

    public InventoryModel(int inventory_id, TypeInventoryModel type, ModelInventoryModel model, float cost, int inventory_number, int count) {
        this.inventory_id = inventory_id;
        this.type = type;
        this.model = model;
        this.cost = cost;
        this.inventory_number = inventory_number;
        this.count = count;
    }

    public int getInventory_id() {
        return inventory_id;
    }

    public void setInventory_id(int inventory_id) {
        this.inventory_id = inventory_id;
    }

    public TypeInventoryModel getType() {
        return type;
    }

    public void setType(TypeInventoryModel type) {
        this.type = type;
    }

    public ModelInventoryModel getModel() {
        return model;
    }

    public void setModel(ModelInventoryModel model) {
        this.model = model;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public int getInventory_number() {
        return inventory_number;
    }

    public void setInventory_number(int inventory_number) {
        this.inventory_number = inventory_number;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

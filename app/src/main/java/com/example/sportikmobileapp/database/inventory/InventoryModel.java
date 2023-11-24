package com.example.sportikmobileapp.database.inventory;

public class InventoryModel {

    private int inventory_id;
    private TypeInventoryModel type;
    private ModelInventoryModel model;
    private float cost;

    public InventoryModel() {
    }

    public InventoryModel(int inventory_id, TypeInventoryModel type, ModelInventoryModel model, float cost) {
        this.inventory_id = inventory_id;
        this.type = type;
        this.model = model;
        this.cost = cost;
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
}

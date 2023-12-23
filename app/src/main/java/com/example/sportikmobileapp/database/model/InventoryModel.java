package com.example.sportikmobileapp.database.model;

import java.io.Serializable;

public class InventoryModel implements Serializable {
    private int inventory_id;
    private int type_id;
    private int model_id;
    private float cost;
    private String pictureURL; //ссылка на картинку
    private String desc; //описание

    private int inventory_number; //инвентарный номер
    private int count; //в наличии

    private TypeInventoryModel type;
    private ModelInventoryModel model;
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

    public InventoryModel(int inventory_id, TypeInventoryModel type, ModelInventoryModel model, float cost, int inventory_number,  int count, String pictureURL, String desc) {
        this.inventory_id = inventory_id;
        this.cost = cost;
        this.pictureURL = pictureURL;
        this.desc = desc;
        this.inventory_number = inventory_number;
        this.count = count;
        this.type = type;
        this.model = model;
    }

    public int getInventory_id() {
        return inventory_id;
    }

    public void setInventory_id(int inventory_id) {
        this.inventory_id = inventory_id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public int getModel_id() {
        return model_id;
    }

    public void setModel_id(int model_id) {
        this.model_id = model_id;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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
}

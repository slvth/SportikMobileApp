package com.example.sportikmobileapp.database.model;

import java.io.Serializable;

public class TypeInventoryModel implements Serializable {
    private int typeId;
    private String name;

    public TypeInventoryModel() {
    }

    public TypeInventoryModel(int typeId, String name) {
        this.typeId = typeId;
        this.name = name;
    }

    public int getType_id() {
        return typeId;
    }

    public void setType_id(int typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

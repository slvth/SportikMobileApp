package com.example.sportikmobileapp.database.inventory;

public class ModelInventoryModel {
    private int modelId;
    private String name;

    public ModelInventoryModel() {
    }

    public ModelInventoryModel(int modelId, String name) {
        this.modelId = modelId;
        this.name = name;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

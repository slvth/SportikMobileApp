package com.example.sportikmobileapp.database.booking;

import com.example.sportikmobileapp.database.inventory.InventoryModel;

import java.io.Serializable;

//Состав заявки
public class BookingDetailModel implements Serializable {
    private int bookingDetailId; //ID состава заявки
    private int bookingId; //ID заявки
    private int inventoryId; //ID заявки
    private int count;
    private InventoryModel inventory; //Инвентарь


    public BookingDetailModel() {
    }

    public BookingDetailModel(int inventoryId, int count, InventoryModel inventory) {
        this.inventoryId = inventoryId;
        this.count = count;
        this.inventory = inventory;
    }

    public BookingDetailModel(int bookingDetailId, int bookingId, InventoryModel inventory, int count) {
        this.bookingDetailId = bookingDetailId;
        this.bookingId = bookingId;
        this.inventory = inventory;
        this.count = count;
    }

    public BookingDetailModel(int bookingDetailId, int bookingId, int inventoryId, int count) {
        this.bookingDetailId = bookingDetailId;
        this.bookingId = bookingId;
        this.inventoryId = inventoryId;
        this.count = count;
    }

    public int getBookingDetailId() {
        return bookingDetailId;
    }

    public void setBookingDetailId(int bookingDetailId) {
        this.bookingDetailId = bookingDetailId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public InventoryModel getInventory() {
        return inventory;
    }

    public void setInventory(InventoryModel inventory) {
        this.inventory = inventory;
    }
}

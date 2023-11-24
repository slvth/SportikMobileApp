package com.example.sportikmobileapp.database.booking;

import com.example.sportikmobileapp.database.EmployeeModel;
import com.example.sportikmobileapp.database.UserModel;
import com.example.sportikmobileapp.database.inventory.ModelInventoryModel;
import com.example.sportikmobileapp.database.inventory.TypeInventoryModel;

//таблица Заявки
public class BookingModel {

    private int booking_id;
    private String dateBooking; //дата подачи заявки
    private String dateStart;
    private String dateEnd;
    private UserModel userModel; //
    private String status;
    private EmployeeModel employeeModel; //сотрудник

    private float total;

    public BookingModel() {
    }

    public BookingModel(int booking_id, String dateBooking, String dateStart, String dateEnd, UserModel userModel, String status, EmployeeModel employeeModel, float total) {
        this.booking_id = booking_id;
        this.dateBooking = dateBooking;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.userModel = userModel;
        this.status = status;
        this.employeeModel = employeeModel;
        this.total = total;
    }

    public BookingModel(int booking_id, String dateStart, String dateEnd, String status) {
        this.booking_id = booking_id;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.status = status;
    }

    public int getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(int booking_id) {
        this.booking_id = booking_id;
    }

    public String getDateBooking() {
        return dateBooking;
    }

    public void setDateBooking(String dateBooking) {
        this.dateBooking = dateBooking;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public EmployeeModel getEmployeeModel() {
        return employeeModel;
    }

    public void setEmployeeModel(EmployeeModel employeeModel) {
        this.employeeModel = employeeModel;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}

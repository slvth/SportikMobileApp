package com.example.sportikmobileapp.database.model;

import com.example.sportikmobileapp.database.model.BookingDetailModel;
import com.example.sportikmobileapp.database.model.UserModel;

import java.io.Serializable;
import java.util.ArrayList;

//таблица Заявки
public class BookingModel  implements Serializable {

    private int booking_id;
    private String dateBooking; //дата подачи заявки
    private String dateStart;
    private String dateEnd;

    private int user_id;

    private int employee_id;

    private float total;

    private int status_id;
    private String status; //статус

    private UserModel userModel; //пользователь

    private ArrayList<BookingDetailModel> details; //Состав заявки


    public BookingModel() {
    }

    public BookingModel(int booking_id, String dateStart, String dateEnd, String status) {
        this.booking_id = booking_id;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.status = status;
    }

    public BookingModel(int booking_id, String dateBooking, String dateStart, String dateEnd, int user_id, int employee_id, float total, String status, ArrayList<BookingDetailModel> details) {
        this.booking_id = booking_id;
        this.dateBooking = dateBooking;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.user_id = user_id;
        this.employee_id = employee_id;
        this.total = total;
        this.status = status;
        this.details = details;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserModel getUserModel() {
        return userModel;
    }

    public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

    public ArrayList<BookingDetailModel> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<BookingDetailModel> details) {
        this.details = details;
    }
}

package com.videostore.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by max on 24.09.2016.
 */
public class Order {
    private int orderId;
    private int userId;
    private BigDecimal totalCost;
    private String name;
    private String address;
    private String telephone;
    private Date orderDate;
    private ArrayList<Film> orderedFilms;

    public Order() {
        this.totalCost = new BigDecimal(0);
        this.orderedFilms = new ArrayList<>();
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public void addFilm(Film film){
        orderedFilms.add(film);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public ArrayList<Film> getOrderedFilms() {
        return orderedFilms;
    }

    public void setOrderedFilms(ArrayList<Film> orderedFilms) {
        this.orderedFilms = orderedFilms;
    }

    public Double getTotalCost(){
        totalCost = new BigDecimal(0);
        for(Film f : orderedFilms){
            totalCost = totalCost.add(new BigDecimal(f.getPrice()).setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        return totalCost.doubleValue();
    }
}

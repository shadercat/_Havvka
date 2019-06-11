package com.shadercat.havvka;

public class Order {
    private int id;
    private String status;
    private String date;
    private String time;
    private double price;

    public Order(int id, String status, String date, double price) {
        this.id = id;
        this.date = date;
        this.status = status;
        this.price = price;
    }

    public Order(int id, String status, String date, String time, double price){
        this(id,status,date,price);
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

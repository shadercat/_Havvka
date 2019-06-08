package com.shadercat.havvka;

public class FavouriteSet {
    private int id;
    private String name;
    private double price;

    public FavouriteSet(int id, String name, double price) {
        this.price = price;
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return this.price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

package com.shadercat.havvka;

import java.io.Serializable;

public class Item implements Serializable {

    private int ID;
    private String name;
    private String smallDescr;
    private String bigDescr;
    private String ingridients;
    private double price;
    private int image;
    private int rating;

    public Item(String name, String smallDescr, String bigDescr, int image) {
        this(name, smallDescr, bigDescr, image, 1, " ", 0f, 1);
    }

    public Item(String name, String smallDescr, String bigDescr, int image, int id, String ingridients, double price, int rating) {
        this.name = name;
        this.smallDescr = smallDescr;
        this.bigDescr = bigDescr;
        this.image = image;
        this.ID = id;
        this.ingridients = ingridients;
        this.price = price;
        this.rating = rating;
    }

    public String GetName() {
        return name;
    }

    public void SetName(String name) {
        this.name = name;
    }

    public String GetSmallDescr() {
        return smallDescr;
    }

    public void SetSmallDescr(String smallDescr) {
        this.smallDescr = smallDescr;
    }

    public String GetBigDescr() {
        return bigDescr;
    }

    public void SetBigDescr(String bigDescr) {
        this.bigDescr = bigDescr;
    }

    public int GetImage() {
        return image;
    }

    public void SetImage(int image) {
        this.image = image;
    }

    public double GetPrice() {
        return this.price;
    }

    public void SetPrice(double price) {
        this.price = price;
    }

    public int GetID() {
        return this.ID;
    }

    public void SetID(int id) {
        this.ID = id;
    }

    public String GetIngridients() {
        return this.ingridients;
    }

    public void SetIngridients(String ingridients) {
        this.ingridients = ingridients;
    }

    public int GetRating() {
        return rating;
    }

    public void SetRating(int rating) {
        this.rating = rating;
    }

}

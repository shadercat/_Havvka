package com.shadercat.havvka;

import android.util.Log;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

public class Item implements Serializable {

    private int ID;
    private String name;
    private String smallDescr;
    private String bigDescr;
    private String ingridients;
    private double price;
    private int rating;
    private URL url;

    public Item(int id, String name, String smallDescr, String bigDescr, String ingridients, double price, int rating, String url) {
        this.ID = id;
        this.name = name;
        this.smallDescr = smallDescr;
        this.bigDescr = bigDescr;
        this.ingridients = ingridients;
        this.price = price;
        this.rating = rating;
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            Log.e("initialize", e.getMessage());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSmallDescr() {
        return smallDescr;
    }

    public void setSmallDescr(String smallDescr) {
        this.smallDescr = smallDescr;
    }

    public String getBigDescr() {
        return bigDescr;
    }

    public void setBigDescr(String bigDescr) {
        this.bigDescr = bigDescr;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getID() {
        return this.ID;
    }

    public void setID(int id) {
        this.ID = id;
    }

    public String getIngridients() {
        return this.ingridients;
    }

    public void setIngridients(String ingridients) {
        this.ingridients = ingridients;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public URL getUrl() {
        return url;
    }
}

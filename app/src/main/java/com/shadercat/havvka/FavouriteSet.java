package com.shadercat.havvka;

import java.util.List;

public class FavouriteSet {
    private int id;
    private String name;
    private int count;

    public String getName(){
        return  this.name;
    }

    public  void setName(String name){
        this.name = name;
    }

    public int getCount(){
        return this.count;
    }
}

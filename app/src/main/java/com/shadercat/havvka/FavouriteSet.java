package com.shadercat.havvka;

public class FavouriteSet {
    private int id;
    private String name;
    private int count;
    private int serverId;

    public FavouriteSet(int id, String name, int count, int serverId) {
        this.count = count;
        this.id = id;
        this.name = name;
        this.serverId = serverId;
    }

    public FavouriteSet(int id, String name, int count) {
        this(id, name, count, 0);
    }

    public FavouriteSet(String name, int count) {
        this(0, name, count, 0);
    }

    public FavouriteSet() {
        this(0, "", 0, 0);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return this.count;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getServerId() {
        return serverId;
    }
}

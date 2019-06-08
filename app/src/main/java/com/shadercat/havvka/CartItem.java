package com.shadercat.havvka;

public class CartItem {
    private Item item;
    private int quantity;
    private double price;

    public CartItem(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
        this.price = item.getPrice() * quantity;
    }

    public boolean Equals(Item item) {
        return (this.item.getID() == item.getID());
    }

    public void AddQuantity(int quantity) {
        this.quantity += quantity;
        this.price = this.item.getPrice() * this.quantity;
    }

    public Item getItem() {
        return this.item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.price = quantity * item.getPrice();
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

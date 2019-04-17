package com.shadercat.havvka;

public class CartItem {
    public Item item;
    public int quantity;
    private double price;

    public CartItem(Item item, int quantity)
    {
        this.item = item;
        this.quantity = quantity;
        this.price = item.GetPrice() * quantity;
    }

    public boolean Equals(Item item)
    {
        return (this.item.GetID() == item.GetID());
    }

    public void AddQuantity(int quantity)
    {
        this.quantity += quantity;
        this.price = this.item.GetPrice() * this.quantity;
    }
}

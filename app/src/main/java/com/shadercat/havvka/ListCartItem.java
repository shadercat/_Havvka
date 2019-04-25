package com.shadercat.havvka;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ListCartItem {
    public static List<CartItem> list = new ArrayList<>();
    private static CartItem lastItemAction;
    private static boolean isAdded = true;

    public List<CartItem> GetList() {
        return list;
    }

    public static void AddCartItem(CartItem item) {
        lastItemAction = new CartItem(item.getItem(), item.getQuantity());
        boolean flag = true;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getItem().GetID() == item.getItem().GetID()) {
                list.get(i).AddQuantity(item.getQuantity());
                flag = false;
            }
        }
        if (flag) {
            list.add(item);
        }
        isAdded = true;
    }

    public static void RemoveCartItem(CartItem item) {
        lastItemAction = new CartItem(item.getItem(), item.getQuantity());
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getItem().GetID() == item.getItem().GetID()) {
                list.remove(i);
                return;
            }
        }
        isAdded = false;
    }

    public static void RemoveCartItem(int index) {
        lastItemAction = new CartItem(list.get(index).getItem(), list.get(index).getQuantity());
        list.remove(index);
        isAdded = false;
    }

    public static double GetSumPrice() {
        double sum = 0D;
        for (CartItem it : list) {
            sum += it.getPrice();
        }
        return sum;
    }

    public static void RemoveAction() {
        if (lastItemAction != null) {
            if (isAdded) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getItem().GetID() == lastItemAction.getItem().GetID()) {
                        int q = list.get(i).getQuantity() - lastItemAction.getQuantity();
                        if (q <= 0) {
                            list.remove(i);
                            return;
                        } else {
                            list.get(i).setQuantity(q);
                        }
                    }
                }
            } else {
                AddCartItem(lastItemAction);
            }
        }
    }
}

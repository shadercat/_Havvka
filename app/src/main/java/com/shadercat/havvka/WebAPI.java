package com.shadercat.havvka;

import java.util.ArrayList;
import java.util.List;

public class WebAPI {
    public static List<Item> GetItems()
    {
        List<Item> list = new ArrayList<>();
        for(int i = 0; i < 101; i++)
        {
            Item item = new Item("Name of Food " + i, "Small descrition","Big Description", R.drawable.food_test);
            item.SetID(i);
            item.SetIngridients("Ingridients");
            list.add(item);
        }
        return list;
    }
}

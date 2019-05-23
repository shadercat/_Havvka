package com.shadercat.havvka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class CartItemAdapter extends ArrayAdapter<CartItem> {

    private LayoutInflater inflater;
    private int layout;
    private List<CartItem> cartItems;

    public CartItemAdapter(Context context, int resource, List<CartItem> cartItems) {
        super(context, resource, cartItems);
        this.layout = resource;
        this.cartItems = cartItems;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CartItem itemList = cartItems.get(position);

        viewHolder.imageView.setImageBitmap(itemList.getItem().getImg());
        viewHolder.nameView.setText(itemList.getItem().GetName());
        viewHolder.quantityView.setText(String.valueOf(itemList.getQuantity()));
        viewHolder.priceView.setText(String.format(Locale.getDefault(), "%.2f", itemList.getPrice()));

        return convertView;
    }

    private class ViewHolder {
        final ImageView imageView;
        final TextView nameView, quantityView, priceView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.image_cartItem);
            nameView = (TextView) view.findViewById(R.id.name_cartItem);
            quantityView = (TextView) view.findViewById(R.id.quantity_cartItem);
            priceView = (TextView) view.findViewById(R.id.price_cartItem);
        }

    }

}

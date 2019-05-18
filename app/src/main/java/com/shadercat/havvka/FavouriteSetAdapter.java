package com.shadercat.havvka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class FavouriteSetAdapter extends ArrayAdapter<FavouriteSet> {
    private LayoutInflater inflater;
    private int layout;
    private List<FavouriteSet> favouriteSets;

    public FavouriteSetAdapter(Context context, int resource, List<FavouriteSet> favouriteSets) {
        super(context, resource, favouriteSets);
        this.layout = resource;
        this.favouriteSets = favouriteSets;
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

        FavouriteSet itemList = favouriteSets.get(position);
        viewHolder.nameView.setText(itemList.getName());
        viewHolder.quantityView.setText(String.valueOf(itemList.getCount()));

        return convertView;
    }

    private class ViewHolder {
        final TextView nameView, quantityView;

        public ViewHolder(View view) {
            nameView = (TextView) view.findViewById(R.id.name_setsItem);
            quantityView = (TextView) view.findViewById(R.id.count_setsItem);
        }

    }
}

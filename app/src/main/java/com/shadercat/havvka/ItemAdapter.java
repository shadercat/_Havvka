package com.shadercat.havvka;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

// Adapter for ListView (schema: picture, big text, small text)
public class ItemAdapter extends ArrayAdapter<Item> {

    private LayoutInflater inflater;
    private int layout;
    private List<Item> food;

    public ItemAdapter(Context context, int resource, List<Item> food) {
        super(context, resource, food);
        this.food = food;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    //optimized getView
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Item itemList = food.get(position);

        viewHolder.imageView.setImageResource(itemList.GetImage());
        viewHolder.nameView.setText(itemList.GetName());
        viewHolder.smallDescrView.setText(itemList.GetSmallDescr());

        return convertView;
    }

    private class ViewHolder {
        final ImageView imageView;
        final TextView nameView, smallDescrView;

        public ViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.image);
            nameView = (TextView) view.findViewById(R.id.name);
            smallDescrView = (TextView) view.findViewById(R.id.smallDescription);
        }

    }
}

package com.shadercat.havvka;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Item> items;
    private ClickListeners mListener;

    ItemListAdapter(Context context, List<Item> items) {
        this.items = items;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ItemListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemListAdapter.ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.imageView.setImageResource(item.GetImage());
        holder.nameView.setText(item.GetName());
        holder.smallDescrView.setText(item.GetSmallDescr());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnClickListeners(ClickListeners listeners) {
        mListener = listeners;
    }

    interface ClickListeners {
        void OnClick(int position);

        void OnClick2(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView imageView;
        final TextView nameView, smallDescrView;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            imageView = (ImageView) view.findViewById(R.id.image);
            imageView.setOnClickListener(this);
            nameView = (TextView) view.findViewById(R.id.name);
            smallDescrView = (TextView) view.findViewById(R.id.smallDescription);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                switch (v.getId()) {
                    case R.id.listItem:
                        mListener.OnClick(getLayoutPosition());
                        break;
                    case R.id.image:
                        mListener.OnClick2(getLayoutPosition());
                        break;
                }
            }
        }
    }
}

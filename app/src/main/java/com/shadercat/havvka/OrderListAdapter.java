package com.shadercat.havvka;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private List<Order> orders;
    private ClickListener mListener;

    OrderListAdapter(Context context, List<Order> orders) {
        this.orders = orders;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(inflater.inflate(R.layout.order_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        Order order = orders.get(i);
        ViewHolder vh = (ViewHolder) holder;
        vh.id.setText(String.valueOf(order.getId()));
        vh.status.setText(order.getStatus());
        vh.price.setText(String.format(Locale.getDefault(), "%.2f", order.getPrice()));
        vh.date.setText(order.getDate());
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public void setListener(ClickListener listener) {
        this.mListener = listener;
    }

    public void setItems(List<Order> orders) {
        this.orders = orders;
    }

    interface ClickListener {
        void itemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView id;
        final TextView date;
        final TextView status;
        final TextView price;

        public ViewHolder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.id_order);
            date = (TextView) view.findViewById(R.id.date_order);
            status = (TextView) view.findViewById(R.id.status_order);
            price = (TextView) view.findViewById(R.id.price_order);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.itemClick(getLayoutPosition());
                    }
                }
            });
        }
    }
}

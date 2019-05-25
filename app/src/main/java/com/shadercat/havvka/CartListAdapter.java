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
import java.util.Locale;

public class CartListAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnClickListeners mListener;
    private List<CartItem> items;
    private LayoutInflater inflater;

    CartListAdapter(Context context, List<CartItem> list){
        this.items = list;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.cart_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        CartItem item = items.get(holder.getAdapterPosition());
        ViewHolder vh = (ViewHolder) holder;
        vh.imageView.setImageBitmap(item.getItem().getImg());
        vh.nameView.setText(item.getItem().GetName());
        vh.quantityView.setText(String.valueOf(item.getQuantity()));
        vh.priceView.setText(String.format(Locale.getDefault(), "%.2f", item.getPrice()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnClickListeners(OnClickListeners listeners){
        mListener = listeners;
    }

    public void setItems(List<CartItem> items){
        this.items = items;
    }

    interface OnClickListeners{
        void itemClick(int position);
        void moreClick(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final ImageView imageView, ic_more;
        final TextView nameView, quantityView, priceView;

        ViewHolder(View view){
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image_cartItem);
            nameView = (TextView) view.findViewById(R.id.name_cartItem);
            quantityView = (TextView) view.findViewById(R.id.quantity_cartItem);
            priceView = (TextView) view.findViewById(R.id.price_cartItem);
            ic_more = (ImageView) view.findViewById(R.id.setting_cartItem);
            view.setOnClickListener(this);
            ic_more.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                switch (v.getId()){
                    case R.id.setting_cartItem:
                        mListener.moreClick(getLayoutPosition());
                        break;
                    case R.id.cart_item_root:
                        mListener.itemClick(getLayoutPosition());
                        break;
                }
            }
        }
    }
}

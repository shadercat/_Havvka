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

public class FavouriteListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int STANDARD_LAYOUT = 7;
    private static final int ADD_NEW_LAYOUT = 572;

    private ClickListeners mListener;
    private LayoutInflater inflater;
    private List<FavouriteSet> sets;


    FavouriteListAdapter(Context context, List<FavouriteSet> sets){
        this.sets = sets;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        switch (i){
            case STANDARD_LAYOUT:
                view = inflater.inflate(R.layout.sets_item, viewGroup, false);
                return new ViewHolder(view);
            case ADD_NEW_LAYOUT:
                view = inflater.inflate(R.layout.endofset_item, viewGroup, false);
                return new ViewHolder2(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == sets.size()){
            ViewHolder2 vh2 = (ViewHolder2) holder;
            vh2.image.setImageResource(R.drawable.ic_add_circle_outline_black_24dp);
        } else {
            FavouriteSet set = sets.get(position);
            ViewHolder vh = (ViewHolder) holder;
            vh.name.setText(set.getName());
            vh.count.setText(set.getCount());
        }
    }

    @Override
    public int getItemCount() {
        return sets.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position != sets.size())? STANDARD_LAYOUT : ADD_NEW_LAYOUT;
    }

    public void setItems(List<FavouriteSet> list){
        this.sets = list;
    }

    public void setOnClickListeners(ClickListeners listeners){
        mListener = listeners;
    }

    interface ClickListeners{
        void onClick(int position);
        void onClickAdd();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView count;
        public ViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            name = (TextView) view.findViewById(R.id.set_name);
            count = (TextView) view.findViewById(R.id.set_quantity);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onClick(getLayoutPosition());
            }
        }
    }
    class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;

        public ViewHolder2(View view){
            super(view);
            view.setOnClickListener(this);
            image = (ImageView) view.findViewById(R.id.image_sets);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onClickAdd();
            }
        }
    }
}

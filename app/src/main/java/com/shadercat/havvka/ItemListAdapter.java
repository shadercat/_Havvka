package com.shadercat.havvka;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int STANDARD_LAYOUT = 1;
    private static final int BORDER_LAYOUT = 2;
    private LayoutInflater inflater;
    private List<Item> items;
    private ClickListeners mListener;
    private ImageView lastCircle;

    ItemListAdapter(Context context, List<Item> items) {
        this.items = items;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        switch (i) {
            case STANDARD_LAYOUT:
                view = inflater.inflate(R.layout.list_item, viewGroup, false);
                ViewHolder vh = new ViewHolder(view);
                return vh;
            case BORDER_LAYOUT:
                view = inflater.inflate(R.layout.last_item, viewGroup, false);
                ViewHolder2 vh2 = new ViewHolder2(view);
                return vh2;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position != items.size()) {
            Item item = items.get(holder.getAdapterPosition());
            ViewHolder vh = (ViewHolder) holder;
            if (DataAdapter.imgCache.containsKey(item.getID())) {
                Bitmap bm = DataAdapter.imgCache.get(item.getID());
                vh.imageView.setImageBitmap(bm);
            } else {
                DataAdapter.imgCache.put(item.getID(), Bitmap.createBitmap(100, 100,
                        Bitmap.Config.ARGB_8888));
                if (mListener != null) {
                    mListener.LoadingImage(vh.imageView, holder.getAdapterPosition());
                }
                vh.imageView.setImageResource(R.drawable.food_test);
            }
            vh.nameView.setText(item.getName());
            vh.smallDescrView.setText(item.getSmallDescr());
        }
    }

    @Override
    public int getItemCount() {
        if(items.size() != 0){
            return items.size();
        } else {
            return 1;
        }
    }

    public void setOnClickListeners(ClickListeners listeners) {
        mListener = listeners;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == items.size()) ? BORDER_LAYOUT : STANDARD_LAYOUT;
    }

    public void StopAnim() {
        if (mListener != null && lastCircle != null) {
            mListener.StopAnim(lastCircle);
        }
    }

    public void StartAnim() {
        if (mListener != null && lastCircle != null) {
            mListener.StartAnim(lastCircle);
        }
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    interface ClickListeners {
        void OnClick(int position);

        void StartAnim(View view);

        void StopAnim(View view);

        void LoadingImage(View view, int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView imageView;
        final TextView nameView, smallDescrView;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            imageView = (ImageView) view.findViewById(R.id.image);
            nameView = (TextView) view.findViewById(R.id.name);
            smallDescrView = (TextView) view.findViewById(R.id.smallDescription);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.OnClick(getLayoutPosition());
            }
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView imageView;

        public ViewHolder2(View view) {
            super(view);
            view.setOnClickListener(this);
            imageView = (ImageView) view.findViewById(R.id.imageLoad);
            lastCircle = imageView;
            if (mListener != null) {
                mListener.StartAnim(imageView);
            }
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.StartAnim(imageView);
            }
        }
    }
}

package com.shadercat.havvka;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class PropositionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private List<Proposition> propositions;
    private ClickListener mListener;


    PropositionListAdapter(Context context, List<Proposition> propositions){
        inflater = LayoutInflater.from(context);
        this.propositions = propositions;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(inflater.inflate(R.layout.proposition_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        Proposition prop = propositions.get(i);
        ViewHolder vh = (ViewHolder) holder;
        vh.textView.setText(prop.getName());
    }

    @Override
    public int getItemCount() {
        return propositions.size();
    }

    public void setListener(ClickListener listener){
        mListener = listener;
    }
    public void setItems(List<Proposition> propositions){
        this.propositions = propositions;
    }
    interface ClickListener{
        void clickItem(int position);
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView textView;
        public ViewHolder(View view){
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.clickItem(getLayoutPosition());
                    }
                }
            });
            textView = (TextView) view.findViewById(R.id.proposition_name_item);
        }
    }
}

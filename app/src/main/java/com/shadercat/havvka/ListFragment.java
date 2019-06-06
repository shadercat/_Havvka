package com.shadercat.havvka;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;


public class ListFragment extends Fragment {

    private ListFragmentInteractionListener mListener;
    List<Item> items = new ArrayList<>();
    ItemListAdapter adapter;
    RecyclerView listView;
    Context context;
    Animation rotateAnim;
    Animation stop;

    public ListFragment() {
        // Required empty public constructor
    }


    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        listView = (RecyclerView) view.findViewById(R.id.itemslist);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ListFragmentInteractionListener) {
            mListener = (ListFragmentInteractionListener) context;
            this.context = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rotateAnim = AnimationUtils.loadAnimation(context, R.anim.rotate_logo);
        stop = AnimationUtils.loadAnimation(context, R.anim.shake_logo);
        adapter = new ItemListAdapter(context, items);
        DividerItemDecoration itemDecor = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        listView.addItemDecoration(itemDecor);
        listView.setAdapter(adapter);
        adapter.setOnClickListeners(new ItemListAdapter.ClickListeners() {
            @Override
            public void OnClick(int position) {
                Intent product_info = new Intent(context, InformationActivity.class);
                product_info.putExtra(Item.class.getSimpleName(), items.get(position).GetID());
                startActivity(product_info);
            }

            @Override
            public void StartAnim(View view) {
                if (rotateAnim != null) {
                    ((ImageView) view).startAnimation(rotateAnim);
                }
            }

            @Override
            public void StopAnim(View view) {
                if (stop != null) {
                    ((ImageView) view).startAnimation(stop);
                }
            }

            @Override
            public void LoadingImage(View view, final int pos) {
                new LoadImage((ImageView) view, new IPermissionForSet() {
                    @Override
                    public boolean isInView() {
                        return isVisibleItem(pos);
                    }
                }).execute(items.get(pos));
            }
        });
        new DataDownload().execute();
    }
    public boolean isVisibleItem(int i){
        LinearLayoutManager layoutManager = ((LinearLayoutManager)listView.getLayoutManager());
        int first = layoutManager.findFirstVisibleItemPosition();
        int last = layoutManager.findLastVisibleItemPosition();
        return (first <= i && i <= last);
    }
    public interface ListFragmentInteractionListener {
        void ListFragmentInteraction(Uri link);
    }

    class DataDownload extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adapter.StartAnim();
        }

        @Override
        protected Void doInBackground(Void... aVoid) {
            items = DataAdapter.GetProductList(context);
            adapter.setItems(items);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.StopAnim();
            adapter.notifyDataSetChanged();
        }
    }
}

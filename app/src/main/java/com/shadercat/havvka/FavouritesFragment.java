package com.shadercat.havvka;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment {

    private FavouriteFragmentInteractionListener mListener;
    List<FavouriteSet> sets = new ArrayList<>();
    FavouriteListAdapter adapter;
    RecyclerView recyclerView;
    Context context;


    public FavouritesFragment() {
        // Required empty public constructor
    }

    public static FavouritesFragment newInstance() {
        return new FavouritesFragment();
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
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.favouritesList);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FavouriteFragmentInteractionListener) {
            mListener = (FavouriteFragmentInteractionListener) context;
            this.context = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ListFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new FavouriteListAdapter(getContext(), sets);
        DividerItemDecoration itemDecor = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListeners(new FavouriteListAdapter.ClickListeners() {
            @Override
            public void onClick(int position) {

            }

            @Override
            public void onClickAdd() {
                AddDialog();
            }
        });
        new DataDownload().execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        new DataDownload().execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void AddDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View inflater = this.getLayoutInflater().inflate(R.layout.dialog_add_favourite_set, null);
        final EditText text = (EditText) inflater.findViewById(R.id.getNameDialog);
        builder.setView(inflater)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = text.getText().toString();
                        if(!name.isEmpty()){
                            FavouriteSet fs = new FavouriteSet(name,0);
                            DataAdapter.SaveFavSet(getContext(),fs,true);
                            Toast.makeText(getContext(),getString(R.string.addedNewFavSet),Toast.LENGTH_SHORT).show();
                            new DataDownload().execute();
                        } else {
                            Toast.makeText(getContext(),getString(R.string.notEmptyName),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public interface FavouriteFragmentInteractionListener {
        void FavouriteFragmentInteraction(Uri link);
    }
    class DataDownload extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... aVoid) {
            sets = DataAdapter.GetFavouriteData(getContext());
            adapter.setItems(sets);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }
    }
}

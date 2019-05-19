package com.shadercat.havvka;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


public class ListFragment extends Fragment {

    private ListFragmentInteractionListener mListener;
    RecyclerView listView;
    Context context;

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
        mListener.ListFragmentInteraction(Uri.parse("data:1"));
    }

    public void addAdapter(ItemListAdapter adapter) {
        listView.setAdapter(adapter);
        adapter.setOnClickListeners(new ItemListAdapter.ClickListeners() {
            @Override
            public void OnClick(int position) {
                mListener.ListFragmentInteraction(Uri.parse("itemClick:" + position));
            }
        });
    }

    public interface ListFragmentInteractionListener {
        void ListFragmentInteraction(Uri link);
    }
}

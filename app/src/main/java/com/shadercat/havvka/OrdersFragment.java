package com.shadercat.havvka;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


public class OrdersFragment extends Fragment {

    List<Order> orders = new ArrayList<>();
    RecyclerView recyclerView;
    OrderListAdapter adapter;
    mWorkingThread parallelThread;
    Handler mUIHandler = new Handler();
    Runnable getOrderTask;

    private OrdersFragmentInteractionListener mListener;

    public OrdersFragment() {
    }

    public static OrdersFragment newInstance() {
        return new OrdersFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.orderList);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OrdersFragmentInteractionListener) {
            mListener = (OrdersFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ListFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        parallelThread = new mWorkingThread("orderfragment");
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        adapter = new OrderListAdapter(getContext(),orders);
        adapter.setListener(new OrderListAdapter.ClickListener() {
            @Override
            public void itemClick(int position) {

            }
        });
        recyclerView.setAdapter(adapter);
        getOrderTask = new Runnable() {
            @Override
            public void run() {
                orders = DataAdapter.GetOrderList(getContext());
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setItems(orders);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        parallelThread.start();
        parallelThread.prepareHandler();
        parallelThread.postTask(getOrderTask);
    }

    @Override
    public void onDestroy() {
        if(parallelThread != null){
            parallelThread.quit();
        }
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OrdersFragmentInteractionListener {
        void OrdersFragmentInteraction(Uri link);
    }
}

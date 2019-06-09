package com.shadercat.havvka;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderInformationActivity extends AppCompatActivity {
    TextView orderId;
    TextView orderDate;
    TextView orderStatus;
    TextView orderCost;
    ImageView backArrow;
    RecyclerView itemList;
    CartListAdapter adapter;
    List<CartItem> items = new ArrayList<>();
    mWorkingThread parallelThread;
    Handler mUIHandler = new Handler();
    Order order;
    int idOrder = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_information);
        orderId = (TextView) findViewById(R.id.order_id_info);
        orderDate = (TextView) findViewById(R.id.order_date_info);
        orderCost = (TextView) findViewById(R.id.order_cost_info);
        orderStatus = (TextView) findViewById(R.id.order_status_info);
        itemList = (RecyclerView) findViewById(R.id.orderItemsList);
        backArrow = (ImageView) findViewById(R.id.back_arrow_order_info);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        final DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemList.addItemDecoration(itemDecor);
        adapter = new CartListAdapter(this, items);
        adapter.setOnClickListeners(new CartListAdapter.OnClickListeners() {
            @Override
            public void itemClick(int position) {
                Intent intent = new Intent(getApplicationContext(), InformationActivity.class);
                intent.putExtra(Item.class.getSimpleName(), items.get(position).getItem().getID());
                startActivity(intent);
            }

            @Override
            public void moreClick(int position) {

            }

            @Override
            public void LoadingImage(View view, final int pos) {
                new LoadImage((ImageView) view, new IPermissionForSet() {
                    @Override
                    public boolean isInView() {
                        return isVisibleItem(pos);
                    }
                }).execute(items.get(pos).getItem());
            }
        });
        itemList.setAdapter(adapter);
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            idOrder = (int) arguments.getSerializable(Order.class.getSimpleName());
        }
        Runnable dataTask = new Runnable() {
            @Override
            public void run() {
                order = DataAdapter.GetOrderById(getApplicationContext(), idOrder);
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (order != null) {
                            orderId.setText(String.valueOf(order.getId()));
                            orderDate.setText(order.getDate());
                            orderCost.setText(String.format(Locale.getDefault(), "%.2f", order.getPrice()));
                            orderStatus.setText(order.getStatus());
                        }
                    }
                });
            }
        };
        parallelThread = new mWorkingThread("orderinformationactivity");
        parallelThread.start();
        parallelThread.prepareHandler();
        parallelThread.postTask(dataTask);

        Runnable itemDataTask = new Runnable() {
            @Override
            public void run() {
                items = DataAdapter.GetOrderItems(getApplicationContext(), idOrder);
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setItems(items);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        parallelThread.postTask(itemDataTask);
    }

    private boolean isVisibleItem(int i) {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) itemList.getLayoutManager());
        int first = layoutManager.findFirstVisibleItemPosition();
        int last = layoutManager.findLastVisibleItemPosition();
        return (first <= i && i <= last);
    }

    @Override
    protected void onDestroy() {
        if (parallelThread != null) {
            parallelThread.quit();
        }
        super.onDestroy();
    }
}

package com.shadercat.havvka;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ShowFavouriteSetItems extends AppCompatActivity {

    public static final String FAVSET_ID = "favsetid";

    TextView sum;
    Button add_to_cart;
    ImageView back_arrow;
    RecyclerView recyclerView;
    CartListAdapter adapter;
    List<CartItem> items = new ArrayList<>();
    mWorkingThread parallelThread;
    Handler mUiHandler = new Handler();
    Runnable getDataTask;
    int setId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_favourite_set_items);
        sum = findViewById(R.id.sum_FavouriteItems);
        add_to_cart = findViewById(R.id.add_btn_FavouriteItems);
        recyclerView = findViewById(R.id.favouritesItems);
        back_arrow = findViewById(R.id.back_arrow_favouriteItems);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        adapter = new CartListAdapter(this, items);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListeners(new CartListAdapter.OnClickListeners() {
            @Override
            public void itemClick(int position) {
                Intent product_info = new Intent(getApplicationContext(), InformationActivity.class);
                product_info.putExtra(Item.class.getSimpleName(), items.get(position).getItem().getID());
                startActivity(product_info);
            }

            @Override
            public void moreClick(int position) {
                Dialog(items.get(position));
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



        parallelThread = new mWorkingThread("showfavsetitems");
        parallelThread.start();
        parallelThread.prepareHandler();
        getDataTask = new Runnable() {
            @Override
            public void run() {
                items = DataAdapter.GetFavItems(getApplicationContext(),setId);
                mUiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setItems(items);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            setId = (int) bundle.getSerializable(FAVSET_ID);
            parallelThread.postTask(getDataTask);
        }

    }


    private void Dialog(final CartItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        View inflater = this.getLayoutInflater().inflate(R.layout.dialog_cart_item_edit, null);
        final NumberPicker picker = (NumberPicker) inflater.findViewById(R.id.numberPicker2);
        picker.setMaxValue(10);
        picker.setMinValue(1);
        picker.setValue(item.getQuantity());
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater)
                // Add action buttons
                .setPositiveButton(R.string.changeQuantity, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Runnable setItemData = new Runnable() {
                            @Override
                            public void run() {
                                DataAdapter.SetFavItemData(getApplicationContext(),setId,item.getItem().getID(),DataAdapter.SET_MODE_CHANGE,picker.getValue());
                            }
                        };
                        parallelThread.postTask(setItemData);
                        parallelThread.postTask(getDataTask);
                        dialog.cancel();
                    }
                })
                .setNeutralButton(R.string.deleteItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Runnable deleteItemTask = new Runnable() {
                            @Override
                            public void run() {
                                DataAdapter.SetFavItemData(getApplicationContext(),setId,item.getItem().getID(),DataAdapter.SET_MODE_DELETE,0);
                            }
                        };
                        parallelThread.postTask(deleteItemTask);
                        parallelThread.postTask(getDataTask);
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onDestroy() {
        if(parallelThread != null){
            parallelThread.quit();
        }
        super.onDestroy();
    }

    private boolean isVisibleItem(int i) {
        LinearLayoutManager layoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
        int first = layoutManager.findFirstVisibleItemPosition();
        int last = layoutManager.findLastVisibleItemPosition();
        return (first <= i && i <= last);
    }
}

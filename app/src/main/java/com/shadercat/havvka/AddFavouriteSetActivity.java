package com.shadercat.havvka;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.List;

public class AddFavouriteSetActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String ITEM_ID = "itemId";

    List<FavouriteSet> sets;
    FavouriteListAdapter adapter;
    RecyclerView list;
    ImageView arrow;
    int itemId = -1;
    Handler mUIHandler = new Handler();
    mWorkingThread parallelThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favourite_set);
        list = findViewById(R.id.favouritesToChooseList);
        arrow = findViewById(R.id.back_arrow_favouriteAdd);

        parallelThread = new mWorkingThread("threadAddFavSet");
        Runnable task = new Runnable() {
            @Override
            public void run() {
                sets = DataAdapter.GetFavouriteData(getApplicationContext());
                redownloadSets();
            }
        };

        parallelThread.start();
        parallelThread.prepareHandler();
        parallelThread.postTask(task);


        adapter = new FavouriteListAdapter(this, sets);
        list.setAdapter(adapter);
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            itemId = (int) arguments.getSerializable(ITEM_ID);
        }
        arrow.setOnClickListener(this);
        adapter.setOnClickListeners(new FavouriteListAdapter.ClickListeners() {
            @Override
            public void onClick(int position) {
                NumberPickerDialog(position);
            }

            @Override
            public void onClickAdd() {
                AddDialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }

    private void redownloadSets() {
        sets = DataAdapter.GetFavouriteData(this);
        adapter.setItems(sets);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        if (parallelThread != null) {
            parallelThread.quit();
        }
        super.onDestroy();
    }

    private void AddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflater = this.getLayoutInflater().inflate(R.layout.dialog_add_favourite_set, null);
        final EditText text = (EditText) inflater.findViewById(R.id.getNameDialog);
        builder.setView(inflater)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String name = text.getText().toString();
                        if (!name.isEmpty()) {
                            Runnable taskAdd = new Runnable() {
                                @Override
                                public void run() {
                                    FavouriteSet fs = new FavouriteSet(0, name, 0);
                                    DataAdapter.SaveFavSet(getApplicationContext(), fs, true);
                                    mUIHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), getString(R.string.addedNewFavSet), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            };
                            parallelThread.postTask(taskAdd);
                            redownloadSets();
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.notEmptyName), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void NumberPickerDialog(int pos) {
        final int position = pos;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        View inflater = this.getLayoutInflater().inflate(R.layout.dialog_quantity, null);
        final NumberPicker picker = (NumberPicker) inflater.findViewById(R.id.numberPicker);
        picker.setMaxValue(10);
        picker.setMinValue(1);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater)
                // Add action buttons
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Runnable taskAddItem = new Runnable() {
                            @Override
                            public void run() {
                                DataAdapter.AddItemToFavSet(getApplicationContext(), sets.get(position).getId(), itemId, picker.getValue());
                                mUIHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), getString(R.string.addedNewFavItem), Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                    }
                                });
                            }
                        };
                        parallelThread.postTask(taskAddItem);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

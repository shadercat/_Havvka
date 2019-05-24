package com.shadercat.havvka;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_favourite_set);
        list = findViewById(R.id.favouritesToChooseList);
        arrow = findViewById(R.id.back_arrow_favouriteAdd);
        arrow.setImageResource(R.drawable.ic_arrow_back_black_24dp);
        arrow.setOnClickListener(this);
        sets = DataAdapter.GetFavouriteData(this);
        adapter = new FavouriteListAdapter(this, sets);
        list.setAdapter(adapter);
        Bundle arguments = getIntent().getExtras();
        if(arguments != null){
            itemId = (int) arguments.getSerializable(ITEM_ID);
        }
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

    private void redownloadSets(){
        sets = DataAdapter.GetFavouriteData(this);
        adapter.setItems(sets);
        adapter.notifyDataSetChanged();
    }
    private void AddDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflater = this.getLayoutInflater().inflate(R.layout.dialog_add_favourite_set, null);
        final EditText text = (EditText) inflater.findViewById(R.id.getNameDialog);
        builder.setView(inflater)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = text.getText().toString();
                        if(!name.isEmpty()){
                            FavouriteSet fs = new FavouriteSet(name,0);
                            DataAdapter.SaveFavSet(getApplicationContext(),fs,true);
                            redownloadSets();
                        } else {
                            Toast.makeText(getApplicationContext(),getString(R.string.notEmptyName),Toast.LENGTH_SHORT).show();
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
                        DataAdapter.AddItemToFavSet(getApplicationContext(), sets.get(position).getId(), itemId, picker.getValue());
                        Toast.makeText(getApplicationContext(), getString(R.string.addedNewFavItem),Toast.LENGTH_SHORT).show();
                        onBackPressed();
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

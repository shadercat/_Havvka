package com.shadercat.havvka;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Locale;

public class InformationActivity extends AppCompatActivity {


    ImageView image;
    TextView name;
    TextView smallDscr;
    TextView bigDescr;
    TextView ingridients;
    TextView price;
    Button buy;
    Button addFavourites;
    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        image = (ImageView) findViewById(R.id.picture_InformationActivity);
        name = (TextView) findViewById(R.id.name_InformationActivity);
        smallDscr = (TextView) findViewById(R.id.smallDescr_InformationActivity);
        bigDescr = (TextView) findViewById(R.id.bigDescr_InformationActivity);
        ingridients = (TextView) findViewById(R.id.ingridients_InformationActivity);
        price = (TextView) findViewById(R.id.price_InformationActivity);
        buy = (Button) findViewById(R.id.btn_buy_InformationActivity);
        addFavourites = (Button) findViewById(R.id.btn_addFavourites_InformationActivity);
        findViewById(R.id.back_arrow_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            int id = (int) arguments.getSerializable(Item.class.getSimpleName());
            new SetInformation().execute(id);
        }


        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog();
            }
        });

        addFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addToFavSet = new Intent(getApplicationContext(), AddFavouriteSetActivity.class);
                addToFavSet.putExtra(AddFavouriteSetActivity.ITEM_ID, item.getID());
                startActivity(addToFavSet);
            }
        });
    }


    private void Dialog() {
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
                        CartItem ct = new CartItem(item, picker.getValue());
                        CartHelper.AddCartItem(ct);
                        ThematicSnackbar.SnackbarWithActionShow(getString(R.string.addedToCart),
                                getString(R.string.cancel),
                                snackbarOnClickListener,
                                buy,
                                getApplicationContext());
                        dialog.cancel();
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

    private View.OnClickListener snackbarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            CartHelper.RemoveAction();
        }
    };


    class SetInformation extends AsyncTask<Integer, Integer, Item> {

        @Override
        protected Item doInBackground(Integer... integers) {
            Item item1 = DataAdapter.itemCache.get(integers[0]);
            if (item == null) {
                DataAdapter.GetItemById(integers[0]);
            }
            return item1;
        }

        @Override
        protected void onPostExecute(Item aItem) {
            item = aItem;
            if (item != null) {
                if (DataAdapter.imgCache.containsKey(item.getID())) {
                    Bitmap bm = DataAdapter.imgCache.get(item.getID());
                    image.setImageBitmap(bm);
                } else {
                    DataAdapter.imgCache.put(item.getID(), Bitmap.createBitmap(100, 100,
                            Bitmap.Config.ARGB_8888));
                    new LoadImage(image, new IPermissionForSet() {
                        @Override
                        public boolean isInView() {
                            return true;
                        }
                    }).execute(item);
                    image.setImageResource(R.drawable.food_test);
                }
                name.setText(item.getName());
                smallDscr.setText(item.getSmallDescr());
                bigDescr.setText(item.getBigDescr());
                ingridients.setText(item.getIngridients());
                price.setText(String.format(Locale.getDefault(), "%.2f", item.getPrice()));
            }
            super.onPostExecute(aItem);
        }
    }
}

package com.shadercat.havvka;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        Bundle arguments = getIntent().getExtras();
        if(arguments != null)
        {
            item = (Item) arguments.getSerializable(Item.class.getSimpleName());

            image.setImageResource(item.GetImage());
            name.setText(item.GetName());
            smallDscr.setText(item.GetSmallDescr());
            bigDescr.setText(item.GetBigDescr());
            ingridients.setText(item.GetIngridients());
            price.setText(String.format(Locale.getDefault(),"%.2f",item.GetPrice()));

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

            }
        });
    }

    private void Dialog()
    {
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
                        ListCartItem.AddCartItem(ct);
                        //Toast.makeText(getApplicationContext(),getString(R.string.addedToCart), Toast.LENGTH_LONG).show();
                        SnackbarShow();
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
    private void SnackbarShow()
    {
        Snackbar mSnackbar = Snackbar.make(buy, getString(R.string.addedToCart), Snackbar.LENGTH_LONG)
                .setAction(R.string.cancel, snackbarOnClickListener)
                .setActionTextColor(getResources().getColor(R.color.colorBlue));
        View snackbarView = mSnackbar.getView();
        snackbarView.setBackgroundResource(R.color.colorPrimaryDark);
        TextView snackTextView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        snackTextView.setTextColor(getResources().getColor(R.color.colorWhite));
        mSnackbar.show();
    }
    private View.OnClickListener snackbarOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ListCartItem.RemoveAction();
        }
    };
}

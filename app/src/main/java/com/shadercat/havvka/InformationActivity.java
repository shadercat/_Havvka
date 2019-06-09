package com.shadercat.havvka;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class InformationActivity extends AppCompatActivity {


    ImageView image;
    TextView rating;
    TextView setRating;
    TextView name;
    TextView smallDscr;
    TextView bigDescr;
    TextView ingridients;
    TextView price;
    Button buy;
    Button addFavourites;
    RecyclerView propositionsView;
    PropositionListAdapter adapter;
    List<Proposition> propositions = new ArrayList<>();
    Item item;
    int itemId;
    mWorkingThread parallelThread;
    Handler mUIHandler = new Handler();

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
        propositionsView = (RecyclerView) findViewById(R.id.propositionlist);
        rating = (TextView) findViewById(R.id.ratingInformationActivity);
        setRating = (TextView) findViewById(R.id.setRating);
        adapter = new PropositionListAdapter(this, propositions);
        propositionsView.setAdapter(adapter);
        findViewById(R.id.back_arrow_info).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            itemId = (int) arguments.getSerializable(Item.class.getSimpleName());
        }

        parallelThread = new mWorkingThread("informationactivity");
        parallelThread.start();
        parallelThread.prepareHandler();
        Runnable infoTask = new Runnable() {
            @Override
            public void run() {
                item = DataAdapter.itemCache.get(itemId);
                if (item == null) {
                    item = DataAdapter.GetItemById(itemId);
                }
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
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
                            rating.setText(String.format(Locale.getDefault(), "%.2f", item.getRating()));
                        }
                    }
                });
            }
        };
        parallelThread.postTask(infoTask);
        Runnable propTask = new Runnable() {
            @Override
            public void run() {
                propositions = DataAdapter.GetPropositionsForItem(itemId);
                mUIHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setItems(propositions);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        parallelThread.postTask(propTask);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog();
            }
        });
        setRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RateDialog();
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

    @Override
    protected void onDestroy() {
        if (parallelThread != null) {
            parallelThread.quit();
        }
        super.onDestroy();
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

    private void RateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        View inflater = this.getLayoutInflater().inflate(R.layout.rating_alert, null);
        final RatingBar bar = (RatingBar) inflater.findViewById(R.id.ratingbar);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater)
                // Add action buttons
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Runnable rateTask = new Runnable() {
                            @Override
                            public void run() {
                                DataAdapter.SetRating(bar.getNumStars(), itemId);
                                mUIHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), getText(R.string.sendRating), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        };
                        parallelThread.postTask(rateTask);
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
}

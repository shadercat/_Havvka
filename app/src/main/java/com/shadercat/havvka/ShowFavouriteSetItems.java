package com.shadercat.havvka;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            int favid = (int) bundle.getSerializable(FAVSET_ID);
            new DownloadData().execute(favid);
        }
        adapter = new CartListAdapter(this,items);
        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListeners(new CartListAdapter.OnClickListeners() {
            @Override
            public void itemClick(int position) {

            }

            @Override
            public void moreClick(int position) {

            }
        });
    }
    class DownloadData extends AsyncTask<Integer,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            items = DataAdapter.GetFavItems(getApplicationContext(),integers[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getApplicationContext(),"That`s all",Toast.LENGTH_SHORT).show();
            adapter.setItems(items);
            adapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }
    }
}

package com.shadercat.havvka;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ListFragment.ListFragmentInteractionListener, FavouritesFragment.FavouriteFragmentInteractionListener,
        OrdersFragment.OrdersFragmentInteractionListener, SettingFragment.SettingFragmentInteractionListener, CartFragment.CartFragmentInteractionListener
{

    static final int LIST_GET_DATA = 101;
    static final int FAVOURITES_GET_DATA = 102;
    static final int ORDERS_GET_DATA = 103;
    static final int SETTING_SET = 104;



    private List<Item> listOfItems = new ArrayList<>();
    private ItemAdapter adapter;

    final ListFragment listFragment = new ListFragment();
    final FavouritesFragment favouritesFragment = new FavouritesFragment();
    final CartFragment cartFragment = new CartFragment();
    final OrdersFragment ordersFragment = new OrdersFragment();
    final SettingFragment settingFragments = new SettingFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = listFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fm.beginTransaction().hide(active).show(listFragment).commit();
                    active = listFragment;
                    return true;
                case R.id.navigation_favourite:
                    fm.beginTransaction().hide(active).show(favouritesFragment).commit();
                    active = favouritesFragment;
                    return true;
                case R.id.navigation_cart:
                    fm.beginTransaction().hide(active).show(cartFragment).commit();
                    active = cartFragment;
                    return true;
                case R.id.navigation_orders:
                    fm.beginTransaction().hide(active).show(ordersFragment).commit();
                    active = ordersFragment;
                    return true;
                case R.id.navigation_setting:
                    fm.beginTransaction().hide(active).show(settingFragments).commit();
                    active = settingFragments;
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CheckInfo();
        setContentView(R.layout.activity_main);
        fm.beginTransaction().add(R.id.main_frame, cartFragment, "5").hide(cartFragment).commit();
        fm.beginTransaction().add(R.id.main_frame, settingFragments, "4").hide(settingFragments).commit();
        fm.beginTransaction().add(R.id.main_frame, ordersFragment, "3").hide(ordersFragment).commit();
        fm.beginTransaction().add(R.id.main_frame, favouritesFragment, "2").hide(favouritesFragment).commit();
        fm.beginTransaction().add(R.id.main_frame, listFragment, "1").show(listFragment).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
    protected void CheckInfo()
    {
        //TODO: implement information check there
        if(!UserInfo.IsCheckedAccount)
        {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void ListFragmentInteraction(Uri link)
    {
        switch (link.getScheme())
        {
            case "data":
                //TODO: logic for list fragment
                for(int i = 0; i < 101; i++)
                {
                    listOfItems.add(new Item("Name of Food " + i, "Small descrition","Big Description", R.drawable.food_test));
                }
                adapter = new ItemAdapter(this, R.layout.list_item, listOfItems);
                listFragment.addAdapter(adapter);
                break;
            case "itemClick":
                Toast.makeText(getApplicationContext(),"Click on " + Integer.parseInt(link.getSchemeSpecificPart()) + " item", Toast.LENGTH_LONG).show();
                break;
            default:
                //default
        }
    }
    public void FavouriteFragmentInteraction(Uri link)
    {

    }
    public void OrdersFragmentInteraction(Uri link)
    {

    }
    public void SettingFragmentInteraction(Uri link)
    {

    }
    public void CartFragmentInteraction(Uri link)
    {

    }
}

package com.shadercat.havvka;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Locale;

public class CartFragment extends Fragment {


    private CartFragmentInteractionListener mListener;
    CartItemAdapter adapter;
    ListView list;
    TextView textSum;
    GridLayout sum_container;

    public CartFragment() {
        // Required empty public constructor
    }


    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        list = (ListView) view.findViewById(R.id.cartItemsList);
        textSum = (TextView) view.findViewById(R.id.sum_CartFragment);
        sum_container = (GridLayout) view.findViewById(R.id.sum_container_CartFragment);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CartFragmentInteractionListener) {
            mListener = (CartFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ListFragmentInteractionListener");
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener.CartFragmentInteraction(Uri.parse("data:1"));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        UpdateInformation();
    }

    public void AddAdapter(final CartItemAdapter adapter) {
        this.adapter = adapter;
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog(position);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.CartFragmentInteraction(Uri.parse("itemLongClick:" + String.valueOf(position)));
                return false;
            }
        });
    }

    public interface CartFragmentInteractionListener {
        void CartFragmentInteraction(Uri link);
    }

    private void Dialog(final int index) {
        final CartItem item = ListCartItem.list.get(index);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                        item.setQuantity(picker.getValue());
                        UpdateInformation();
                        dialog.cancel();
                    }
                })
                .setNeutralButton(R.string.deleteItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ListCartItem.RemoveCartItem(index);
                        SnackbarShow();
                        UpdateInformation();
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void SnackbarShow() {
        Snackbar mSnackbar = Snackbar.make(list, getString(R.string.removedFromCart), Snackbar.LENGTH_LONG)
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
            UpdateInformation();
        }
    };

    public void UpdateInformation() {
        if (adapter != null && textSum != null) {
            adapter.notifyDataSetChanged();
            textSum.setText(String.format(Locale.getDefault(), "%.2f", ListCartItem.GetSumPrice()));
            if (ListCartItem.GetSumPrice() == 0d) {
                sum_container.setVisibility(View.GONE);
            } else {
                sum_container.setVisibility(View.VISIBLE);
            }
        }
    }
}

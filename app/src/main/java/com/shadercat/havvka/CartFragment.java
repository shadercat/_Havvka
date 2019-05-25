package com.shadercat.havvka;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Locale;

public class CartFragment extends Fragment {


    private CartFragmentInteractionListener mListener;
    TextView textSum;
    GridLayout sum_container;
    Context context;
    CartListAdapter adapter;
    RecyclerView recyclerView;

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
        recyclerView = (RecyclerView) view.findViewById(R.id.cartItemsList);
        textSum = (TextView) view.findViewById(R.id.sum_CartFragment);
        sum_container = (GridLayout) view.findViewById(R.id.sum_container_CartFragment);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CartFragmentInteractionListener) {
            mListener = (CartFragmentInteractionListener) context;
            this.context = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ListFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListener.CartFragmentInteraction(Uri.parse("data:1"));
        adapter = new CartListAdapter(getContext(),ListCartItem.list);
        adapter.setOnClickListeners(new CartListAdapter.OnClickListeners() {
            @Override
            public void itemClick(int position) {
                Intent product_info = new Intent(getContext(), InformationActivity.class);
                product_info.putExtra(Item.class.getSimpleName(), ListCartItem.list.get(position).getItem().GetID());
                startActivity(product_info);
            }

            @Override
            public void moreClick(int position) {
                Dialog(position);
            }
        });
        DividerItemDecoration itemDecor = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
        recyclerView.setAdapter(adapter);
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
                        ThematicSnackbar.SnackbarWithActionShow(getString(R.string.removedFromCart),
                                getString(R.string.cancel), snackbarOnClickListener,
                                recyclerView,
                                getContext());
                        UpdateInformation();
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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
            adapter.setItems(ListCartItem.list);
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

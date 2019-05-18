package com.shadercat.havvka;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

public class ThematicSnackbar {
    public static void SnackbarTextShow(String mes, View element, Context context) {
        Snackbar mSnackbar = Snackbar.make(element, mes, Snackbar.LENGTH_SHORT)
                .setActionTextColor(context.getResources().getColor(R.color.colorBlue));
        View snackbarView = mSnackbar.getView();
        snackbarView.setBackgroundResource(R.color.colorPrimaryDark);
        TextView snackTextView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        snackTextView.setTextColor(context.getResources().getColor(R.color.colorWhite));
        mSnackbar.show();
    }

    public static void SnackbarWithActionShow(String mes, String actionMes, View.OnClickListener listener, View element, Context context) {
        Snackbar mSnackbar = Snackbar.make(element, mes, Snackbar.LENGTH_LONG)
                .setAction(actionMes, listener)
                .setActionTextColor(context.getResources().getColor(R.color.colorBlue));
        View snackbarView = mSnackbar.getView();
        snackbarView.setBackgroundResource(R.color.colorPrimaryDark);
        TextView snackTextView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        snackTextView.setTextColor(context.getResources().getColor(R.color.colorWhite));
        mSnackbar.show();
    }
}

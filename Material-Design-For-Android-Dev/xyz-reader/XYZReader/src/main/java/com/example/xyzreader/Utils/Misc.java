package com.example.xyzreader.Utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.example.xyzreader.R;

public class Misc {

    public static void showSnackBar(Context context, View view, int stringRes, boolean error) {
        final Snackbar snackbar;
        snackbar = Snackbar.make(view, context.getString(stringRes), Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(ContextCompat.getColor(context, error ? R.color.colorError : R.color.colorInfo));
        TextView textView = snackBarView.findViewById(android.support.design.R.id.snackbar_text); //Get reference of snackbar textview
        textView.setMaxLines(3);

        snackbar.show();
    }
}

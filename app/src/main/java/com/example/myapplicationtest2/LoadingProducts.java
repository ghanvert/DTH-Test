package com.example.myapplicationtest2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.app.AlertDialog;

class LoadingProducts {

    private final Activity activity;
    private AlertDialog dialog;

    LoadingProducts(Activity myActivity) {
        activity = myActivity;
    }

    void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog_products, null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog() {
        dialog.dismiss();
    }

}

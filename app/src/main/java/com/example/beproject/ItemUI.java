package com.example.beproject;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.AuthResult;

import java.util.ArrayList;
import java.util.List;


public class ItemUI extends ArrayAdapter<Item> {

    private final Context context;
    public  List<Item> items;









    public ItemUI(Context context, int resource, List<Item> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_item, null);
        View view2 = inflater.inflate(R.layout.activity_main, null);


        TextView viewName = view.findViewById(R.id.ui_item_name);
        TextView viewPrice = view.findViewById(R.id.ui_item_price);
        ImageView viewImage = view.findViewById(R.id.ui_item_image);
        TextView itemTotal = view2.findViewById(R.id.MainActivity_TextView_ItemTotal);
        TextView itemCount = view2.findViewById(R.id.MainActivity_TextView_TotalPrice);



        String name = items.get(position).getName();
        final String[] iPrice = {"Rs." + items.get(position).getPrice()};
        Drawable image = items.get(position).getImage();



        viewName.setText(name);
        viewPrice.setText(iPrice[0]);
        viewImage.setImageDrawable(image);


        return view;
    }








}


















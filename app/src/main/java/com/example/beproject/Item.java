package com.example.beproject;

import android.graphics.drawable.Drawable;

public class Item {
    private final String indicator;
    private final double price;
    private final String name;
    private final Drawable image;

    public Item(String indicator, String name, double price, Drawable image) {
        this.indicator = indicator;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public String getIndicator() {
        return indicator;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Drawable getImage() {
        return image;
    }
}

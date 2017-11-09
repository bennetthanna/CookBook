package com.hannabennett.cookbook;

/**
 * Created by HannaBennett on 11/9/17.
 */

public class Ingredient {
    private String item;
    private String quantity;
    private String measurement;

    public Ingredient() {
        this.item = null;
        this.quantity = null;
        this.measurement = null;
    }

    public Ingredient(String item, String quantity, String measurement) {
        this.item = item;
        this.quantity = quantity;
        this.measurement = measurement;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }
}

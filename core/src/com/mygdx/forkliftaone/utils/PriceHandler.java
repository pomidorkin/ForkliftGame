package com.mygdx.forkliftaone.utils;

public class PriceHandler {
    private int price;
    private boolean donateCurrency;

    public PriceHandler(int price, boolean donateCurrency){
        this.price = price;
        this.donateCurrency = donateCurrency;
    }

    public int getPrice() {
        return price;
    }

    public boolean isDonateCurrency() {
        return donateCurrency;
    }
}

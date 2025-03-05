package com.example.dragun.services;

import android.util.Log;

import com.example.dragun.data.NonSellableProduct;
import com.example.dragun.data.SellableProduct;
import com.example.dragun.helpers.DatabaseHelper;
import com.example.dragun.helpers.ModelBank;

public class ProductService {
    public static void addSellable(SellableProduct product) {
        ModelBank<SellableProduct> bank = DatabaseHelper.getSellableProductBank();
        LogService.log("Added " + product.getProductName());
        bank.add(product);
    }

    public static void editSellable(SellableProduct product) {
        ModelBank<SellableProduct> bank = DatabaseHelper.getSellableProductBank();
        LogService.log("Edited " + product.getProductName());
        bank.update(product);
    }

    public static void deleteSellable(int id) {
        ModelBank<SellableProduct> bank = DatabaseHelper.getSellableProductBank();
        SellableProduct product = bank.get(id);
        LogService.log("Deleted " + product.getProductName());
        bank.delete(id);
    }

    public static void addNonSellable(NonSellableProduct product) {
        ModelBank<NonSellableProduct> bank = DatabaseHelper.getNonSellableProductBank();
        bank.add(product);
    }

    public static void editNonSellable(NonSellableProduct product) {
        ModelBank<NonSellableProduct> bank = DatabaseHelper.getNonSellableProductBank();
        bank.update(product);
    }

    public static void deleteNonSellable(int id) {
        ModelBank<NonSellableProduct> bank = DatabaseHelper.getNonSellableProductBank();
        bank.delete(id);
    }
}

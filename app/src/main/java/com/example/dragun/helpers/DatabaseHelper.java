package com.example.dragun.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.dragun.data.NonSellableProduct;
import com.example.dragun.data.ProductLog;
import com.example.dragun.data.SellableProduct;
import com.example.dragun.services.ProductService;

public class DatabaseHelper {
    public static final int PRODUCT_COUNT_LIMIT = 10;

    private static final String FILE_KEY = "db";
    private static SharedPreferences sharedPref;
    private static SharedPreferences.Editor editor;

    private static ModelBank<SellableProduct> sellableProductBank;
    private static ModelBank<NonSellableProduct> nonSellableProductBank;
    private static ModelBank<ProductLog> productLogBank;

    public static void initialize(Context context) {
        sharedPref = context.getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        sellableProductBank = new ModelBank<>(sharedPref, editor, "sellables", SellableProduct.class);
        nonSellableProductBank = new ModelBank<>(sharedPref, editor, "nonsellables", NonSellableProduct.class);
        productLogBank = new ModelBank<>(sharedPref, editor, "productlog", ProductLog.class);
    }

    public static ModelBank<SellableProduct> getSellableProductBank() {
        return sellableProductBank;
    }

    public static ModelBank<NonSellableProduct> getNonSellableProductBank() {
        return nonSellableProductBank;
    }

    public static ModelBank<ProductLog> getProductLogBank() {
        return productLogBank;
    }

    public static void addDummyData() {
        if (sellableProductBank.getAll().isEmpty()) {
            addSellableDummies();
        }
        if (nonSellableProductBank.getAll().isEmpty()) {
            addNonSellableDummies();
        }
    }

    public static boolean isLimitHit() {
        int productCount = sellableProductBank.getAll().size() + nonSellableProductBank.getAll().size();
        return productCount >= PRODUCT_COUNT_LIMIT;
    }

    public static void clear() {
        sellableProductBank.clear();
        nonSellableProductBank.clear();
    }

    private static void addSellableDummies() {
        SellableProduct add1 = new SellableProduct("Black Coffee", 80.5, "Hot Brew");
        SellableProduct add2 = new SellableProduct("Cappucino", 125, "Hot Brew");
        SellableProduct add3 = new SellableProduct("Frappe", 80.5, "Cold Brew");
        SellableProduct add4 = new SellableProduct("Ice Americano", 80.5, "Cold Brew");
        SellableProduct add5 = new SellableProduct("Latte", 80.5, "Hot Brew");
        SellableProduct add6 = new SellableProduct("Macchiato", 80.5, "Hot Brew");

        ProductService.addSellable(add1);
        ProductService.addSellable(add2);
        ProductService.addSellable(add3);
        ProductService.addSellable(add4);
        ProductService.addSellable(add5);
        ProductService.addSellable(add6);
    }

    private static void addNonSellableDummies() {
        NonSellableProduct add1 = new NonSellableProduct("Coffee Beans",  "g", 450);
        NonSellableProduct add3 = new NonSellableProduct("Cream", "ml", 1200);
        NonSellableProduct add6 = new NonSellableProduct("Sugar", "g", 400);

        ProductService.addNonSellable(add1);
        ProductService.addNonSellable(add3);
        ProductService.addNonSellable(add6);
    }
}

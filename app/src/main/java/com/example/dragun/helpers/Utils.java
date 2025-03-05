package com.example.dragun.helpers;

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dragun.data.NonSellableProduct;

public class Utils {
    public static void longToast(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void toast(String message, Context context) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String getText(EditText text) {
        return String.valueOf(text.getText());
    }

    public static String getText(TextView text) {
        return String.valueOf(text.getText());
    }

    public static boolean startsWithDigit(String text) {
        if (text.isEmpty()) {
            return false;
        }

        char[] digits = "0123456789".toCharArray();
        char firstChar = text.charAt(0);
        for (char digit : digits) {
            if (firstChar == digit) {
                return true;
            }
        }
        return false;
    }

    public static String getFullUnitWord(NonSellableProduct product) {
        if (product.getUnit().equals("g")) {
            return "grams";
        }
        if (product.getUnit().equals("ml")) {
            return "milliliters";
        }
        return "";
    }
}

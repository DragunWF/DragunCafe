package com.example.dragun.services;

import com.example.dragun.data.ProductLog;
import com.example.dragun.helpers.DatabaseHelper;
import com.example.dragun.helpers.ModelBank;

import java.util.Date;

public class LogService {
    public static void log(String description) {
        ModelBank<ProductLog> bank = DatabaseHelper.getProductLogBank();
        bank.add(new ProductLog(description, new Date().toString()));
    }
}

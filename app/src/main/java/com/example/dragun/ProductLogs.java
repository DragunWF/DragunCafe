package com.example.dragun;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dragun.adapters.LogAdapter;
import com.example.dragun.data.ProductLog;
import com.example.dragun.helpers.DatabaseHelper;
import com.example.dragun.helpers.Utils;

import java.util.Collections;
import java.util.List;

public class ProductLogs extends AppCompatActivity {
    private RecyclerView logRecycler;
    private LogAdapter logAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_logs);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {
            logRecycler = findViewById(R.id.logRecycler);
            backBtn = findViewById(R.id.backBtn);

            setButtons();
            setRecycler();
        } catch (Exception err) {
            err.printStackTrace();
            Utils.longToast(err.getMessage(), this);
        }
    }

    private void setButtons() {
        backBtn.setOnClickListener(v -> finish());
    }

    private void setRecycler() {
        logRecycler.setHasFixedSize(false);

        List<ProductLog> productLogs = DatabaseHelper.getProductLogBank().getAll();
        Collections.reverse(productLogs);
        logAdapter = new LogAdapter(productLogs);
        logRecycler.setAdapter(logAdapter);

        layoutManager = new LinearLayoutManager(this);
        logRecycler.setLayoutManager(layoutManager);
    }
}
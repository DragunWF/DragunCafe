package com.example.dragun;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dragun.adapters.NonSellableProductAdapter;
import com.example.dragun.adapters.SellableProductAdapter;
import com.example.dragun.data.NonSellableProduct;
import com.example.dragun.data.SellableProduct;
import com.example.dragun.helpers.DatabaseHelper;
import com.example.dragun.helpers.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SearchView searchBar;
    private Button addBtn, sellableBtn, nonSellableBtn;
    private ImageView logBtn;

    private RecyclerView productRecycler;
    private SellableProductAdapter productAdapter;
    private NonSellableProductAdapter nonSellableProductAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private Spinner categorySpinner;
    private ArrayAdapter<CharSequence> adapter;
    private String productType;

    @Override
    protected void onResume() {
        super.onResume();
        productAdapter.updateDataSet();
        nonSellableProductAdapter.updateDataSet();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {
            DatabaseHelper.initialize(this);
            DatabaseHelper.addDummyData();
            DatabaseHelper.getSellableProductBank().log();

            searchBar = findViewById(R.id.searchBar);

            addBtn = findViewById(R.id.addBtn);
            logBtn = findViewById(R.id.logBtn);
            sellableBtn = findViewById(R.id.sellableBtn);
            nonSellableBtn = findViewById(R.id.nonSellableBtn);

            productRecycler = findViewById(R.id.productRecycler);
            categorySpinner = findViewById(R.id.categorySpinner);

            productType = "Sellable";

            setRecycler();
            setButtons();
            setSpinner();
            setSearch();
        } catch (Exception err) {
            err.printStackTrace();
            Utils.longToast(err.getMessage(), this);
        }
    }

    private void setRecycler() {
        sellableBtn.setBackgroundColor(getResources().getColor(R.color.primary));
        productRecycler.setHasFixedSize(false);

        nonSellableProductAdapter = new NonSellableProductAdapter(DatabaseHelper.getNonSellableProductBank().getAll(), this);
        productAdapter = new SellableProductAdapter(DatabaseHelper.getSellableProductBank().getAll(), this);
        productRecycler.setAdapter(productAdapter);

        layoutManager = new LinearLayoutManager(this);
        productRecycler.setLayoutManager(layoutManager);
    }

    private void setButtons() {
        addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddItem.class);
            intent.putExtra("productType", productType);
            startActivity(intent);
        });
        sellableBtn.setOnClickListener(v -> {
            productType = "Sellable";
            setAdapter("Sellable");
            // Utils.longToast("Switched to Sellable!", MainActivity.this);
        });
        nonSellableBtn.setOnClickListener(v -> {
            productType = "NonSellable";
            setAdapter("NonSellable");
            // Utils.longToast("Switched to Non-Sellable!", MainActivity.this);
        });
        logBtn.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ProductLogs.class));
        });
    }

    private void setAdapter(String productType) {
        if (productType.equals("Sellable")) {
            productAdapter = new SellableProductAdapter(DatabaseHelper.getSellableProductBank().getAll(), this);
            productRecycler.setAdapter(productAdapter);

            adapter = ArrayAdapter.createFromResource(
                    this,
                    R.array.coffee_categories,
                    android.R.layout.simple_spinner_item
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            sellableBtn.setBackgroundColor(getResources().getColor(R.color.primary));
            nonSellableBtn.setBackgroundColor(getResources().getColor(R.color.black));
            categorySpinner.setAdapter(adapter);
        } else {
            nonSellableProductAdapter = new NonSellableProductAdapter(DatabaseHelper.getNonSellableProductBank().getAll(), this);
            productRecycler.setAdapter(nonSellableProductAdapter);

            adapter = ArrayAdapter.createFromResource(
                    this,
                    R.array.unit_measurement,
                    android.R.layout.simple_spinner_item
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sellableBtn.setBackgroundColor(getResources().getColor(R.color.black));
            nonSellableBtn.setBackgroundColor(getResources().getColor(R.color.primary));
            categorySpinner.setAdapter(adapter);
        }
    }

    private void setSpinner() {
        // Create an ArrayAdapter using the string array and a default spinner layout.
        adapter = ArrayAdapter.createFromResource(
                this,
                R.array.coffee_categories,
                android.R.layout.simple_spinner_item
        );
        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = adapter.getItem(position).toString();

                if (selectedItem.equals("Any")) {
                    productAdapter.updateDataSet(DatabaseHelper.getSellableProductBank().getAll());
                    nonSellableProductAdapter.updateDataSet(DatabaseHelper.getNonSellableProductBank().getAll());
                } else {
                    if (productType.equals("Sellable")) {
                        List<SellableProduct> products = DatabaseHelper.getSellableProductBank().getAll();
                        List<SellableProduct> results = new ArrayList<>();

                        for (SellableProduct product : products) {
                            if (product.getCategory().equals(selectedItem)) {
                                results.add(product);
                            }
                        }

                        productAdapter.updateDataSet(results);
                    } else {
                        List<NonSellableProduct> products = DatabaseHelper.getNonSellableProductBank().getAll();
                        List<NonSellableProduct> results = new ArrayList<>();

                        for (NonSellableProduct product : products) {
                            if (product.getUnit().equals(selectedItem)) {
                                results.add(product);
                            }
                        }

                        nonSellableProductAdapter.updateDataSet(results);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSearch() {
        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                update(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                update(newText);
                return false;
            }

            public void update(String query) {
                query = query.toLowerCase();

                if (productType.equals("Sellable")) {
                    List<SellableProduct> sellableProducts = DatabaseHelper.getSellableProductBank().getAll();
                    List<SellableProduct> results = new ArrayList<>();

                    for (SellableProduct product : sellableProducts) {
                        String productName = product.getProductName().toLowerCase();
                        if (productName.contains(query)) {
                            results.add(product);
                        }
                    }

                    productAdapter.updateDataSet(results);
                } else {
                    List<NonSellableProduct> nonSellableProducts = DatabaseHelper.getNonSellableProductBank().getAll();
                    List<NonSellableProduct> results = new ArrayList<>();

                    for (NonSellableProduct product : nonSellableProducts) {
                        String productName = product.getProductName().toLowerCase();
                        if (productName.contains(query)) {
                            results.add(product);
                        }
                    }

                    nonSellableProductAdapter.updateDataSet(results);
                }
            }
        });
    }
}
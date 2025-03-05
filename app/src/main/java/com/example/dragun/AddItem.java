package com.example.dragun;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.dragun.data.NonSellableProduct;
import com.example.dragun.data.SellableProduct;
import com.example.dragun.helpers.DatabaseHelper;
import com.example.dragun.helpers.Utils;
import com.example.dragun.services.ProductService;

public class AddItem extends AppCompatActivity {
    private TextView priceOrStockHeader, headerText;
    private EditText productNameText, priceOrStockText;
    private Spinner categorySpinner;
    private Button backBtn, addBtn;

    private boolean isSellableForm;
    private String selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {
            headerText = findViewById(R.id.headerText);
            priceOrStockHeader = findViewById(R.id.priceOrStockHeader);
            productNameText = findViewById(R.id.productNameText);
            priceOrStockText = findViewById(R.id.priceOrStockText);

            categorySpinner = findViewById(R.id.categorySpinner);
            backBtn = findViewById(R.id.backBtn);
            addBtn = findViewById(R.id.addBtn);

            String productType = getIntent().getStringExtra("productType");
            isSellableForm = productType != null && productType.equals("Sellable");

            setData();
            setButtons();
            setSpinner();
        } catch (Exception err) {
            err.printStackTrace();
            Utils.longToast(err.getMessage(), AddItem.this);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setData() {
        if (isSellableForm) {
            priceOrStockHeader.setText("Price");
        } else {
            headerText.setText("Add New Non-Sellable Item:");
            priceOrStockHeader.setText("Stock");
            priceOrStockText.setHint("Enter stock");
        }
    }

    private void setButtons() {
        backBtn.setOnClickListener(v -> {
            String productName = Utils.getText(productNameText).trim();
            String priceOrStockStr = Utils.getText(priceOrStockText).trim();

            if (!productName.isEmpty() || !priceOrStockStr.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddItem.this);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancels the dialog.
                    }
                });
                builder.setMessage("Are you sure you want to cancel your changes?");

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                finish();
            }
        });
        addBtn.setOnClickListener(v -> {
            if (DatabaseHelper.isLimitHit()) {
                int productLimit = DatabaseHelper.PRODUCT_COUNT_LIMIT;
                AlertDialog.Builder builder = new AlertDialog.Builder(AddItem.this);

                builder.setMessage("You can only have a maximum of " + productLimit + " products!")
                        .setTitle("Product Count Limit Reached!");

                builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Utils.toast("Cancelled", AddItem.this);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                return;
            }

            String productName = Utils.getText(productNameText).trim();
            String priceOrStockStr = Utils.getText(priceOrStockText).trim();
            String category = selectedCategory;

            if (productName.isEmpty() || priceOrStockStr.isEmpty()) {
                Utils.longToast("All fields are required!", AddItem.this);
                return;
            }
            if (Utils.startsWithDigit(productName)) {
                Utils.longToast("The product name cannot start with digits!", AddItem.this);
                return;
            }


            try {
                if (isSellableForm) {
                    double price = Double.parseDouble(priceOrStockStr);
                    SellableProduct product = new SellableProduct(productName, price, category);
                    ProductService.addSellable(product);
                } else {
                    int quantity = Integer.parseInt(priceOrStockStr);
                    NonSellableProduct product = new NonSellableProduct(productName, category, quantity);
                    ProductService.addNonSellable(product);
                }

                Utils.longToast(productName + " has been added!", AddItem.this);

                finish();
            } catch (Exception err) {
                err.printStackTrace();
                Utils.longToast(err.getMessage(), this);
            }

        });
    }

    private void setSpinner() {
        ArrayAdapter<CharSequence> adapter;
        if (isSellableForm) {
            adapter = ArrayAdapter.createFromResource(
                    this,
                    R.array.coffee_categories_add_form,
                    android.R.layout.simple_spinner_item
            );
        } else {
            adapter = ArrayAdapter.createFromResource(
                    this,
                    R.array.unit_measurement_add_form,
                    android.R.layout.simple_spinner_item
            );
        }

        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = adapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
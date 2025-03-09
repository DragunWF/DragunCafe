package com.example.dragun;

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

public class EditItems extends AppCompatActivity {
    private TextView priceHeader, headerText;
    private EditText productNameText, priceOrStockText;
    private Spinner categorySpinner;

    private boolean isSellableForm;
    private String selectedCategory;
    private int viewedProductId;

    private Button backBtn, editBtn;

    private String initialProductName;
    private String initialPriceOrStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_items);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {
            productNameText = findViewById(R.id.productNameText);
            priceOrStockText = findViewById(R.id.priceOrStockText);
            categorySpinner = findViewById(R.id.categorySpinner);
            priceHeader = findViewById(R.id.priceOrStockHeader);
            headerText = findViewById(R.id.headerText);

            backBtn = findViewById(R.id.backBtn);
            editBtn = findViewById(R.id.editBtn);

            viewedProductId = getIntent().getIntExtra("productId", 1);
            String productType = getIntent().getStringExtra("productType");
            isSellableForm = productType != null && productType.equals("Sellable");

            setButtons();
            setSpinner();
            setData();
        } catch (Exception err) {
            err.printStackTrace();
            Utils.longToast(err.getMessage(), this);
        }
    }

    private void setData() {
        if (isSellableForm) {
            SellableProduct sellable = DatabaseHelper.getSellableProductBank().get(viewedProductId);

            initialProductName = sellable.getProductName();
            initialPriceOrStock = String.valueOf(sellable.getPrice());

            productNameText.setText(sellable.getProductName());
            priceOrStockText.setText(String.valueOf(sellable.getPrice()));

            headerText.setText("Edit " + sellable.getProductName() + ":");
        } else {
            NonSellableProduct nonSellable = DatabaseHelper.getNonSellableProductBank().get(viewedProductId);

            initialProductName = nonSellable.getProductName();
            initialPriceOrStock = String.valueOf(nonSellable.getStock());

            productNameText.setText(nonSellable.getProductName());
            priceOrStockText.setText(String.valueOf(nonSellable.getStock()));
            priceHeader.setText("Stock");
            priceOrStockText.setHint("Enter stock");
            headerText.setText("Edit " + nonSellable.getProductName() + ":");

            categorySpinner.setEnabled(false);
        }
    }

    private void setButtons() {
        backBtn.setOnClickListener(v -> {
            String productName = Utils.getText(productNameText);
            String priceOrStockStr = Utils.getText(priceOrStockText);

            if (!productName.equals(initialProductName) || !priceOrStockStr.equals(initialPriceOrStock)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditItems.this);

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
                builder.setMessage("Are you sure you want to cancel your edited changes?");

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                finish();
            }
        });
        editBtn.setOnClickListener(v -> {
            String productName = Utils.getText(productNameText);
            String priceOrStockStr = Utils.getText(priceOrStockText);

            if (productName.isEmpty() || priceOrStockStr.isEmpty()) {
                Utils.longToast("All fields are required!", EditItems.this);
                return;
            }
            if (Utils.startsWithDigit(productName)) {
                Utils.longToast("The product name cannot start with a digit", EditItems.this);
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(EditItems.this);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    try {
                        if (isSellableForm) {
                            SellableProduct product = DatabaseHelper.getSellableProductBank().get(viewedProductId);

                            double price = Double.parseDouble(priceOrStockStr);
                            product.setProductName(productName);
                            product.setPrice(price);
                            product.setCategory(selectedCategory);

                            ProductService.editSellable(product);
                        } else {
                            NonSellableProduct product = DatabaseHelper.getNonSellableProductBank().get(viewedProductId);

                            int stock = Integer.parseInt(priceOrStockStr);
                            product.setProductName(productName);
                            product.setStock(stock);

                            ProductService.editNonSellable(product);
                        }

                        Utils.longToast("Product has been successfully edited!", EditItems.this);
                        finish();
                    } catch (Exception err) {
                        Utils.longToast("Invalid numeric field!", EditItems.this);
                    }
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancels the dialog.
                }
            });
            builder.setMessage("Are you sure you want to edit this item?");

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void setSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                isSellableForm ? R.array.coffee_categories_add_form : R.array.unit_measurement_add_form,
                android.R.layout.simple_spinner_item
        );;

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
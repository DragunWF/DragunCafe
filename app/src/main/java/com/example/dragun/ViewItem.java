package com.example.dragun;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
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

public class ViewItem extends AppCompatActivity {
    private TextView productNameText, priceText, priceHeader, categoryText, categoryHeader;
    private Button editBtn, deleteBtn;
    private ImageView backBtn;

    private String productType;
    private int viewedProductId;
    private boolean isSellableView;

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_item);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {
            productNameText = findViewById(R.id.productNameText);
            priceText = findViewById(R.id.priceText);
            priceHeader = findViewById(R.id.priceHeader);
            categoryText = findViewById(R.id.categoryText);
            categoryHeader = findViewById(R.id.categoryHeader);

            editBtn = findViewById(R.id.editBtn);
            deleteBtn = findViewById(R.id.deleteBtn);
            backBtn = findViewById(R.id.backImageBtn);

            productType = getIntent().getStringExtra("productType");
            viewedProductId = getIntent().getIntExtra("productId", 1);
            isSellableView = productType.equals("Sellable");

            setData();
            setButtons();
        } catch (Exception err) {
            err.printStackTrace();
            Utils.longToast(err.getMessage(), this);
        }
    }

    private void setData() {
        if (isSellableView) {
            SellableProduct product = DatabaseHelper.getSellableProductBank().get(viewedProductId);
            productNameText.setText(product.getProductName());
            priceText.setText(product.getPrice() + " PHP");
            categoryText.setText(product.getCategory());
        } else {
            NonSellableProduct product = DatabaseHelper.getNonSellableProductBank().get(viewedProductId);
            productNameText.setText(product.getProductName());
            priceHeader.setText("Stock");
            priceText.setText(String.valueOf(product.getStock()));
            categoryHeader.setText("Unit");

            String fullUnitWord = Utils.getFullUnitWord(product);
            categoryText.setText(String.format("%s (%s)", product.getUnit(), fullUnitWord));
        }
    }

    private void setButtons() {
        backBtn.setOnClickListener(v -> {
            finish();
        });
        editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ViewItem.this, EditItems.class);
            intent.putExtra("productType", productType);
            intent.putExtra("productId", viewedProductId);
            startActivity(intent);
        });
        deleteBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ViewItem.this);
            String productName = Utils.getText(productNameText);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if (productType.equals("Sellable")) {
                        ProductService.deleteSellable(viewedProductId);
                    } else {
                        ProductService.deleteNonSellable(viewedProductId);
                    }

                    Utils.longToast(productName + " has been deleted!", ViewItem.this);
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancels the dialog.
                }
            });
            builder.setMessage("Are you sure you want to delete " + productName + "?");

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }
}
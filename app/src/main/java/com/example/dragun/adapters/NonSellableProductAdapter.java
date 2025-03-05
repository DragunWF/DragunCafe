package com.example.dragun.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dragun.R;
import com.example.dragun.ViewItem;
import com.example.dragun.data.NonSellableProduct;
import com.example.dragun.data.SellableProduct;
import com.example.dragun.helpers.DatabaseHelper;
import com.example.dragun.helpers.Utils;

import java.util.List;

public class NonSellableProductAdapter extends RecyclerView.Adapter<NonSellableProductAdapter.ViewHolder> {

    private List<NonSellableProduct> localDataSet;
    private Context context;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameText, stockText;
        private final ImageView viewBtn;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            nameText = view.findViewById(R.id.ingredientNameText);
            stockText = view.findViewById(R.id.stockText);
            viewBtn = view.findViewById(R.id.coffeeImage);
        }

        public TextView getNameText() {
            return nameText;
        }

        public TextView getStockText() {
            return stockText;
        }

        public ImageView getViewBtn() {
            return viewBtn;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public NonSellableProductAdapter(List<NonSellableProduct> dataSet, Context context) {
        localDataSet = dataSet;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.nonsellable_card, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        // viewHolder.getTextView().setText(localDataSet[position]);
        NonSellableProduct product = localDataSet.get(position);
        viewHolder.getNameText().setText(product.getProductName());

        String unit = String.format(" %s (%s)", product.getUnit(), Utils.getFullUnitWord(product));
        viewHolder.getStockText().setText(product.getStock() + unit);
        viewHolder.getViewBtn().setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewItem.class);
            intent.putExtra("productId", product.getId());
            intent.putExtra("productType", "NonSellable");
            context.startActivity(intent);
        });
    }

    public void updateDataSet() {
        List<NonSellableProduct> products = DatabaseHelper.getNonSellableProductBank().getAll();
        updateDataSet(products);
    }

    public void updateDataSet(List<NonSellableProduct> products) {
        localDataSet.clear();
        for (NonSellableProduct product : products) {
            localDataSet.add(product);
        }
        notifyDataSetChanged();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

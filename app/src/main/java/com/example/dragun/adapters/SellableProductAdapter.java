package com.example.dragun.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dragun.R;
import com.example.dragun.ViewItem;
import com.example.dragun.data.SellableProduct;
import com.example.dragun.helpers.DatabaseHelper;

import java.util.List;

public class SellableProductAdapter extends RecyclerView.Adapter<SellableProductAdapter.ViewHolder> {

    private List<SellableProduct> localDataSet;
    private Context context;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView productNameText, priceText, categoryText;
        private final ImageView viewBtn;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            productNameText = view.findViewById(R.id.productNameText);
            priceText = view.findViewById(R.id.priceText);
            categoryText = view.findViewById(R.id.categoryText);
            viewBtn = view.findViewById(R.id.coffeeImage);
        }

        public TextView getProductNameText() {
            return productNameText;
        }

        public TextView getPriceText() {
            return priceText;
        }

        public TextView getCategoryText() {
            return categoryText;
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
    public SellableProductAdapter(List<SellableProduct> dataSet, Context context) {
        localDataSet = dataSet;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.sellable_card, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        // viewHolder.getTextView().setText(localDataSet[position]);
        SellableProduct product = localDataSet.get(position);
        viewHolder.getProductNameText().setText(product.getProductName());
        viewHolder.getPriceText().setText(product.getPrice() + " PHP");
        viewHolder.getCategoryText().setText(product.getCategory());
        viewHolder.getViewBtn().setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewItem.class);
            intent.putExtra("productId", product.getId());
            intent.putExtra("productType", "Sellable");
            context.startActivity(intent);
        });
    }

    public void updateDataSet() {
        List<SellableProduct> products = DatabaseHelper.getSellableProductBank().getAll();
        updateDataSet(products);
    }

    public void updateDataSet(List<SellableProduct> products) {
        localDataSet.clear();
        for (SellableProduct product : products) {
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

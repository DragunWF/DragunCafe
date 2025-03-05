package com.example.dragun.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dragun.R;
import com.example.dragun.data.ProductLog;

import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {

    private List<ProductLog> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateTimeText, descriptionText;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            dateTimeText = view.findViewById(R.id.datetimeText);
            descriptionText = view.findViewById(R.id.descriptionText);
        }

        public TextView getDateTimeText() {
            return dateTimeText;
        }

        public TextView getDescriptionText() {
            return descriptionText;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public LogAdapter(List<ProductLog> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.log_sellable_card, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        // viewHolder.getTextView().setText(localDataSet[position]);
        ProductLog productLog = localDataSet.get(position);
        viewHolder.getDateTimeText().setText(productLog.getDatetime());
        viewHolder.getDescriptionText().setText(productLog.getDescription());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

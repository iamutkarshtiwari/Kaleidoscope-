package com.github.iamutkarshtiwari.kaleidoscope.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.iamutkarshtiwari.kaleidoscope.models.Movies;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.github.iamutkarshtiwari.kaleidoscope.R;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<ProductViewHolder> {

    ArrayList<Movies> items;
    Activity activity;

    public HomeRecyclerAdapter(Activity activity, ArrayList<Movies> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_view_product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, final int position) {
        // Set sold status tag
        if (items.get(position).getStatus().equalsIgnoreCase("sold_out")) {
            holder.soldOut.setVisibility(View.VISIBLE);
        } else {
            holder.soldOut.setVisibility(View.GONE);
        }

        // Downloads the product image from url
        Picasso.with(activity)
                .load(items.get(position).getPhotoURL())
                .fit()
                .error(activity.getResources().getDrawable(R.drawable.image_not_found))
                .into(holder.productImage);

        // Format price to currency style
        DecimalFormat formatter = new DecimalFormat("#,###");
        String price = formatter.format(items.get(position).getPrice());
        holder.productPrice.setText(activity.getResources().getString(R.string.product_price, "$", price));
        holder.productName.setText(items.get(position).getName());
        holder.productLikes.setText(String.format("%s", items.get(position).getLikesCount()));
        holder.productComments.setText(String.format("%s", items.get(position).getCommentsCount()));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Updates the adapter item list with new list
     * @param newData list
     */
    public void updateAdapterData(ArrayList<Movies> newData) {
        this.items = newData;
    }
}
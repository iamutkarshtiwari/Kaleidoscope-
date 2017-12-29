package com.github.iamutkarshtiwari.kaleidoscope.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.iamutkarshtiwari.kaleidoscope.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {

    public ImageView productImage, soldOut;
    public TextView productPrice, productName, productLikes, productComments;

    public ProductViewHolder(View itemView) {
        super(itemView);

        productImage = (ImageView) itemView.findViewById(R.id.product_image);
        soldOut = (ImageView) itemView.findViewById(R.id.sold_out);
        productPrice = (TextView) itemView.findViewById(R.id.product_price);
        productName = (TextView) itemView.findViewById(R.id.product_name);
        productLikes = (TextView) itemView.findViewById(R.id.product_likes);
        productComments = (TextView) itemView.findViewById(R.id.product_comments);
    }
}

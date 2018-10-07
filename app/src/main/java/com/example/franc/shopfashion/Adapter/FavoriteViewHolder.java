package com.example.franc.shopfashion.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.franc.shopfashion.R;

public class FavoriteViewHolder extends RecyclerView.ViewHolder {
    public ImageView image_product;
    public TextView txt_product_name,txt_price;

    public RelativeLayout view_background;
    public LinearLayout view_foreground;
    public FavoriteViewHolder(View itemView) {
        super(itemView);
        image_product = itemView.findViewById(R.id.image_product);
        txt_product_name = itemView.findViewById(R.id.txt_product_name);
        txt_price = itemView.findViewById(R.id.txt_price);
        view_background = itemView.findViewById(R.id.view_background);
        view_foreground = itemView.findViewById(R.id.view_foreground);
    }
}

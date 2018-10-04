package com.example.franc.shopfashion.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.franc.shopfashion.R;

public class CartViewHolder extends RecyclerView.ViewHolder {
    public ImageView img_Product ;
    public TextView txt_product_name,txt_product_color,txt_product_size,txt_price;
    public ElegantNumberButton txt_amount;


    public CartViewHolder(View itemView) {
        super(itemView);

        img_Product = itemView.findViewById(R.id.image_product);
        txt_product_name = itemView.findViewById(R.id.txt_product_name);
        txt_product_color = itemView.findViewById(R.id.txt_product_color);
        txt_product_size = itemView.findViewById(R.id.txt_product_size);
        txt_price = itemView.findViewById(R.id.txt_price);
        txt_amount = itemView.findViewById(R.id.txt_amount);

    }
}

package com.example.franc.shopfashion.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.franc.shopfashion.Interface.ItemClickListener;
import com.example.franc.shopfashion.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public ImageView imageProduct;
    public TextView txtNameProduct;

    ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public MenuViewHolder(View itemView) {
        super(itemView);

        imageProduct =itemView.findViewById(R.id.image_product);
        txtNameProduct =itemView.findViewById(R.id.txt_name_product);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view);
    }
}

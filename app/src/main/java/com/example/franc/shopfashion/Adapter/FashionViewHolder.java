package com.example.franc.shopfashion.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.franc.shopfashion.Interface.ItemClickListener;
import com.example.franc.shopfashion.R;

public class FashionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public ImageView imageFashion;
    public TextView txt_fashion_name,txt_fashion_price;

    public Button btn_add_to_cart;

    ItemClickListener itemClickListener;
    public FashionViewHolder(View itemView) {
        super(itemView);
        imageFashion =itemView.findViewById(R.id.image_fashion);
        txt_fashion_name =itemView.findViewById(R.id.txt_fashion_name);
        txt_fashion_price=itemView.findViewById(R.id.txt_price);
        btn_add_to_cart=itemView.findViewById(R.id.btn_add_cart);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view);
    }
}

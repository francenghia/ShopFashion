package com.example.franc.shopfashion.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.franc.shopfashion.Common.Common;
import com.example.franc.shopfashion.Database.ModelDB.Cart;
import com.example.franc.shopfashion.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {
    private Context context;
    private List<Cart> cartList;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.cart_item_layout, null);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, final int position) {
        Picasso.with(context)
                .load(cartList.get(position).link)
                .into(holder.img_Product);

        holder.txt_product_name.setText(cartList.get(position).name);
        holder.txt_product_color.setText(new StringBuilder("Color :").append(cartList.get(position).color).toString());
        holder.txt_product_size.setText(new StringBuilder("Size :").append(cartList.get(position).size).toString());
        holder.txt_price.setText(new StringBuilder(String.valueOf(cartList.get(position).price)).append(" đồng").toString());
        holder.txt_amount.setNumber(String.valueOf(cartList.get(position).amount));

        holder.txt_amount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Cart cart = cartList.get(position);
                cart.amount = newValue;
                Common.cartRepository.updateToCart(cart);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void removeItem(int deleteIndex) {
        cartList.remove(deleteIndex);
        notifyDataSetChanged();
    }

    public void restoreItem(Cart deleteItem, int deleteIndex) {
        cartList.add(deleteIndex, deleteItem);
        notifyDataSetChanged();
    }
}

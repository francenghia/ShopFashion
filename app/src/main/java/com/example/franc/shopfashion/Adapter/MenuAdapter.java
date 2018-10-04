package com.example.franc.shopfashion.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.franc.shopfashion.Common.Common;
import com.example.franc.shopfashion.FashionActivity;
import com.example.franc.shopfashion.Interface.ItemClickListener;
import com.example.franc.shopfashion.Model.Fashion;
import com.example.franc.shopfashion.Model.Menu;
import com.example.franc.shopfashion.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuViewHolder>{

    private Context context;
    private List<Menu> menuList;

    public MenuAdapter(Context context, List<Menu> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(context).inflate(R.layout.menu_item,null);
        return new MenuViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, final int position) {
        Picasso.with(context).load(menuList.get(position).Link).into(holder.imageProduct);

        holder.txtNameProduct.setText(menuList.get(position).Name);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v) {
                Common.currentMenu = menuList.get(position);
                context.startActivity(new Intent(context, FashionActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }
}

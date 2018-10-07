package com.example.franc.shopfashion.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.franc.shopfashion.Database.ModelDB.Favorite;
import com.example.franc.shopfashion.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteViewHolder>{
    private Context context;
    private List<Favorite> favorites;

    public FavoriteAdapter(Context context, List<Favorite> favorites) {
        this.context = context;
        this.favorites = favorites;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fav_item_layout,null);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Picasso.with(context).load(favorites.get(position).link).into(holder.image_product);

        holder.txt_product_name.setText(favorites.get(position).name);
        holder.txt_price.setText(new StringBuilder(favorites.get(position).price).append(" đồng").toString());

    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public void removeItem(int position) {
        favorites.remove(position);
        notifyDataSetChanged();
    }

    public void restoreItem(Favorite item ,int position){
        favorites.add(position,item);
        notifyDataSetChanged();
    }
}

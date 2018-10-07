package com.example.franc.shopfashion.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.franc.shopfashion.Common.Common;
import com.example.franc.shopfashion.Database.ModelDB.Favorite;
import com.example.franc.shopfashion.DetailsProductFashion;
import com.example.franc.shopfashion.Interface.ItemClickListener;
import com.example.franc.shopfashion.Model.Fashion;
import com.example.franc.shopfashion.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FashionAdapter extends RecyclerView.Adapter<FashionViewHolder> {
    private Context context;
    private List<Fashion> fashionList;

    public FashionAdapter(Context context, List<Fashion> fashionList) {
        this.context = context;
        this.fashionList = fashionList;
    }

    @NonNull
    @Override
    public FashionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fashion_display_item, null);
        return new FashionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FashionViewHolder holder, final int position) {
        holder.txt_fashion_name.setText(fashionList.get(position).Name);
        holder.txt_fashion_price.setText(new StringBuilder("$").append(fashionList.get(position).Price));

        Picasso.with(context).load(fashionList.get(position).Link).into(holder.imageFashion);

        holder.btn_add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.currentFashion = fashionList.get(position);
                Intent intent = new Intent(context, DetailsProductFashion.class);
                intent.putExtra("position",fashionList.get(position)+"");
                context.startActivity(intent);
            }
        });

       //Change image
        if(Common.favoriteRepository.isFavorite(Integer.parseInt(fashionList.get(position).ID)) == 1) {
            holder.btn_favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
        else {
            holder.btn_favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }


        //Show Detail
        holder.btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Common.favoriteRepository.isFavorite(Integer.parseInt(fashionList.get(position).ID))!=1)
                {
                    addOrRemoveToFavorite(fashionList.get(position),true);
                    holder.btn_favorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                }else
                {
                    addOrRemoveToFavorite(fashionList.get(position),false);
                    holder.btn_favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                }
            }
        });

        //Favorite
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void addOrRemoveToFavorite(Fashion fashion, boolean isFavorite) {
        Favorite favorite = new Favorite();
        favorite.id = fashion.ID;
        favorite.link = fashion.Link;
        favorite.name = fashion.Name;
        favorite.price = fashion.Price;
        favorite.menuId = fashion.MenuId;

        if(isFavorite)
            Common.favoriteRepository.InsetFav(favorite);
        else
            Common.favoriteRepository.delete(favorite);
    }


    @Override
    public int getItemCount() {
        return fashionList.size();
    }
}

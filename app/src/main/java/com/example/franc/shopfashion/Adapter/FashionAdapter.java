package com.example.franc.shopfashion.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.franc.shopfashion.Common.Common;
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
    public void onBindViewHolder(@NonNull FashionViewHolder holder, final int position) {
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


        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return fashionList.size();
    }
}

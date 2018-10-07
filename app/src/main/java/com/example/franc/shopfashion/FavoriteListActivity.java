package com.example.franc.shopfashion;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.franc.shopfashion.Adapter.FavoriteAdapter;
import com.example.franc.shopfashion.Adapter.FavoriteViewHolder;
import com.example.franc.shopfashion.Common.Common;
import com.example.franc.shopfashion.Database.ModelDB.Favorite;
import com.example.franc.shopfashion.Utils.RecyclerItemTouchHelper;
import com.example.franc.shopfashion.Utils.RecyclerItemTouchHelperListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FavoriteListActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {
    RecyclerView recycler_fav;
    Button btn_buy_now;

    CompositeDisposable compositeDisposable;
    FavoriteAdapter adapter;
    List<Favorite> listFav = new ArrayList<>();

    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        compositeDisposable = new CompositeDisposable();


        rootLayout=(RelativeLayout) findViewById(R.id.rootLayout);

        btn_buy_now = findViewById(R.id.btn_buy_now);
        recycler_fav = findViewById(R.id.recyclerFav);
        recycler_fav.setLayoutManager(new LinearLayoutManager(this));
        recycler_fav.setHasFixedSize(true);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recycler_fav);

        loadFavoriteItem();

    }
    @Override
    protected void onResume() {
        super.onResume();
        loadFavoriteItem();
    }



    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    private void loadFavoriteItem() {
        compositeDisposable.add(Common.favoriteRepository.getFavItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Favorite>>() {
                    @Override
                    public void accept(List<Favorite> favorites) throws Exception {
                        displayFavoriteItem(favorites);
                    }
                }));
    }

    private void displayFavoriteItem(List<Favorite> favorites) {
        listFav =favorites;
        adapter = new FavoriteAdapter(this, favorites);
        recycler_fav.setAdapter(adapter);
    }



    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof FavoriteViewHolder)
        {
            String name = listFav.get(viewHolder.getAdapterPosition()).name;

            final Favorite deleteItem =listFav.get(viewHolder.getAdapterPosition());

            final int deleteIndex = viewHolder.getAdapterPosition();

            adapter.removeItem(deleteIndex);

            Common.favoriteRepository.delete(deleteItem);

            Snackbar snackbar= Snackbar.make(rootLayout,new StringBuilder(name).append(" removed from Favorite List").toString(),Snackbar.LENGTH_LONG);

            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.restoreItem(deleteItem,deleteIndex);
                    Common.favoriteRepository.InsetFav(deleteItem);

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();



        }
    }
}

package com.example.franc.shopfashion.Database.Local;

import com.example.franc.shopfashion.Database.DataSource.IFavoriteDataSource;
import com.example.franc.shopfashion.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public class FavoriteDataSource implements IFavoriteDataSource {

    private FavoriteDAOs favoriteDAOs;
    private static FavoriteDataSource instance;

    public FavoriteDataSource(FavoriteDAOs favoriteDAOs) {
        this.favoriteDAOs = favoriteDAOs;
    }

    public static FavoriteDataSource getInstance(FavoriteDAOs favoriteDAOs){
        if(instance ==null)
            instance = new FavoriteDataSource(favoriteDAOs);
        return instance;
    }

    @Override
    public Flowable<List<Favorite>> getFavItems() {
        return favoriteDAOs.getFavItems();
    }

    @Override
    public int isFavorite(int itemId) {
        return favoriteDAOs.isFavorite(itemId);
    }

    @Override
    public void InsetFav(Favorite... favorites) {
        favoriteDAOs.InsetFav(favorites);
    }

    @Override
    public void delete(Favorite favorite) {
        favoriteDAOs.delete(favorite);
    }
}

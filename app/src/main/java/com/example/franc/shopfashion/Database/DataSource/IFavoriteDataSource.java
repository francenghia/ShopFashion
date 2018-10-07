package com.example.franc.shopfashion.Database.DataSource;

import com.example.franc.shopfashion.Database.ModelDB.Favorite;

import java.util.List;

import io.reactivex.Flowable;

public interface IFavoriteDataSource {

    Flowable<List<Favorite>> getFavItems();
    int isFavorite(int itemId);
    void InsetFav(Favorite...favorites);
    void delete(Favorite favorite);

}

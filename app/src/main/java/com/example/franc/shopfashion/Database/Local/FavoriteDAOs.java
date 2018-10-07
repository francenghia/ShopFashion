package com.example.franc.shopfashion.Database.Local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.example.franc.shopfashion.Database.ModelDB.Favorite;
import java.util.List;
import io.reactivex.Flowable;

@Dao
public interface FavoriteDAOs {
    @Query("SELECT * FROM Favorite")
    Flowable<List<Favorite>> getFavItems();

    @Query("SELECT EXISTS (SELECT 1 FROM Favorite WHERE ID=:itemId)")
    int isFavorite(int itemId);

    @Insert
    void InsetFav(Favorite...favorites);

    @Delete
    void delete(Favorite favorite);
}

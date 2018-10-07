package com.example.franc.shopfashion.Database.Local;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.franc.shopfashion.Database.ModelDB.Cart;
import com.example.franc.shopfashion.Database.ModelDB.Favorite;

@Database(entities = {Cart.class, Favorite.class},version = 1)
public abstract class NNRoomDatabase extends RoomDatabase{
    public abstract CardDAOs cartDAOs();

    public abstract FavoriteDAOs favoriteDAOs();


    private static NNRoomDatabase instance ;

    public static NNRoomDatabase getInstance(Context context)
    {
        if(instance == null)
            instance = Room.databaseBuilder(context,NNRoomDatabase.class,"FRANCENGHIA_SHOPFASHION_DB")
            .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
            .build();
        return instance;
    }
}
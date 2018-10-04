package com.example.franc.shopfashion.Database.Local;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.franc.shopfashion.Database.ModelDB.Cart;

@Database(entities = {Cart.class},version = 2)
public abstract class CartDatabase extends RoomDatabase{
    public abstract CardDAOs cartDAOs();

    private static CartDatabase instance ;

    public static CartDatabase getInstance(Context context)
    {
        if(instance ==null)
            instance = Room.databaseBuilder(context,CartDatabase.class,"FRANCENGHIA_SHOPFASHION_DB")
            .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
            .build();
        return instance;
    }
}
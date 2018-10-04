package com.example.franc.shopfashion.Database.Local;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.franc.shopfashion.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CardDAOs {

    @Query("SELECT * FROM Cart")
    Flowable<List<Cart>> getCartItems();

    @Query("SELECT * FROM Cart WHERE id=:cartItemId")
    Flowable<List<Cart>> getCartItemById(int cartItemId);

    @Query("SELECT COUNT(*) from  Cart")
    int countCartItems();

    @Query("DELETE FROM Cart")
    int emptyCart();

    @Insert
    void insertToCart(Cart... carts);

    @Update
    void updateToCart(Cart... carts);

    @Delete
    void deleteToCart(Cart cart);


}

package com.example.franc.shopfashion.Database.DataSource;

import com.example.franc.shopfashion.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public interface ICartDataSource {
    Flowable<List<Cart>> getCartItems();
    Flowable<List<Cart>> getCartItemById(int cartItemId);
    int countCartItems();
    int emptyCart();
    void insertToCart(Cart...carts);
    void updateToCart(Cart...carts);
    void deleteToCart(Cart cart);
}

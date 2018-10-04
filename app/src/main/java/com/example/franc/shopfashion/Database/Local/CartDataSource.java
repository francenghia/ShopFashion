package com.example.franc.shopfashion.Database.Local;

import com.example.franc.shopfashion.Database.DataSource.ICartDataSource;
import com.example.franc.shopfashion.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.Flowable;

public class CartDataSource implements ICartDataSource{



    private CardDAOs cartDaos;

    private static CartDataSource instance;

    public CartDataSource(CardDAOs cartDaos) {
        this.cartDaos = cartDaos;
    }

    public static CartDataSource getInstance(CardDAOs cardDAOs)
    {
        if(instance == null)
            instance = new CartDataSource(cardDAOs);
            return instance;
    }

    @Override
    public Flowable<List<Cart>> getCartItems() {
        return cartDaos.getCartItems();
    }

    @Override
    public Flowable<List<Cart>> getCartItemById(int cartItemId) {
        return cartDaos.getCartItemById(cartItemId);
    }

    @Override
    public int countCartItems() {
        return cartDaos.countCartItems();
    }

    @Override
    public int emptyCart() {
        return cartDaos.emptyCart();
    }

    @Override
    public void insertToCart(Cart... carts) {
        cartDaos.insertToCart(carts);
    }

    @Override
    public void updateToCart(Cart... carts) {
        cartDaos.updateToCart(carts);
    }

    @Override
    public void deleteToCart(Cart cart) {
        cartDaos.deleteToCart(cart);
    }
}

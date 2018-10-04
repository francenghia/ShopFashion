package com.example.franc.shopfashion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.franc.shopfashion.Adapter.CartAdapter;
import com.example.franc.shopfashion.Common.Common;
import com.example.franc.shopfashion.Database.ModelDB.Cart;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CartActivity extends AppCompatActivity {
    RecyclerView recycler_cart;
    Button btn_buy_now;

    CompositeDisposable compositeDisposable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        compositeDisposable = new CompositeDisposable();
        btn_buy_now =findViewById(R.id.btn_buy_now);
        recycler_cart =findViewById(R.id.recyclerCart);
        recycler_cart.setHasFixedSize(true);
        recycler_cart.setLayoutManager(new LinearLayoutManager(this));

        btn_buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        loadCart();

    }

    private void loadCart() {
        compositeDisposable.add(
                Common.cartRepository.getCartItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Cart>>() {
                    @Override
                    public void accept(List<Cart> carts) throws Exception {
                        displayItemCart(carts);
                    }
                })
        );
    }

    private void displayItemCart(List<Cart> carts) {
        CartAdapter cartAdapter= new CartAdapter(this,carts);
        recycler_cart.setAdapter(cartAdapter);
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    boolean isBackButtonClicked = false;

    @Override
    public void onBackPressed() {
        if(isBackButtonClicked) {
            super.onBackPressed();
            return;
        }
        this.isBackButtonClicked =true;
        Toast.makeText(this, "Pleas click Back again to exit", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        isBackButtonClicked = false;
    }
}

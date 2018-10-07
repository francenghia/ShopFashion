package com.example.franc.shopfashion;

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
import android.widget.Toast;

import com.example.franc.shopfashion.Adapter.CartAdapter;
import com.example.franc.shopfashion.Adapter.CartViewHolder;
import com.example.franc.shopfashion.Adapter.FavoriteViewHolder;
import com.example.franc.shopfashion.Common.Common;
import com.example.franc.shopfashion.Database.ModelDB.Cart;
import com.example.franc.shopfashion.Database.ModelDB.Favorite;
import com.example.franc.shopfashion.Utils.RecyclerItemTouchHelper;
import com.example.franc.shopfashion.Utils.RecyclerItemTouchHelperListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CartActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {
    RecyclerView recycler_cart;
    Button btn_buy_now;

    CompositeDisposable compositeDisposable;
    CartAdapter cartAdapter;

    RelativeLayout rootLayout;

    List<Cart> cartList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        rootLayout =findViewById(R.id.rootLayout);
        compositeDisposable = new CompositeDisposable();
        btn_buy_now =findViewById(R.id.btn_buy_now);
        recycler_cart =findViewById(R.id.recyclerCart);
        recycler_cart.setHasFixedSize(true);

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recycler_cart);

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
        cartList =carts;
        cartAdapter= new CartAdapter(this,carts);
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


    @Override
    protected void onResume() {
        super.onResume();
        loadCart();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof CartViewHolder)
        {
            String name = cartList.get(viewHolder.getAdapterPosition()).name;

            final Cart deleteItem =cartList.get(viewHolder.getAdapterPosition());

            final int deleteIndex = viewHolder.getAdapterPosition();

            cartAdapter.removeItem(deleteIndex);

            Common.cartRepository.deleteToCart(deleteItem);

            Snackbar snackbar= Snackbar.make(rootLayout,new StringBuilder(name).append(" removed from Cart List").toString(),Snackbar.LENGTH_LONG);

            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cartAdapter.restoreItem(deleteItem,deleteIndex);
                    Common.cartRepository.insertToCart(deleteItem);

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();



        }
    }
}

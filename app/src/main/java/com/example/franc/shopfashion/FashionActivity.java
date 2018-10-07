package com.example.franc.shopfashion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.franc.shopfashion.Adapter.FashionAdapter;
import com.example.franc.shopfashion.Common.Common;
import com.example.franc.shopfashion.Model.Fashion;
import com.example.franc.shopfashion.Retrofit2Fashion.FashionAPIObservable;

import org.w3c.dom.Text;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FashionActivity extends AppCompatActivity {

    private FashionAPIObservable mService;
    private RecyclerView lst_fashion;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    TextView txt_banner_fashion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fashion);
        mService = Common.getAPI();
        txt_banner_fashion =findViewById(R.id.txt_name_fashion);
        lst_fashion =findViewById(R.id.recyclerFashion);
        lst_fashion.setLayoutManager(new GridLayoutManager(this,2));
        lst_fashion.setHasFixedSize(true);

        loadListFashion(Common.currentMenu.ID);
    }

    private void loadListFashion(String menuid) {
        compositeDisposable.add(mService.getFashion(menuid)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<List<Fashion>>() {
            @Override
            public void accept(List<Fashion> fashions) throws Exception {
                displayFashion(fashions);
            }
        }));
    }

    private void displayFashion(List<Fashion> fashions) {
        FashionAdapter fashionAdapter = new FashionAdapter(this,fashions);
        lst_fashion.setAdapter(fashionAdapter);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
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
        Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        isBackButtonClicked = false;
    }
}
package com.example.franc.shopfashion;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.franc.shopfashion.Adapter.FashionAdapter;
import com.example.franc.shopfashion.Common.Common;
import com.example.franc.shopfashion.Model.Fashion;
import com.example.franc.shopfashion.Retrofit2Fashion.FashionAPIObservable;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity {

    List<String> suggestList =new ArrayList<>();
    List<Fashion> localDataSource = new ArrayList<>();

    RecyclerView recycler_search;

    MaterialSearchBar searchBar;

    FashionAPIObservable mService;

    CompositeDisposable compositeDisposable =new CompositeDisposable();

    FashionAdapter adapter,searchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mService = Common.getAPI();
        recycler_search = findViewById(R.id.recyclerSearch);

        recycler_search.setLayoutManager(new GridLayoutManager(this,2));

        recycler_search.setHasFixedSize(true);

        searchBar =findViewById(R.id.searchBar);
        searchBar.setHint("Enter your fashion");

        loadAllShop();

        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggest = new ArrayList<>();
                for(String search : suggestList)
                {
                    if(search.toLowerCase().contains(searchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                searchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled)
                    recycler_search.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        List<Fashion> result = new ArrayList<>();
        for(Fashion fashion:localDataSource)
            if(fashion.Name.contains(text))
                result.add(fashion);
        searchAdapter = new FashionAdapter(this,result);
        recycler_search.setAdapter(searchAdapter);
    }

    private void loadAllShop() {
        compositeDisposable.add(mService.getAllShop()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new Consumer<List<Fashion>>() {
            @Override
            public void accept(List<Fashion> fashions) throws Exception {
                displayListShop(fashions);
                buildSuggestList(fashions);
            }
        }));
    }

    private void displayListShop(List<Fashion> fashions) {
        for(Fashion fashion:fashions)
            suggestList.add(fashion.Name);

        searchBar.setLastSuggestions(suggestList);
    }

    private void buildSuggestList(List<Fashion> fashions) {
        localDataSource = fashions;
        adapter = new FashionAdapter(this,fashions);
        recycler_search.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();

    }
}

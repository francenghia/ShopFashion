package com.example.franc.shopfashion.Common;

import com.example.franc.shopfashion.Database.DataSource.CartRepository;
import com.example.franc.shopfashion.Database.Local.CartDataSource;
import com.example.franc.shopfashion.Database.Local.CartDatabase;
import com.example.franc.shopfashion.Model.Fashion;
import com.example.franc.shopfashion.Model.Menu;
import com.example.franc.shopfashion.Model.User;
import com.example.franc.shopfashion.Retrofit2Fashion.FashionAPIObservable;
import com.example.franc.shopfashion.Retrofit2Fashion.RetrofitClient;

public class Common {
    public static final String BASE_URL = "http://192.168.4.135/fashionshop/";

    public static FashionAPIObservable getAPI(){
        return RetrofitClient.getClient(BASE_URL).create(FashionAPIObservable.class);
    }

    public static User currentUser = null;

    public static Menu currentMenu = null;

    public static Fashion currentFashion = null ;

    //Hold field

    public static String sizeOfFashion = "-1";  //-1: no choose 0 = M ; 1 = L ; 2 = XL ;3 =XXl

    public static String chooseColor = "-1"; //-1 no choose

    //Database
    public static CartDatabase cartDatabase;
    public static CartRepository cartRepository;


}

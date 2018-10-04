package com.example.franc.shopfashion.Retrofit2Fashion;

import com.example.franc.shopfashion.Model.Banner;
import com.example.franc.shopfashion.Model.Fashion;
import com.example.franc.shopfashion.Model.IsUserRes;
import com.example.franc.shopfashion.Model.Menu;
import com.example.franc.shopfashion.Model.User;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FashionAPIObservable {
    @FormUrlEncoded
    @POST("checkuser.php")
    Call<IsUserRes> checkUserExists(@Field("phone")String phone);

    @FormUrlEncoded
    @POST("register.php")
    Call<User> registerNewUser(@Field("phone")String phone,
                               @Field("name")String name,
                               @Field("address")String address,
                               @Field("email")String email);

    @FormUrlEncoded
    @POST("getfashion.php")
    Observable<List<Fashion>> getFashion(@Field("menuid")String menuID);

    @FormUrlEncoded
    @POST("getuser.php")
    Call<User> getUserInformation(@Field("phone")String phone);

    @POST("getbanner.php")
    Observable<List<Banner>> getBanner();


    @POST("getmenu.php")
    Observable<List<Menu>> getMenu();

    @Multipart
    @POST("upload.php")
    Call<String> uploadFile(@Part MultipartBody.Part phone, @Part MultipartBody.Part flie);

}

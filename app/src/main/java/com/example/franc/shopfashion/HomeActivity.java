package com.example.franc.shopfashion;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.franc.shopfashion.Adapter.MenuAdapter;
import com.example.franc.shopfashion.Common.Common;
import com.example.franc.shopfashion.Database.DataSource.CartRepository;
import com.example.franc.shopfashion.Database.DataSource.FavoriteRepository;
import com.example.franc.shopfashion.Database.Local.CartDataSource;
import com.example.franc.shopfashion.Database.Local.FavoriteDataSource;
import com.example.franc.shopfashion.Database.Local.NNRoomDatabase;
import com.example.franc.shopfashion.Interface.UploadCallBack;
import com.example.franc.shopfashion.Model.Banner;
import com.example.franc.shopfashion.Model.Menu;
import com.example.franc.shopfashion.Retrofit2Fashion.FashionAPIObservable;
import com.example.franc.shopfashion.Utils.ProgressRequestBody;
import com.facebook.accountkit.AccountKit;
import com.ipaulpro.afilechooser.utils.FileUtils;
import com.nex3z.notificationbadge.NotificationBadge;
import com.squareup.picasso.Picasso;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements UploadCallBack {
    private Toolbar toolbar;
    private FrameLayout root;
    private View contentHamburger;
    private static final int PICK_FILE_REQUEST = 1222;
    private static final long RIPPLE_DURATION = 250;

    private SliderLayout sliderLayout;
    private FashionAPIObservable mService;

    private RecyclerView lst_menu;
    //Rxjava
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    private NotificationBadge badge;
    CircleImageView img_avatar;

    ImageView cart_icon;

    Uri selectFileUri;

    //nav
    private TextView txtLogout ,txtFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }

        contentHamburger = findViewById(R.id.content_hamburger);
        lst_menu = findViewById(R.id.lst_menu);

        mService = Common.getAPI();

        lst_menu.setLayoutManager(new GridLayoutManager(this, 2));
        lst_menu.setHasFixedSize(true);


        sliderLayout = findViewById(R.id.slider);
        root = findViewById(R.id.root);


        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.design_navigation_bar_home, null);
        TextView txtName = guillotineMenu.findViewById(R.id.txtName);
        TextView txtPhone = guillotineMenu.findViewById(R.id.txtPhone);
        img_avatar = guillotineMenu.findViewById(R.id.img_avatar);
        txtLogout = guillotineMenu.findViewById(R.id.txtLogout);
        txtFavorite=guillotineMenu.findViewById(R.id.txtFavorite);


        //Log out
        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getSupportActionBar().getThemedContext());
                //
                builder.setTitle("Exit Application");
                builder.setMessage("Do you want to exit this application !");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AccountKit.logOut();
                        Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });


        txtFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,FavoriteListActivity.class));
            }
        });

        root.addView(guillotineMenu);
        img_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        txtName.setText(Common.currentUser.getName());
        txtPhone.setText(Common.currentUser.getPhone());


        if (!TextUtils.isEmpty(Common.currentUser.getAvatarURL())) {
            Picasso.with(this)
                    .load(String.valueOf(new StringBuilder(Common.BASE_URL)
                    .append("user_avatar/")
                    .append(Common.currentUser.getAvatarURL().toString())))
                    .into(img_avatar);
        }


        new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();
        //getBanner
        getBannerImage();
        //getMenu
        getMenu();

        initDB();

    }

    private void chooseImage() {
        startActivityForResult(Intent.createChooser(FileUtils.createGetContentIntent(), "Select a file"), PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_FILE_REQUEST) {
                if (data != null) {

                    selectFileUri = data.getData();

                    if (selectFileUri != null && !selectFileUri.getPath().isEmpty()) {
                        img_avatar.setImageURI(selectFileUri);
                        uploadFile();
                    } else {
                        Toast.makeText(this, "Can't upload file to server", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void uploadFile() {
        if (selectFileUri != null) {

            File file = FileUtils.getFile(this, selectFileUri);

            String filename = String.valueOf(new StringBuilder(Common.currentUser.getPhone()).append(FileUtils.getExtension(file.toString())));

            Log.d("Kiem tra", filename);

            final ProgressRequestBody requestBody = new ProgressRequestBody(file, this);

            final MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", filename, requestBody);

            final MultipartBody.Part userPhone = MultipartBody.Part.createFormData("phone", Common.currentUser.getPhone());

            new Thread(new Runnable() {
                @Override
                public void run() {
                    mService.uploadFile(userPhone, body)
                            .enqueue(new Callback<String>() {
                                @Override
                                public void onResponse(Call<String> call, Response<String> response) {
                                    Toast.makeText(HomeActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(Call<String> call, Throwable t) {
                                    Toast.makeText(HomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }).start();
        }else
        {
            Log.d("Kiem tra err","Error");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_bar, menu);
        View view = menu.findItem(R.id.cart_menu).getActionView();
        badge = (NotificationBadge) view.findViewById(R.id.badge);
        cart_icon = view.findViewById(R.id.cart_icon);
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });
        updateCartCount();
        return true;
    }

    private void updateCartCount() {
        if (badge == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Common.cartRepository.countCartItems() == 0) {
                    badge.setVisibility(View.INVISIBLE);
                } else {
                    badge.setVisibility(View.VISIBLE);
                    badge.setText(String.valueOf(Common.cartRepository.countCartItems()));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.cart_menu) {
            return true;
        }else if(id == R.id.search_menu){
            startActivity(new Intent(HomeActivity.this,SearchActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initDB() {
        Common.nnRoomDatabase = NNRoomDatabase.getInstance(this);
        Common.cartRepository = CartRepository.getInstance(CartDataSource.getInstance(Common.nnRoomDatabase.cartDAOs()));
        Common.favoriteRepository = FavoriteRepository.getInstance(FavoriteDataSource.getInstance(Common.nnRoomDatabase.favoriteDAOs()));
    }

    private void getMenu() {
        compositeDisposable.add(mService.getMenu()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Menu>>() {
                    @Override
                    public void accept(List<Menu> menus) throws Exception {
                        displayMenu(menus);
                    }
                }));
    }

    private void displayMenu(List<Menu> menus) {
        MenuAdapter adapter = new MenuAdapter(this, menus);
        lst_menu.setAdapter(adapter);
    }


    private void getBannerImage() {
        compositeDisposable.add(mService.getBanner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Banner>>() {
                    @Override
                    public void accept(List<Banner> banners) throws Exception {
                        displayImage(banners);
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    private void displayImage(List<Banner> banners) {
        HashMap<String, String> bannerMap = new HashMap<>();
        for (Banner item : banners)
            bannerMap.put(item.getName(), item.getLink());

        for (String name : bannerMap.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView.description(name)
                    .image(bannerMap.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            sliderLayout.addSlider(textSliderView);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();
        isBackButtonClicked =false;
    }

    @Override
    public void onProgressUpdate(int pertantage) {

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
}

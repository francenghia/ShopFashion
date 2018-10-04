package com.example.franc.shopfashion;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.franc.shopfashion.Common.Common;
import com.example.franc.shopfashion.Database.Local.CartDatabase;
import com.example.franc.shopfashion.Model.IsUserRes;
import com.example.franc.shopfashion.Model.User;
import com.example.franc.shopfashion.Retrofit2Fashion.FashionAPIObservable;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 1000;

    public static final int REQUEST_PERMISSION = 1001;

    private Button btnContinue;

    FashionAPIObservable mService;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
            break;
            default:
                break;

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);

        mService = Common.getAPI();

        printKeyHash();
        btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginPage(LoginType.PHONE);
            }
        });

        if (AccountKit.getCurrentAccessToken() != null) {
            final AlertDialog dialog = new SpotsDialog(this);
            dialog.show();
            dialog.setMessage("Please waiting...");

            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(final Account account) {
                    mService.checkUserExists(account.getPhoneNumber().toString())
                            .enqueue(new Callback<IsUserRes>() {
                                @Override
                                public void onResponse(Call<IsUserRes> call, Response<IsUserRes> response) {
                                    IsUserRes userResponse = response.body();
                                    if (userResponse.isExists()) {
                                        mService.getUserInformation(account.getPhoneNumber().toString())
                                                .enqueue(new Callback<User>() {
                                                    @Override
                                                    public void onResponse(Call<User> call, Response<User> response) {
                                                        dialog.dismiss();
                                                        Common.currentUser = response.body();
                                                        startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                                        finish();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<User> call, Throwable t) {
                                                        Toast.makeText(MainActivity.this, t.getMessage() + "Kiem tra", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    } else {
                                        dialog.dismiss();
                                        ShowRegisterDialog(account.getPhoneNumber().toString());
                                    }
                                }

                                @Override
                                public void onFailure(Call<IsUserRes> call, Throwable t) {
                                    Toast.makeText(MainActivity.this, "Error 1 :" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }

                @Override
                public void onError(AccountKitError accountKitError) {
                    Log.d("Error", accountKitError.getErrorType().getMessage() + "");
                }
            });
        }

    }

    private void startLoginPage(LoginType phone) {
        Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder builder = new
                AccountKitConfiguration.AccountKitConfigurationBuilder(phone, AccountKitActivity.ResponseType.TOKEN);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, builder.build());
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

            if (result.getError() != null) {
                Toast.makeText(this, result.getError().getErrorType().getMessage() + "", Toast.LENGTH_SHORT).show();
            } else if (result.wasCancelled()) {
                Toast.makeText(this, "Cancle", Toast.LENGTH_SHORT).show();
            } else {
                if (result.getAccessToken() != null) {
                    final AlertDialog dialog = new SpotsDialog(this);
                    dialog.show();
                    dialog.setMessage("Please waiting...");


                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(final Account account) {
                            mService.checkUserExists(account.getPhoneNumber().toString())
                                    .enqueue(new Callback<IsUserRes>() {
                                        @Override
                                        public void onResponse(Call<IsUserRes> call, Response<IsUserRes> response) {
                                            IsUserRes userResponse = response.body();
                                            if (userResponse.isExists()) {
                                                mService.getUserInformation(account.getPhoneNumber().toString())
                                                        .enqueue(new Callback<User>() {
                                                            @Override
                                                            public void onResponse(Call<User> call, Response<User> response) {
                                                                dialog.dismiss();

                                                                //Fixed first time crash
                                                                Common.currentUser = response.body();
                                                                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                                                finish();
                                                            }

                                                            @Override
                                                            public void onFailure(Call<User> call, Throwable t) {
                                                                Toast.makeText(MainActivity.this, t.getMessage() + "", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                            } else {
                                                dialog.dismiss();
                                                ShowRegisterDialog(account.getPhoneNumber().toString());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<IsUserRes> call, Throwable t) {
                                            Toast.makeText(MainActivity.this, "Error 1 :" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                        @Override
                        public void onError(AccountKitError accountKitError) {
                            Log.d("Error", accountKitError.getErrorType().getMessage() + "");
                        }
                    });
                }
            }
        }
    }

    private void ShowRegisterDialog(final String phone) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("REGISTER");


        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.fashion_register, null);

        final MaterialEditText edtName = view.findViewById(R.id.edtName);
        final MaterialEditText edtEmail = view.findViewById(R.id.edtEmail);
        final MaterialEditText edtAddress = view.findViewById(R.id.edtAddress);

        Button btnRegister = view.findViewById(R.id.btnRegister);

        alertDialog.setView(view);
        final AlertDialog dialog = alertDialog.create();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

                if (TextUtils.isEmpty(edtName.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edtEmail.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(edtAddress.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter your address", Toast.LENGTH_SHORT).show();
                    return;
                }
                final android.app.AlertDialog dialog = new SpotsDialog(MainActivity.this);
                dialog.show();
                dialog.setMessage("Please waiting...");

                mService.registerNewUser(phone
                        , edtName.getText().toString()
                        , edtAddress.getText().toString()
                        , edtEmail.getText().toString())
                        .enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                dialog.dismiss();
                                User user = response.body();
                                if (TextUtils.isEmpty(user.getError_msg())) {

                                    Toast.makeText(MainActivity.this, "User register successfully", Toast.LENGTH_SHORT).show();

                                    Common.currentUser = response.body();
                                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                                    finish();
                                }

                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {

                            }
                        });

            }
        });

        dialog.show();
    }

    private void printKeyHash() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo("com.example.franc.shopfashion",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KEYHASH", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
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
        isBackButtonClicked = false;
        super.onResume();
    }
}

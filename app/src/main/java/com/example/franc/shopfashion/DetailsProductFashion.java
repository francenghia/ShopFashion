package com.example.franc.shopfashion;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.franc.shopfashion.Common.Common;
import com.example.franc.shopfashion.Database.ModelDB.Cart;
import com.example.franc.shopfashion.Model.Fashion;
import com.example.franc.shopfashion.Retrofit2Fashion.FashionAPIObservable;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import io.reactivex.disposables.CompositeDisposable;

public class DetailsProductFashion extends AppCompatActivity {
    private boolean isOpen = false;
    private ConstraintSet layout1, layout2;
    private ConstraintLayout constraintLayout;
    private ImageView imageExpanded;
    String position = "";

    //layout constrainlayouts

    ImageButton backToActivity;
    TextView txt_name_product_fashion;
    ElegantNumberButton txt_count;
    RadioButton radio_sizeM, radio_sizeL, radio_sizeXL, radio_sizeXXL,radio_color1,radio_color2,radio_color3,radio_color4;
    ImageView imageView2;
    Button btn_add_comfirm;
    Intent intent = getIntent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_product_fashion);
        position = Common.currentFashion.ID;
        Log.d("kiem tra 1:",position);

        layout1 = new ConstraintSet();
        layout2 = new ConstraintSet();

        imageExpanded = findViewById(R.id.imageExpanded);
        constraintLayout = findViewById(R.id.constrain_layout);
        btn_add_comfirm =findViewById(R.id.btn_add_comfirm);

        backToActivity = findViewById(R.id.backToActivity);
        txt_name_product_fashion=findViewById(R.id.txt_name_product_fashion);
        imageView2 =findViewById(R.id.imageView2);
        txt_count = findViewById(R.id.txt_count);

        radio_sizeM = findViewById(R.id.radio_sizeM);
        radio_sizeL = findViewById(R.id.radio_sizeL);
        radio_sizeXL = findViewById(R.id.radio_sizeXL);
        radio_sizeXXL = findViewById(R.id.radio_sizeXXL);

        radio_sizeM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Common.sizeOfFashion ="M" ;
                }
            }
        });
        radio_sizeL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Common.sizeOfFashion = "L" ;
                }
            }
        });
        radio_sizeXL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Common.sizeOfFashion = "XL" ;
                }
            }
        });
        radio_sizeXXL.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Common.sizeOfFashion ="XXL";
                }
            }
        });


        radio_color1 = findViewById(R.id.radio_color1);
        radio_color2 = findViewById(R.id.radio_color2);
        radio_color3 = findViewById(R.id.radio_color3);
        radio_color4 = findViewById(R.id.radio_color4);
        
        radio_color1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Common.chooseColor = "RED";
                }
            }
        });
        radio_color2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Common.chooseColor = "BLUE";
                }
            }
        });
        radio_color3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Common.chooseColor = "YELLOW";
                }
            }
        });
        radio_color4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Common.chooseColor = "GREEN";
                }
            }
        });




        Picasso.with(getApplicationContext()).load(Common.currentFashion.Link).into(imageView2);
        txt_name_product_fashion.setText(Common.currentFashion.Name);



        layout2.clone(this, R.layout.details_fashion_expanded);
        layout1.clone(constraintLayout);



        Log.d("kiem tra", Common.currentFashion.ID + "/" + Common.currentFashion.MenuId);

        imageExpanded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOpen) {
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    layout2.applyTo(constraintLayout);
                    isOpen = !isOpen;
                } else {
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    layout1.applyTo(constraintLayout);
                    isOpen = !isOpen;
                }
            }
        });

        backToActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackToActivityMenu();
            }
        });

        btn_add_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Common.sizeOfFashion == "-1"){
                    Toast.makeText(DetailsProductFashion.this, "Please choose size of product", Toast.LENGTH_SHORT).show();
                    return ;
                }
                if(Common.chooseColor == "-1"){
                    Toast.makeText(DetailsProductFashion.this, "Please choose color of product", Toast.LENGTH_SHORT).show();
                    return ;
                }

                showComfirmDialog(position,txt_count.getNumber());
            }
        });


    }

    private void showComfirmDialog(String position, final String number) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.comfirm_add_to_cart,null);
        //View
        ImageView img_product = view.findViewById(R.id.img_product);
        final TextView txt_cart_product_name = view.findViewById(R.id.txt_cart_product_name);
        TextView txt_size_product = view.findViewById(R.id.txt_size_product);
        TextView txt_color_product = view.findViewById(R.id.txt_color_product);
        final TextView txt_cart_product_price = view.findViewById(R.id.txt_cart_product_price);


        Picasso.with(getApplicationContext()).load(Common.currentFashion.Link).into(img_product);
        txt_cart_product_name.setText(new StringBuilder(Common.currentFashion.Name).toString());


        txt_size_product.setText(new StringBuilder("Size :").append(Common.sizeOfFashion).toString());
        txt_color_product.setText(new StringBuilder("Color :").append(Common.chooseColor).toString());

        final double price = Double.parseDouble(Common.currentFashion.Price)*Double.parseDouble(number);

        txt_cart_product_price.setText(new StringBuilder("Price :").append(price)+" đồng");

        dialog.setNegativeButton("COMFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                try {
                    Cart cartItem = new Cart();
                    cartItem.name = txt_cart_product_name.getText().toString();
                    cartItem.amount = Integer.parseInt(number);
                    cartItem.size = Common.sizeOfFashion;
                    cartItem.color = Common.chooseColor;
                    cartItem.price = price;
                    cartItem.link = Common.currentFashion.Link;


                    Common.cartRepository.insertToCart(cartItem);
                    Log.d("kiem tra2", new Gson().toJson(cartItem));
                    Toast.makeText(DetailsProductFashion.this, "Save item to cart successfully", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(DetailsProductFashion.this, e.getMessage()+"", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.setPositiveButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }


    private void BackToActivityMenu() {
        Intent intent = new Intent(DetailsProductFashion.this,FashionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        finish();
    }


}

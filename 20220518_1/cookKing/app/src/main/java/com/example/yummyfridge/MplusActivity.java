package com.example.yummyfridge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.Buffer;
import java.util.Random;

public class MplusActivity extends AppCompatActivity {

    Button btn_addFood;
    EditText addIngredients;
    DatePicker date;
    //Spinner addType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);

        Button mbtn_url;

        //setContentView(R.layout.activity_main);
        mbtn_url = findViewById(R.id.ssg);

        mbtn_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st = "https://emart.ssg.com/search.ssg?target=all&query=";
                String name = addIngredients.getText().toString().trim();
                st = st.concat(name);
                Intent urlintent = new Intent(Intent.ACTION_VIEW, Uri.parse(st));
                startActivity(urlintent);
            }
        });

        Button mbtn_url2;

        //setContentView(R.layout.activity_main);
        mbtn_url2 = findViewById(R.id.coupang);

        mbtn_url2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String st = "https://www.coupang.com/np/search?rocketAll=true&q=";
                String name = addIngredients.getText().toString().trim();
                st = st.concat(name);
                st = st.concat("&brand=&offerCondition=&filter=&availableDeliveryFilter=&filterType=rocket_wow%2Ccoupang_global&isPriceRange=false&priceRange=&minPrice=&maxPrice=&page=1&trcid=&traid=&filterSetByUser=true&channel=user&backgroundColor=&searchProductCount=1254812&component=&rating=0&sorter=scoreDesc&listSize=36");
                Intent urlintent = new Intent(Intent.ACTION_VIEW, Uri.parse(st));
                startActivity(urlintent);
            }
        });

        //메인 화면으로 전환
        ImageButton btnmanager=(ImageButton)findViewById(R.id.btnmanager);
        btnmanager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent outIntent=new Intent(getApplicationContext(), MainActivity.class);
                setResult(RESULT_OK,outIntent);
                finish();
            }
        });

        //식재료 이름 가져오기
        addIngredients=(EditText)findViewById(R.id.addIngredients);

        //날짜(유통기한) 가져오기
        date=(DatePicker)findViewById(R.id.addDate);
        date.init(2022, 6, 20, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {

            }
        });

        //식재료 타입 가져오기
        // addType = (Spinner)findViewById(R.id.addType);
        ArrayAdapter typeAdapter = ArrayAdapter.createFromResource(this, R.array.foodType, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //addType.setAdapter(typeAdapter);
        //스피너 초기화 date.setSelection(1);


        //식재료 추가
        btn_addFood= (Button)findViewById(R.id.btn_addFood);
        btn_addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(MplusActivity.this);
                String fulldate= date.getYear()+"/"+(date.getMonth()+1)+"/"+date.getDayOfMonth();
                myDB.addFridge(addIngredients.getText().toString().trim(), "NULL", fulldate.trim());
            }
        });

    }

}

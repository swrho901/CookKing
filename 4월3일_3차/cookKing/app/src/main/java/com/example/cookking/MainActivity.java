package com.example.cookking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.media.Image;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private CustomDialog customDialog;

    ImageButton btn_add;
    ImageButton btn_add_material;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ArrayList<SampleData> movieDataList;
    ArrayList<MaterialData> materialDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 왼쪽 상단 버튼 만들기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24); //왼쪽 상단 버튼 아이콘 지정

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.navigation_view);

        final ArrayList<SampleData> items =new ArrayList<SampleData>();
        this.InitializeMovieData();
        ListView listView1 = (ListView)findViewById(R.id.listView1);
        MyAdapter myAdapter = new MyAdapter(this,movieDataList);

        listView1.setAdapter(myAdapter);

        final ArrayList<MaterialData> items2 = new ArrayList<MaterialData>();
        this.InitializeMaterialData();
        ListView listview2 = (ListView)findViewById(R.id.listView2);
        MyAdapterTwo myAdapter2 = new MyAdapterTwo(this,materialDataList);

        listview2.setAdapter(myAdapter2);

        // 여기 에러 일으킬 수 있으니깐 조심해서 사용
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id){
                Toast.makeText(getApplicationContext(),
                        myAdapter.getItem(position).getMovieName(),
                        Toast.LENGTH_LONG).show();
            }
        });
        Button.OnClickListener btnListener = new View.OnClickListener() {
            public void onClick(View v) {

                switch (v.getId()){

                    case R.id.addRecipe:

                        customDialog = new CustomDialog(MainActivity.this,"레시피 정보를 입력해주세요.");
                        customDialog.show();

                        int count = myAdapter.getCount();
                        // 아이템 추가.
                        movieDataList.add(new SampleData(R.drawable.plus, "어벤져스","12세 이상관람가"));
                        // listview 갱신
                        myAdapter.notifyDataSetChanged();


                        break;

                    case R.id.addMaterial:

                        customDialog = new CustomDialog(MainActivity.this,"재료 정보를 입력해주세요.");
                        customDialog.show();

                        count = myAdapter2.getCount();
                        // 아이템 추가.
                        materialDataList.add(new MaterialData(R.drawable.plus, "마늘","2022 7월 7일"));
                        // listview 갱신
                        myAdapter2.notifyDataSetChanged();


                        break;
                }
            }
        } ;

        btn_add = (ImageButton)findViewById(R.id.addRecipe) ;
        btn_add.setOnClickListener(btnListener);
        btn_add_material = (ImageButton)findViewById(R.id.addMaterial) ;
        btn_add_material.setOnClickListener(btnListener);

        try {

            TabHost th = (TabHost) findViewById(R.id.host);
            th.setup();

            TabHost.TabSpec spec = th.newTabSpec("Tab One");
            spec.setContent(R.id.tab1);
            spec.setIndicator("RECIPE");
            th.addTab(spec);

            spec = th.newTabSpec("Tab Two");
            spec.setContent(R.id.tab2);
            spec.setIndicator("MATERIAL");
            th.addTab(spec);

            spec = th.newTabSpec("Tab Three");
            spec.setContent(R.id.tab3);
            spec.setIndicator("Tab 3");
            th.addTab(spec);

        }catch(Exception e){
            Toast.makeText(this,""+e.getMessage(), Toast.LENGTH_LONG).show();
        }


        //다이얼로그 밖의 화면은 흐리게 만들어줌
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);

    }


    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { // 왼쪽 상단 버튼 눌렀을 때
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override public void onBackPressed() { //뒤로가기 했을 때
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void InitializeMovieData()
    {
        movieDataList = new ArrayList<SampleData>();

        //movieDataList.add(new SampleData(R.drawable.plus, "미션임파서블","15세 이상관람가"));
        //movieDataList.add(new SampleData(R.drawable.plus, "아저씨","19세 이상관람가"));
        //movieDataList.add(new SampleData(R.drawable.plus, "어벤져스","12세 이상관람가"));
    }
    public void InitializeMaterialData(){
        materialDataList = new ArrayList<MaterialData>();

    }

}

package com.example.cookKing;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {

    EditText recipe_search; // 바뀐 부분
    //Data[] list_data;
    String val = "";
    String search_name = "";
    String date1;
    public int dateIndex = 0;
    String[] datelst = new String[30];

    MyDatabaseHelper myDB;
    ArrayList <String> fridge_ingredients, fridge_type, fridge_date;
    ArrayList<String> recipe_name, recipe_info, recipe_image, recipe_star;
    ArrayList<String> star_name, star_info, star_image, star_star, star_sql;
    ListView fridge_listView, recipe_listView, star_listView;
    ListViewAdapter fridge_adapter;
    RecipeListViewAdapter recipe_adapter, star_adapter;
    TextView myFridge;
    Bitmap bmp;
    int count=0, count2=0, view_type=0;


    private AlarmManager alarmManager;
    private GregorianCalendar mCalender;

    private NotificationManager notificationManager;
    NotificationCompat.Builder builder;

    Button addShop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateIndex = 0;

        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec spec;

        // 탭에 넣을 이미지 설정
        ImageView tabwidget01 = new ImageView(this);
        tabwidget01.setImageResource(R.drawable.refrigerator);

        ImageView tabwidget02 = new ImageView(this);
        tabwidget02.setImageResource(R.drawable.recipe);

        ImageView tabwidget04 = new ImageView(this);
        tabwidget04.setImageResource(R.drawable.search_icon);

        //탭에 이미지 생성한 것 넣어주기
        TabHost.TabSpec tabFridge = tabHost.newTabSpec("FRIDGE").setIndicator(tabwidget01);
        tabFridge.setContent(R.id.fridge);
        tabHost.addTab(tabFridge);

        TabHost.TabSpec tabRecipe = tabHost.newTabSpec("Recipe").setIndicator(tabwidget02);
        tabRecipe.setContent(R.id.recipe);
        tabHost.addTab(tabRecipe);

        TabHost.TabSpec tabBookmark = tabHost.newTabSpec("Bookmark").setIndicator(tabwidget04);
        tabBookmark.setContent(R.id.search);
        tabHost.addTab(tabBookmark);

        fridge_listView = (ListView) findViewById(R.id.fridge_list);
        fridge_adapter = new ListViewAdapter(MainActivity.this);
        fridge_listView.setAdapter(fridge_adapter);

        recipe_listView = (ListView) findViewById(R.id.sqlResult);
        recipe_adapter = new RecipeListViewAdapter(MainActivity.this);
        recipe_listView.setAdapter(recipe_adapter);

        star_listView = (ListView) findViewById(R.id.starResult);
        star_adapter = new RecipeListViewAdapter(MainActivity.this);
        star_listView.setAdapter(star_adapter);

        //검색기능

        recipe_search = findViewById(R.id.recipe_search);
        ImageButton btn123 = (ImageButton) findViewById(R.id.btnmanager2);
        btn123.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = recipe_search.getText().toString().trim();

                search_name = temp;
                searchR();
                int a = 0;
            }
        });


        star_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                intent.putExtra("recipe_name", star_name.get(i));
                intent.putExtra("recipe_image",star_image.get(i));
                startActivityForResult(intent,2); // 1 ??
            }
        });
        //알람기능
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        mCalender = new GregorianCalendar();

        Log.v("HelloAlarmActivity", mCalender.getTime().toString());


        // 식재료 추가 페이지로 넘어가기
        ImageButton btnmanager = (ImageButton) findViewById(R.id.btnmanager);
        btnmanager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MplusActivity.class);
                startActivityForResult(intent,0);
            }
        });

        //냉장고 DB불러오기
        myDB = new MyDatabaseHelper(MainActivity.this);
        fridge_ingredients = new ArrayList<>();
        fridge_type = new ArrayList<>();
        fridge_date = new ArrayList<>();
        storeDataInArrays();
        displayData();

        //알람
        setAlarm();

        //재료 삭제
        fridge_listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                MyDatabaseHelper myDB2 = new MyDatabaseHelper(MainActivity.this);
                myDB2.deleteOneRow(fridge_ingredients.get(i));
                fridge_ingredients.remove(i);
                fridge_type.remove(i);
                fridge_date.remove(i);
                //fridge_adapter.removeItem(i);
                //fridge_listView.setAdapter(fridge_adapter);
                restart(MainActivity.this);
                return false;
            }
        });
        myFridge=(TextView)findViewById(R.id.myFridge);

        //레시피 추천 함수
        getVal();

        //레시피 상세보기 페이지로
        recipe_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                intent.putExtra("recipe_name", recipe_name.get(i));
                intent.putExtra("recipe_image",recipe_image.get(i));
                startActivityForResult(intent,1);
            }
        });

        }


        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            restart(MainActivity.this);
        }

    public void setAlarm() {
        //AlarmReceiver에 값 전달
        Intent receiverIntent = new Intent(MainActivity.this, AlarmRecevier.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, receiverIntent, 0);

        String from = "2022-05-25 20:26:00"; //임의로 날짜와 시간을 지정
        String from2 = "";
        for(int i=0;i<dateIndex;i++) {
            String[] ymd = datelst[i].split("/");
            from2 = from2.concat(ymd[0] + "-");
            if (ymd[1].length() == 2) {
                from2 = from2.concat(ymd[1] + "-");
            } else {
                from2 = from2.concat("0" + ymd[1] + "-");
            }
            if (ymd[2].length() == 2) {
                from2 = from2.concat(ymd[2] + " ");
            } else {
                from2 = from2.concat("0" + ymd[2] + " ");
            }
            from2 = from2.concat("11:00:00");
            from = from2;

            //날짜 포맷을 바꿔주는 소스코드
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date datetime = null;
            try {
                datetime = dateFormat.parse(from);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(datetime);

            alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

        }
    }

    public void searchR(){
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        StarDatabaseHelper dbHelper2 = new StarDatabaseHelper(this);
        SQLiteDatabase db2 = dbHelper2.getReadableDatabase();
        int star=0;
        String[] strData=fridge_ingredients.toArray(new String[fridge_ingredients.size()]);

        star_name=new ArrayList<>();
        star_info=new ArrayList<>();
        star_image=new ArrayList<>();
        star_star=new ArrayList<>();

        if(search_name != ""){
            String addSql="A.IRDNT_NM=? ";
            for(int i=0;i<strData.length-1;i++){
                addSql+="OR A.IRDNT_NM=? ";
            }

            String sqlsql = "SELECT B.RECIPE_NM_KO, B.SUMRY, B.IMG_URL, 0 FROM recipe_basic B" +
                    " WHERE RECIPE_NM_KO LIKE \"%" + search_name + "%\"";

            Cursor cursor = db.rawQuery(sqlsql, null);

            while (cursor.moveToNext())
            {
                val += cursor.getString(0)+", ";
                star_name.add(cursor.getString(0));
                star_info.add(cursor.getString(1));
                star_image.add(cursor.getString(2));
            }
            cursor.close();
        }
        displayRecipe(star_name, star_info, star_image, star_star, 2);
        dbHelper2.close();
        dbHelper.close();

    }


    public void getVal() {

        DataBaseHelper dbHelper = new DataBaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        StarDatabaseHelper dbHelper2 = new StarDatabaseHelper(this);
        SQLiteDatabase db2 = dbHelper2.getReadableDatabase();
        int star=0;
        String[] strData=fridge_ingredients.toArray(new String[fridge_ingredients.size()]);

        recipe_name=new ArrayList<>();
        recipe_info=new ArrayList<>();
        recipe_image=new ArrayList<>();
        recipe_star=new ArrayList<>();

        if(strData.length>0){
            //현재 냉장고 안에 있는 재료 출력
            String addIngredients=strData[0];
            for(int i=1;i<strData.length;i++){
                addIngredients+=", "+strData[i];
            }
            myFridge.setText("재료: "+addIngredients);

            // 냉장고 속 식재료로 만들 수 있는 레시피 조회(가장 많이 겹치는 상위 10개 출력)
            String addSql="A.IRDNT_NM=? ";
            for(int i=0;i<strData.length-1;i++){
                addSql+="OR A.IRDNT_NM=? ";
            }
            Cursor cursor = db.rawQuery("SELECT B.RECIPE_NM_KO, B.SUMRY, B.IMG_URL, C.RECIPE_COUNT" +
                    " FROM recipe_basic B,(SELECT A.RECIPE_ID, count(A.IRDNT_NM) AS RECIPE_COUNT" +
                    " FROM recipe_ingredient A" +
                    " WHERE " +addSql+
                    " GROUP BY A.RECIPE_ID" +
                    " ORDER BY count(A.IRDNT_NM) DESC) C" +
                    " WHERE B.RECIPE_ID=C.RECIPE_ID" +
                    " ORDER BY C.RECIPE_COUNT DESC limit 10", strData);

            while (cursor.moveToNext())
            {
                val += cursor.getString(0)+", ";
                recipe_name.add(cursor.getString(0));
                recipe_info.add(cursor.getString(1));
                recipe_image.add(cursor.getString(2));
                Cursor cursor2 = db2.rawQuery("SELECT ID" +
                        " FROM my_star" +
                        " WHERE ID =?;", new String[]{cursor.getString(0)});
                if(cursor2.moveToNext()){
                    recipe_star.add("1");
                }else{
                    recipe_star.add("0");
                }
                cursor2.close();
            }
            //namename.setText("요리 목록: "+val);
            cursor.close();
        }
        displayRecipe(recipe_name, recipe_info, recipe_image, recipe_star, 1);
        dbHelper2.close();
        dbHelper.close();
    }

    public void getStar() {
        DataBaseHelper dbHelper = new DataBaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        StarDatabaseHelper dbHelper2 = new StarDatabaseHelper(this);
        SQLiteDatabase db2 = dbHelper2.getReadableDatabase();

        Cursor cursor2 = dbHelper2.readAllData();

        star_name=new ArrayList<>();
        star_info=new ArrayList<>();
        star_image=new ArrayList<>();
        star_star=new ArrayList<>();

        while (cursor2.moveToNext()) {
            Cursor cursor = db.rawQuery("SELECT RECIPE_NM_KO, SUMRY, IMG_URL" +
                    " FROM recipe_basic" +
                    " WHERE RECIPE_NM_KO=?;", new String[]{cursor2.getString(0)});
            if (cursor.moveToNext()) {
                star_name.add(cursor.getString(0));
                star_info.add(cursor.getString(1));
                star_image.add(cursor.getString(2));
            }
            cursor.close();
        }
        cursor2.close();
        displayRecipe(star_name, star_info, star_image, star_star, 2);
        dbHelper.close();
        dbHelper2.close();
    }

    void storeDataInArrays(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
        }else{
            while (cursor.moveToNext()){
                fridge_ingredients.add(cursor.getString(0));
                fridge_type.add(cursor.getString(1));
                fridge_date.add(cursor.getString(2));
                fridge_adapter.notifyDataSetChanged();
            }
        }
    }

    void displayData(){
        for(int i=0;i<fridge_ingredients.size();i++){
            int image=0;
            switch (fridge_type.get(i)){

                default:
                    image=R.drawable.egg;
                    break;
            }
            fridge_adapter.addItem(ContextCompat.getDrawable(this, image), fridge_ingredients.get(i),fridge_date.get(i));
            date1 = fridge_date.get(i).toString();
            datelst[dateIndex] = date1;
            dateIndex++;
        }
        fridge_adapter.notifyDataSetChanged();
    }

    void displayRecipe( ArrayList<String> recipe_name, ArrayList<String> recipe_info, ArrayList<String> recipe_image, ArrayList<String> recipe_star, int type){

        if(type==1) {
            view_type=1;
            for (int i = 0; i < recipe_name.size(); i++) {
                new DownloadFilesTask().execute(recipe_image.get(i));
            }
            recipe_adapter.notifyDataSetChanged();
        }else{
            view_type=2;
            for (int i = 0; i < recipe_name.size(); i++) {
                new DownloadFilesTask().execute(recipe_image.get(i));
            }
            star_adapter.notifyDataSetChanged();
        }
    }
    private void restart(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }

    private class DownloadFilesTask extends AsyncTask<String,Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            bmp = null;
            try {
                String img_url = strings[0]; //url of the image
                URL url = new URL(img_url);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Bitmap result) {
            // doInBackground 에서 받아온 total 값 사용 장소
            if(view_type==1) {
                int star;
                if (recipe_star.get(count).equals("1")) {
                    star = R.drawable.star;
                    Log.v("MYTAG", recipe_name.get(count));
                    star_adapter.addItem(result, recipe_name.get(count), recipe_info.get(count), ContextCompat.getDrawable(MainActivity.this, star));
                } else {
                    star = R.drawable.no_star;
                }
                recipe_adapter.addItem(result, recipe_name.get(count), recipe_info.get(count), ContextCompat.getDrawable(MainActivity.this, star));
                count++;
            }else if(view_type ==2){
                int star;
                star = R.drawable.star;
                star_adapter.addItem(result, star_name.get(count2), star_info.get(count2), ContextCompat.getDrawable(MainActivity.this, star));
                count2++;
            }
        }
    }

}


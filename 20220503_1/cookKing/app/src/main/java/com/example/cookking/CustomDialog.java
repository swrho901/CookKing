package com.example.cookking;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CustomDialog extends Dialog {
    private TextView txt_contents;
    private Button shutdownClick;
   //ArrayList<SampleData> movieDataList;

    static int either = 0;
    public CustomDialog(@NonNull Context context, String contents) {
        super(context);
        setContentView(R.layout.activity_custom_dialog);
        //MyAdapter myAdapter = new MyAdapter(this,movieDataList);

        EditText customRecipe = (EditText) this.findViewById(R.id.customRecipe);
        EditText customMaterial = (EditText) this.findViewById(R.id.customMaterial);

        txt_contents = findViewById(R.id.txt_contents);
        txt_contents.setText(contents);
        shutdownClick = findViewById(R.id.btnDone);
        shutdownClick.setOnClickListener(new View.OnClickListener() {


            protected void onCreate(Bundle savedInstanceState) {



            }
            @Override
            public void onClick(View v) {

                if(either == 1) {
                    //String recipeName = (customRecipe.getText().toString());
                    MainActivity.recipeName = (customRecipe.getText().toString());
                    //String recipeMaterial = customMaterial.getText().toString();
                    MainActivity.recipeMaterial = customMaterial.getText().toString();

                    MainActivity.flag1 = 1;
                }
                else{
                    MainActivity.Material = (customRecipe.getText().toString());

                    MainActivity.materialExpireDate = customMaterial.getText().toString();
                    MainActivity.flag2 = 1;

                }

                /*int count = myAdapter.getCount();
                // 아이템 추가.
                movieDataList.add(new SampleData(R.drawable.plus, recipeName,recipeMaterial));
                // listview 갱신
                myAdapter.notifyDataSetChanged();*/


                dismiss();
            }
        });
    }
}
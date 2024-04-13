package com.trycatch_tanmay.scanlycard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity  {

ImageView nxt_button,camera;
TextView first_txt,second_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nxt_button = findViewById(R.id.nxt_button);
        camera=findViewById(R.id.camera);
        first_txt=findViewById(R.id.first_txt);
        second_txt=findViewById(R.id.second_txt);
        getWindow().setStatusBarColor(Integer.parseInt(String.valueOf(getColor(R.color.top2_colour))));


        nxt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }




}

package com.trycatch_tanmay.scanlycard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemDetailsActivity extends AppCompatActivity {
    ImageView Details_imageView;
    TextView Details_Name,Details_Contact,Details_Address,Details_Position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        // Receive the clicked item details
        ModelHome modelHome = getIntent().getParcelableExtra("modelHome");
        getWindow().setStatusBarColor(Integer.parseInt(String.valueOf(getColor(R.color.top_colour))));

        // Display the details in your layout (e.g., TextViews, ImageView)
        Details_Name = findViewById(R.id.Details_Name);
        // Similarly, set other details...
        Details_Name.setText(modelHome.getName());

        Details_Contact=findViewById(R.id.Details_Contact);
        Details_Contact.setText(modelHome.getContact());

        Details_Address=findViewById(R.id.Details_Address);
        Details_Address.setText(modelHome.getAddress());

        Details_Position=findViewById(R.id.Details_Position);
        Details_Position.setText(modelHome.getPosition());

        Details_imageView=findViewById(R.id.Details_imageView);


    }
}
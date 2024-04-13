package com.trycatch_tanmay.scanlycard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import android.content.Intent;
import android.graphics.Camera;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.util.concurrent.ListenableFuture;

import org.w3c.dom.Text;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;


public class DashBoardActivity extends AppCompatActivity {
    ImageView top_bkc_img;
    TextView scanly_txt;
    FrameLayout frame_lyt;
    BottomNavigationView bottomNavigationView;
    ImageView image_btn;
    HomeFragment homeFragment;
    CategoryFragment categoryFragment;
    FavouriteFragment favouriteFragment;
    ProfileFragment profileFragment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        getWindow().setStatusBarColor(Integer.parseInt(String.valueOf(getColor(R.color.top_colour))));
        // Initialize fragments
        homeFragment = new HomeFragment();
        categoryFragment = new CategoryFragment();
        favouriteFragment = new FavouriteFragment();
        profileFragment = new ProfileFragment();

        // If the savedInstanceState is null, add the HomeFragment
        // If the savedInstanceState is null, add the HomeFragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.dashboard_container, new HomeFragment(), "HomeFragment")
                    .commit();
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        image_btn = findViewById(R.id.fab);

        image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this,CameraActivity.class);
                startActivity(intent);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navHome) {
                    loadFrag(new HomeFragment(), false); // Replace with HomeFragment
                    return true;
                } else if (id == R.id.navCategory) {
                    loadFrag(new CategoryFragment(), false); // Replace with CategoryFragment
                    return true;
                } else if (id == R.id.navFavorite) {
                    loadFrag(new FavouriteFragment(), false); // Replace with FavouriteFragment
                    return true;
                } else if (id == R.id.navProfile) {
                    loadFrag(new ProfileFragment(), true); // Replace with ProfileFragment
                    return true;
                }
                return true;
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, homeFragment, "HomeFragment")
                    .commit();

        }
    }

    //    public void loadFrag(Fragment fragment, boolean flag) {
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//
//        // Generate a unique tag based on the current time
//        String tag = "HomeFragment_" + System.currentTimeMillis();
//
//        // Add the fragment to the container with the unique tag
//        ft.add(R.id.container, fragment, tag);
//        ft.addToBackStack(tag); // This allows the user to navigate back to the previous fragment
//
//        ft.commit();
//    }
    public void loadFrag(Fragment fragment, boolean flag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        // Replace the current fragment with the new one
        ft.replace(R.id.dashboard_container, fragment);
        if (flag) {
            ft.addToBackStack(null); // Add to back stack only if needed
        }

        ft.commit();
    }
}



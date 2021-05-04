package com.example.rent.screen.myads;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.rent.R;
import com.example.rent.pojo.HomePoJo;

public class MyAdsDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ads_detail);

        getSupportActionBar().setTitle("My Ads Detail");

        try {
            HomePoJo homePoJo = (HomePoJo) getIntent().getSerializableExtra("homePoJo");
            changeFragment(MyAdViewFragment.getInstance(homePoJo));
        }catch (Exception e){}

    }


    void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.my_ads_detail_container, fragment);
        fragmentTransaction.commit();

    }
}
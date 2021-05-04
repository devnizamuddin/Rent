package com.example.rent;

import android.os.Bundle;

import com.example.rent.screen.myads.MyAdsFragment;
import com.example.rent.screen.home.HomeFragment;
import com.example.rent.screen.notification.BookingRequestFragment;
import com.example.rent.screen.profile.ProfileFragment;
import com.example.rent.screen.booking.BookingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeFragment(new HomeFragment());
                    break;
                case R.id.ads_nav:
                    changeFragment(new MyAdsFragment());
                    break;
                case R.id.notification_nav:
                    changeFragment(new BookingRequestFragment());
                    break;
                case R.id.booking_nav:
                    changeFragment(new BookingFragment());
                    break;
                case R.id.navigation_profile:
                    changeFragment(new ProfileFragment());
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navView.setSelectedItemId(R.id.navigation_home);

    }
    void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.test_container, fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0){
            showCloseingDialog();

        }
        else {
            super.onBackPressed();
        }
    }

    private void showCloseingDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.close_app_layout,null,false);
        builder.setView(view);
        final AlertDialog dialog = builder.show();

        final Button yes_btn = view.findViewById(R.id.yes_btn);
        final Button no_btn = view.findViewById(R.id.no_btn);

        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}

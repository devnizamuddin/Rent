package com.example.rent.screen.notification;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.rent.pojo.BookingPoJo;
import com.example.rent.R;

public class RequestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        getSupportActionBar().setTitle("Booking Request Detail");

        try {

            BookingPoJo bookingPoJo = (BookingPoJo) getIntent().getSerializableExtra("bookingPoJo");
            changeFragment(RequestDetailFragment.getInstance(bookingPoJo));

        } catch (Exception e) {

        }

    }

    void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.request_container, fragment);
        fragmentTransaction.commit();

    }

}

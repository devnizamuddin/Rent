package com.example.rent.Navigation.MyAdd;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rent.Adapter.HomeAdapter;
import com.example.rent.Adapter.RequestAdapter;
import com.example.rent.Navigation.MyAdd.BookingHistory.HistoryActivity;
import com.example.rent.Navigation.MyAdd.Request.RequestActivity;
import com.example.rent.PoJo.BookingPoJo;
import com.example.rent.PoJo.HomePoJo;
import com.example.rent.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAdsFragment extends Fragment implements HomeAdapter.HomeItemClickListener, RequestAdapter.RequestClickListener {

    private DatabaseReference databaseReference,bookingDatabaseReference;
    private FirebaseAuth firebaseAuth;
    private Context context;
    private String userId;


    private ArrayList<HomePoJo> homePoJos;
    private HomeAdapter homeAdapter;
    private ArrayList<BookingPoJo>bookingPoJos = new ArrayList<>();

    private RecyclerView my_ad_rv,my_booking_rv;
    private ImageView notification_iv,history_iv;
    private RequestAdapter requestAdapter;

    public MyAdsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_my_ads, container, false);

        my_ad_rv = view.findViewById(R.id.my_ad_rv);
        my_booking_rv = view.findViewById(R.id.my_booking_rv);
        notification_iv = view.findViewById(R.id.notification_iv);
        history_iv = view.findViewById(R.id.history_img);


        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("House_Ad");
        bookingDatabaseReference = FirebaseDatabase.getInstance().getReference("Booking");

        userId = firebaseAuth.getCurrentUser().getUid();


        homePoJos = new ArrayList<>();

        @SuppressLint("WrongConstant") RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);

        @SuppressLint("WrongConstant") RecyclerView.LayoutManager bookingLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);

        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
                super.getItemOffsets(outRect, itemPosition, parent);
                outRect.set(0, 0, 20, 0);
            }
        };
        my_ad_rv.addItemDecoration(itemDecoration);
        my_ad_rv.setLayoutManager(layoutManager);
        homeAdapter = new HomeAdapter(context,homePoJos,this);
        my_ad_rv.setAdapter(homeAdapter);


        my_booking_rv.addItemDecoration(itemDecoration);
        my_booking_rv.setLayoutManager(bookingLayoutManager);
        requestAdapter = new RequestAdapter(context,bookingPoJos,this,100);
        my_booking_rv.setAdapter(requestAdapter);

        notification_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), RequestActivity.class));
            }
        });

        history_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HistoryActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        getMyAdsFromFireBase();
        getMyBookingFromFirebase();

        return view;
    }

    private void getMyBookingFromFirebase() {

        bookingDatabaseReference.child("Accept").orderByChild("bookerId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    bookingPoJos.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()){


                        BookingPoJo bookingPoJo = data.getValue(BookingPoJo.class);
                        bookingPoJos.add(bookingPoJo);

                    }
                    requestAdapter.updateBookinglList(bookingPoJos);
                }
                else {
                    bookingPoJos.clear();
                    requestAdapter.updateBookinglList(bookingPoJos);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getMyAdsFromFireBase() {

        databaseReference.orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    homePoJos.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()){


                        HomePoJo homePoJo = data.getValue(HomePoJo.class);
                        homePoJos.add(homePoJo);

                    }
                    homeAdapter.updateHomeList(homePoJos);
                }
                else {
                    Toast.makeText(context, "No Data Exists", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onClickHomeItemClickListener(HomePoJo homePoJo) {

        changeFragment(MyAdViewFragment.getInstance(homePoJo));

    }
    void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.test_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    @Override
    public void onClick(BookingPoJo bookingPoJo) {

    }
}

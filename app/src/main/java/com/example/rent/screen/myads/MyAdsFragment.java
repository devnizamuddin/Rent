package com.example.rent.screen.myads;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rent.adapter.HomeAdapter;
import com.example.rent.pojo.HomePoJo;
import com.example.rent.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MyAdsFragment extends Fragment implements HomeAdapter.HomeItemClickListener {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private Context context;
    private String userId;


    private ArrayList<HomePoJo> homePoJos;//My ads which i had posted
    private HomeAdapter homeAdapter;

    private RecyclerView my_ad_rv;

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


        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("House_Ad");

        userId = firebaseAuth.getCurrentUser().getUid();


        homePoJos = new ArrayList<>();

        @SuppressLint("WrongConstant") RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);

        my_ad_rv.setLayoutManager(layoutManager);
        homeAdapter = new HomeAdapter(context,homePoJos,this);
        my_ad_rv.setAdapter(homeAdapter);

        getMyAdsFromFireBase();

        return view;
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

        Intent intent = new Intent(getContext(),MyAdsDetailActivity.class);
        intent.putExtra("homePoJo",homePoJo);
        startActivity(intent);
    }
    void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.test_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

}

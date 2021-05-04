package com.example.rent.screen.booking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rent.adapter.RequestAdapter;
import com.example.rent.pojo.BookingPoJo;
import com.example.rent.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class BookingFragment extends Fragment implements RequestAdapter.RequestClickListener {


    //other objects
    private String userId;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference bookingDatabaseReference;
    private ArrayList<BookingPoJo> bookingPoJos;
    private RequestAdapter requestAdapter;
    private static final String TAG = "Hello";

    //Views
    private RecyclerView my_booking_rv;
    private Button view_booking_history_btn;

    public BookingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        //Initializing views
        my_booking_rv = view.findViewById(R.id.my_booking_rv);
        view_booking_history_btn = view.findViewById(R.id.view_booking_history_btn);

        //Initializing Objects
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        bookingDatabaseReference = FirebaseDatabase.getInstance().getReference("Booking");

        initializeBookingList();
        getMyBookingFromFirebase();


        view_booking_history_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HistoryActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
            }
        });

        return view;
    }

    private void initializeBookingList() {

        bookingPoJos = new ArrayList<>();
        @SuppressLint("WrongConstant") RecyclerView.LayoutManager bookingLayoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        my_booking_rv.setLayoutManager(bookingLayoutManager);
        requestAdapter = new RequestAdapter(getContext(),bookingPoJos,this,100);
        my_booking_rv.setAdapter(requestAdapter);
    }

    private void getMyBookingFromFirebase() {

        bookingDatabaseReference.child("Accept").orderByChild("bookerId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Log.d(TAG, "onDataChange: ");
                    bookingPoJos.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {


                        BookingPoJo bookingPoJo = data.getValue(BookingPoJo.class);
                        bookingPoJos.add(bookingPoJo);

                    }
                    requestAdapter.updateBookinglList(bookingPoJos);
                } else {
                    bookingPoJos.clear();
                    requestAdapter.updateBookinglList(bookingPoJos);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(BookingPoJo bookingPoJo) {

    }
}
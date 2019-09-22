package com.example.rent.Navigation.MyAdd.Request;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
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

import com.example.rent.Adapter.RequestAdapter;

import com.example.rent.PoJo.BookingPoJo;
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
public class RequestListFragment extends Fragment implements RequestAdapter.RequestClickListener {


    RecyclerView request_rv;
    private DatabaseReference databaseReferenceBooking;
    private FirebaseAuth firebaseAuth;
    private String userId;

    private ArrayList<BookingPoJo> bookingPoJos;
    private RequestAdapter requestAdapter;
    private Context context;

    public RequestListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_request_list, container, false);

        request_rv = view.findViewById(R.id.request_rv);


        databaseReferenceBooking = FirebaseDatabase.getInstance().getReference("Booking");
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        bookingPoJos = new ArrayList<>();

        @SuppressLint("WrongConstant") RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        request_rv.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
                super.getItemOffsets(outRect, itemPosition, parent);
                outRect.set(0, 0, 0, 20);
            }
        };
        request_rv.addItemDecoration(itemDecoration);
        requestAdapter = new RequestAdapter(context,bookingPoJos,this,10);
        request_rv.setAdapter(requestAdapter);

        getDataFromFireBase();

        return view;
    }

    private void getDataFromFireBase() {

        databaseReferenceBooking.child("Request").orderByChild("ownerId").equalTo(userId).addValueEventListener(new ValueEventListener() {
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
                    Toast.makeText(context, "You have no request", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(context, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onClick(BookingPoJo bookingPoJo) {

    changeFragment(RequestViewFragment.getInstance(bookingPoJo));

    }

    void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.request_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}

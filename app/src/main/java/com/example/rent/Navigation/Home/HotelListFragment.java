package com.example.rent.Navigation.Home;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rent.Adapter.HotelAdapter;

import com.example.rent.PoJo.HotelPoJo;
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
public class HotelListFragment extends Fragment implements HotelAdapter.HotelClickListener {

    private Context context;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String userId;

    private RecyclerView hotel_rv;
    private Spinner hotel_sp;

    private ArrayList<HotelPoJo> hotelPoJos;
    private HotelAdapter hotelAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
    public HotelListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_hotel_list, container, false);

       hotel_sp = view.findViewById(R.id.hotel_sp);
       hotel_rv = view.findViewById(R.id.hotel_rv);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.
                createFromResource(context,R.array.location_array,android.R.layout.simple_list_item_1);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        hotel_sp.setAdapter(arrayAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Hotel_Ad");
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        hotelPoJos = new ArrayList<>();

        @SuppressLint("WrongConstant") RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        hotel_rv.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
                super.getItemOffsets(outRect, itemPosition, parent);
                outRect.set(0, 0, 0, 20);
            }
        };
        hotel_rv.addItemDecoration(itemDecoration);
        hotelAdapter = new HotelAdapter(context,hotelPoJos,this);
        hotel_rv.setAdapter(hotelAdapter);

        getDataFromFireBase();

        hotel_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String text = hotel_sp.getSelectedItem().toString();
                if (!text.equals("Please select your area")){

                    searchInFireBase(text);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return view;
    }

    private void getDataFromFireBase() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                hotelPoJos.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    HotelPoJo hotelPoJo = data.getValue(HotelPoJo.class);
                    hotelPoJos.add(hotelPoJo);

                }
                hotelAdapter.updateHotelList(hotelPoJos);
            }
                else {
                hotelPoJos.clear();
                hotelAdapter.updateHotelList(hotelPoJos);
            }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(context, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void searchInFireBase(String s) {

        databaseReference.orderByChild("area").equalTo(s).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){

                    hotelPoJos.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()){


                        HotelPoJo hotelPoJo = data.getValue(HotelPoJo.class);
                        hotelPoJos.add(hotelPoJo);

                    }
                    hotelAdapter.updateHotelList(hotelPoJos);
                }
                else {
                    hotelPoJos.clear();
                    hotelAdapter.updateHotelList(hotelPoJos);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(context, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClickHotel(HotelPoJo hotelPoJo) {

        Intent intent = new Intent(getActivity(),HotelViewActivity.class);
        intent.putExtra("HotelPoJo",hotelPoJo);
        startActivity(intent);

    }
}

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rent.Adapter.HomeAdapter;
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
public class HouseListFragment extends Fragment implements HomeAdapter.HomeItemClickListener {


    private Context context;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String userId;

    private RecyclerView home_rv;
    private Spinner home_sp;

    private ArrayList<HomePoJo>homePoJos;
    private HomeAdapter homeAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public HouseListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_house_list, container, false);

        home_rv = view.findViewById(R.id.home_rv);
        home_sp = view.findViewById(R.id.home_sp);


        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.
                createFromResource(context,R.array.location_array,android.R.layout.simple_list_item_1);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        home_sp.setAdapter(arrayAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("House_Ad");
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        homePoJos = new ArrayList<>();

        @SuppressLint("WrongConstant") RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        home_rv.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
                super.getItemOffsets(outRect, itemPosition, parent);
                outRect.set(0, 0, 0, 20);
            }
        };
        home_rv.addItemDecoration(itemDecoration);
        homeAdapter = new HomeAdapter(context,homePoJos,this);
        home_rv.setAdapter(homeAdapter);

        getDataFromFireBase();
        home_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String text = home_sp.getSelectedItem().toString();
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

    private void searchInFireBase(String query) {

        databaseReference.orderByChild("area").equalTo(query).addValueEventListener(new ValueEventListener() {
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
                    homePoJos.clear();
                    homeAdapter.updateHomeList(homePoJos);
                    Toast.makeText(context, "No Data Exists", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(context, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getDataFromFireBase() {


        databaseReference.addValueEventListener(new ValueEventListener() {
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
                    Toast.makeText(context, "No Ad Yet", Toast.LENGTH_SHORT).show();
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

        /*HouseDetailFragment houseDetailFragment = HouseDetailFragment.getInstance(homePoJo);
        changeFragment(houseDetailFragment);*/

        startActivity(new Intent(getActivity(),HouseViewActivity.class).putExtra("homePoJo",homePoJo));
    }


   /* void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.test_container, fragment);
        fragmentTransaction.commit();

    }*/
}

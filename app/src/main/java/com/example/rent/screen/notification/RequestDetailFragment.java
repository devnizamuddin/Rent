package com.example.rent.screen.notification;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.rent.pojo.BookingPoJo;
import com.example.rent.pojo.UserInfoPoJo;
import com.example.rent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestDetailFragment extends Fragment {


    BookingPoJo bookingPoJo;
    DatabaseReference datesDatabaseReference, userDatabaseReference;
    private Context context;

    TextView description_tv, name_tv, email_tv, phone_tv;
    ImageView user_iv;
    String renterId;
    Button accept_btn,cancel_btn;

    public RequestDetailFragment() {
        // Required empty public constructor
    }

    public static RequestDetailFragment getInstance(BookingPoJo bookingPoJo) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("bookingPoJo", bookingPoJo);
        RequestDetailFragment requestDetailFragment = new RequestDetailFragment();
        requestDetailFragment.setArguments(bundle);
        return requestDetailFragment;


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
        View view = inflater.inflate(R.layout.fragment_request_detail, container, false);

        description_tv = view.findViewById(R.id.description_tv);
        name_tv = view.findViewById(R.id.name_tv);
        email_tv = view.findViewById(R.id.email_tv);
        phone_tv = view.findViewById(R.id.phone_tv);
        user_iv = view.findViewById(R.id.user_iv);
        accept_btn = view.findViewById(R.id.accept_btn);
        cancel_btn = view.findViewById(R.id.cancel_btn);

        try {

            bookingPoJo = (BookingPoJo) getArguments().getSerializable("bookingPoJo");
            renterId = bookingPoJo.getBookerId();


        } catch (Exception e) {
        }

        datesDatabaseReference = FirebaseDatabase.getInstance().getReference("Booking");
        userDatabaseReference = FirebaseDatabase.getInstance().getReference("UserInfo");


        accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                acceptBooking();
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelBooking();
            }
        });

      //  getPreviousBookingDates();
        getRenterInformation();

        return view;
    }

    private void cancelBooking() {
        datesDatabaseReference.child("Request").child(bookingPoJo.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(context, "request canceled", Toast.LENGTH_SHORT).show();
                    changeFragment(new BookingRequestFragment());
                }
                else {
                    Toast.makeText(context, ""+task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void acceptBooking() {
        datesDatabaseReference.child("Accept").child(bookingPoJo.getId()).setValue(bookingPoJo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    datesDatabaseReference.child("Request").child(bookingPoJo.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(context, "Accepted", Toast.LENGTH_SHORT).show();
                                changeFragment(new BookingRequestFragment());
                            }
                            else {
                                Toast.makeText(context, ""+task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(context, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.request_container, fragment);
        fragmentTransaction.commit();

    }

    private void getRenterInformation() {

        userDatabaseReference.child(renterId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    UserInfoPoJo userInfoPoJo = dataSnapshot.getValue(UserInfoPoJo.class);

                    description_tv.setText(userInfoPoJo.getName()+" wants to hire your\n"+bookingPoJo.getDescription());
                    name_tv.setText(userInfoPoJo.getName());
                    email_tv.setText(userInfoPoJo.getEmail());
                    phone_tv.setText(userInfoPoJo.getPhoneNumber());
                    Uri uri = Uri.parse(userInfoPoJo.getProfileImageUrl());
                    Picasso.get().load(uri).into(user_iv);

                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

   /* private void getPreviousBookingDates() {

        datesDatabaseReference.child("Accepted").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                } else {
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }*/

}

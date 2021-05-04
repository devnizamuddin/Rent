package com.example.rent.screen.home.house;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rent.pojo.BookingPoJo;
import com.example.rent.pojo.HomePoJo;
import com.example.rent.pojo.MyLocation;
import com.example.rent.pojo.UserInfoPoJo;
import com.example.rent.R;
import com.example.rent.screen.home.ViewLocationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.savvi.rangedatepicker.CalendarPickerView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HouseViewActivity extends AppCompatActivity {


    HomePoJo homePoJo;
    TextView description_tv, price_tv, phone_tv, address_tv, name_tv, email_tv;
    ImageView image_one_iv, image_two_iv, image_three_iv;
    Button booking_request_btn;

    DatabaseReference databaseReference, databaseReferenceBooking;
    FirebaseAuth firebaseAuth ;

    String ownerId;
    String userId;
    String adId;
    String description;
    String price, type;
    ArrayList<String> sDisableDates = new ArrayList<>();
    ArrayList<String> sTwoDisableDates = new ArrayList<>();
    ArrayList<Date> disableDates = new ArrayList<>();

    CalendarPickerView calendar;
    Button button,view_location_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_view);

        getSupportActionBar().setTitle("House Detail");

        description_tv = findViewById(R.id.description_tv);
        price_tv = findViewById(R.id.price_tv);
        phone_tv = findViewById(R.id.phone_tv);
        address_tv = findViewById(R.id.address_tv);
        image_one_iv = findViewById(R.id.image_one_iv);
        image_two_iv = findViewById(R.id.image_two_iv);
        image_three_iv = findViewById(R.id.image_three_iv);

        name_tv = findViewById(R.id.name_tv);
        email_tv = findViewById(R.id.email_tv);
        phone_tv = findViewById(R.id.phone_tv);
        booking_request_btn = findViewById(R.id.booking_request_btn);
        view_location_btn = findViewById(R.id.view_location_btn);


        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("UserInfo");
        databaseReferenceBooking = FirebaseDatabase.getInstance().getReference("Booking");


        //Getting Current Ad information........................
        try {

            homePoJo = (HomePoJo) getIntent().getSerializableExtra("homePoJo");
            ownerId = homePoJo.getUserId();
            adId =  homePoJo.getId();
            description = homePoJo.getDescription();
            price = homePoJo.getAmount();
            type = homePoJo.getPriceType();
            if (firebaseAuth.getUid().equals(ownerId)){
                booking_request_btn.setVisibility(View.INVISIBLE);
            }
                description_tv.setText(homePoJo.getDescription());
            price_tv.setText(homePoJo.getAmount() + " taka " + homePoJo.getPriceType());
            address_tv.setText(homePoJo.getArea());
        } catch (Exception e) {
        }


        //.............................. Getting Current Ad information
        booking_request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (homePoJo.getPriceType().equals("Daily")) {
                   createDatePickerDialog();
                }
                else {
                    Toast.makeText(HouseViewActivity.this, "Processing", Toast.LENGTH_SHORT).show();
                }
            }
        });

        view_location_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    MyLocation myLocation = homePoJo.getMyLocation();
                    if (myLocation.isValid()){

                        Intent intent = new Intent(HouseViewActivity.this, ViewLocationActivity.class);
                        intent.putExtra("myLocation",myLocation);
                        startActivity(intent);
                    }
                    else {

                        Toast.makeText(HouseViewActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){}

            }
        });
        setImage();
        getHouseOwnerInformation();
        registerPreviousBooking();
        registerPreviousRequest();
    }

    private void registerPreviousRequest() {

        databaseReferenceBooking.child("Request").orderByChild("adId").equalTo(adId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    sTwoDisableDates.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        BookingPoJo bookingPoJo = data.getValue(BookingPoJo.class);
                       // ArrayList<String> dates = (ArrayList<String>) data.getValue();

                        sTwoDisableDates.addAll(bookingPoJo.getDates());

                    }
                    stringToDate();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(HouseViewActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void registerPreviousBooking() {

        databaseReferenceBooking.child("Accept").orderByChild("adId").equalTo(adId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    sDisableDates.clear();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        BookingPoJo bookingPoJo = data.getValue(BookingPoJo.class);
                        // ArrayList<String> dates = (ArrayList<String>) data.getValue();

                        sDisableDates.addAll(bookingPoJo.getDates());

                    }
                    stringToDate();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(HouseViewActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void stringToDate() {

        boolean dates = sDisableDates.addAll(sTwoDisableDates);

        try {
            SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
            for (String sDate : sDisableDates) {
                Date date = dateformat.parse(sDate);
                if (new Date().before(date) || new Date().equals(date)){
                    disableDates.add(date);
                    disableDates.add(new Date());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void createDatePickerDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = LayoutInflater.from(this).inflate(R.layout.calder_picker_layout, null, false);
        builder.setView(view);
        final AlertDialog alertDialog = builder.show();

        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        final Calendar lastYear = Calendar.getInstance();

        calendar = view.findViewById(R.id.calendar_view);
        button = view.findViewById(R.id.get_selected_dates);

        calendar.init(lastYear.getTime(), nextYear.getTime(), new SimpleDateFormat("MMMM, YYYY", Locale.getDefault())) //
                .inMode(CalendarPickerView.SelectionMode.RANGE)
                .withHighlightedDates(disableDates);
        calendar.scrollToDate(new Date());


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = databaseReferenceBooking.push().getKey();
                ArrayList<String> dates = dateListToStringList();
                BookingPoJo bookingPoJo = new BookingPoJo(id,adId,ownerId,userId,description,price+" Taka "+type,dates);

                databaseReferenceBooking.child("Request").child(id).setValue(bookingPoJo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            alertDialog.dismiss();
                            Toast.makeText(HouseViewActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HouseViewActivity.this, ""+task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    private ArrayList<String> dateListToStringList() {

        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");

        ArrayList<String> sDates = new ArrayList<>();

        for (Date date : calendar.getSelectedDates()) {
            sDates.add(dateformat.format(date));
        }

        return sDates;
    }

    private void getHouseOwnerInformation() {

        databaseReference.child(ownerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    UserInfoPoJo userInfoPoJo = dataSnapshot.getValue(UserInfoPoJo.class);
                    name_tv.setText(userInfoPoJo.getName());
                    email_tv.setText(userInfoPoJo.getEmail());
                    phone_tv.setText(userInfoPoJo.getPhoneNumber());

                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(HouseViewActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setImage() {

        int size = homePoJo.getDownloadUrls().size();
        if (size == 3) {

            Uri uri = Uri.parse(homePoJo.getDownloadUrls().get(0));
            Picasso.get().load(uri).resize(150, 100).into(image_one_iv);
            Uri uriTwo = Uri.parse(homePoJo.getDownloadUrls().get(1));
            Picasso.get().load(uriTwo).resize(150, 100).into(image_two_iv);
            Uri uriThree = Uri.parse(homePoJo.getDownloadUrls().get(2));
            Picasso.get().load(uriThree).resize(150, 100).into(image_three_iv);
        } else if (size == 2) {
            Uri uri = Uri.parse(homePoJo.getDownloadUrls().get(0));
            Picasso.get().load(uri).into(image_one_iv);
            Uri uriTwo = Uri.parse(homePoJo.getDownloadUrls().get(1));
            Picasso.get().load(uriTwo).into(image_two_iv);
        } else if (size == 1) {
            Uri uri = Uri.parse(homePoJo.getDownloadUrls().get(0));
            Picasso.get().load(uri).into(image_one_iv);
        } else {
        }

    }
}

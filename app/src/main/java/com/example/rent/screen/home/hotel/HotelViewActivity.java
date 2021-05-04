package com.example.rent.screen.home.hotel;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rent.pojo.HotelPoJo;
import com.example.rent.R;
import com.savvi.rangedatepicker.CalendarPickerView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HotelViewActivity extends AppCompatActivity {

    TextView description_tv, price_tv, phone_tv, address_tv, name_tv, email_tv;
    ImageView image_one_iv, image_two_iv, image_three_iv;
    Button booking_request_btn,button;
    HotelPoJo hotelPoJo;
    CalendarPickerView calendar;
    ArrayList<Date> disableDates = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_view);

        getSupportActionBar().setTitle("Hotel Details");

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

        try {
            hotelPoJo = (HotelPoJo) getIntent().getSerializableExtra("HotelPoJo");
            price_tv.setText(hotelPoJo.getPrice());
            name_tv.setText(hotelPoJo.getName());
            description_tv.setText(hotelPoJo.getDescription());
            phone_tv.setText(hotelPoJo.getPhone());
            address_tv.setText(hotelPoJo.getAddress());
            email_tv.setText(hotelPoJo.getEmail());
            setImage();
        }
        catch (Exception e){}

        booking_request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Toast.makeText(HotelViewActivity.this, "Processing", Toast.LENGTH_SHORT).show();
            }
        });



    }
    private void setImage() {



            Uri uri = Uri.parse(hotelPoJo.getDownloadUrls().get("01"));
            Picasso.get().load(uri).resize(150, 100).into(image_one_iv);
            Uri uriTwo = Uri.parse(hotelPoJo.getDownloadUrls().get("02"));
            Picasso.get().load(uriTwo).resize(150, 100).into(image_two_iv);
            Uri uriThree = Uri.parse(hotelPoJo.getDownloadUrls().get("03"));
            Picasso.get().load(uriThree).resize(150, 100).into(image_three_iv);

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

              /*  String id = databaseReferenceBooking.push().getKey();
                ArrayList<String> dates = dateListToStringList();
                BookingPoJo bookingPoJo = new BookingPoJo(id,adId,ownerId,userId,description,price+" Taka "+type,dates);
                Toast.makeText(HouseViewActivity.this, ""+price, Toast.LENGTH_SHORT).show();

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
                });*/
            }
        });

    }
}

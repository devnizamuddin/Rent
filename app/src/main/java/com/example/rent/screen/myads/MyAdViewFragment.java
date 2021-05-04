package com.example.rent.screen.myads;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.rent.pojo.HomePoJo;
import com.example.rent.R;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAdViewFragment extends Fragment {

    private Context context;

    HomePoJo homePoJo;
    TextView description_tv, price_tv, phone_tv, address_tv, name_tv, email_tv;
    ImageView image_one_iv, image_two_iv, image_three_iv;
    Button edit_btn,delete_btn;

    DatabaseReference databaseReference;
    String userId;

    public MyAdViewFragment() {
        // Required empty public constructor
    }

    public static MyAdViewFragment getInstance(HomePoJo homePoJo) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("homePoJo", homePoJo);
        MyAdViewFragment myAdViewFragment = new MyAdViewFragment();
        myAdViewFragment.setArguments(bundle);
        return myAdViewFragment;

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
        View view = inflater.inflate(R.layout.fragment_my_ad_view, container, false);

        description_tv = view.findViewById(R.id.description_tv);
        price_tv = view.findViewById(R.id.price_tv);
        address_tv = view.findViewById(R.id.address_tv);
        image_one_iv = view.findViewById(R.id.image_one_iv);
        image_two_iv = view.findViewById(R.id.image_two_iv);
        image_three_iv = view.findViewById(R.id.image_three_iv);

        name_tv = view.findViewById(R.id.name_tv);
        email_tv = view.findViewById(R.id.email_tv);
        phone_tv = view.findViewById(R.id.phone_tv);

        edit_btn = view.findViewById(R.id.edit_btn);
        delete_btn = view.findViewById(R.id.delete_btn);



        try {
            homePoJo = (HomePoJo) getArguments().getSerializable("homePoJo");
            userId = homePoJo.getUserId();

            description_tv.setText(homePoJo.getDescription());
            price_tv.setText(homePoJo.getAmount()+" "+homePoJo.getPriceType());
            address_tv.setText(homePoJo.getArea());
            setImage();

        } catch (Exception e) {
        }

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        return view;
    }

    private void setImage() {

        int size = homePoJo.getDownloadUrls().size();
        if (size==3){

            Uri uri = Uri.parse(homePoJo.getDownloadUrls().get(0));
            Picasso.get().load(uri).resize(150,100).into(image_one_iv);
            Uri uriTwo = Uri.parse(homePoJo.getDownloadUrls().get(1));
            Picasso.get().load(uriTwo).resize(150,100).into(image_two_iv);
            Uri uriThree = Uri.parse(homePoJo.getDownloadUrls().get(2));
            Picasso.get().load(uriThree).resize(150,100).into(image_three_iv);
        }
        else if (size ==2){
            Uri uri = Uri.parse(homePoJo.getDownloadUrls().get(0));
            Picasso.get().load(uri).into(image_one_iv);
            Uri uriTwo = Uri.parse(homePoJo.getDownloadUrls().get(1));
            Picasso.get().load(uriTwo).into(image_two_iv);
        }
        else if (size == 1){
            Uri uri = Uri.parse(homePoJo.getDownloadUrls().get(0));
            Picasso.get().load(uri).into(image_one_iv);
        }
        else {}

    }

}

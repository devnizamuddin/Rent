package com.example.rent.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rent.PoJo.HotelPoJo;
import com.example.rent.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {

    private Context context;
    private ArrayList<HotelPoJo>hotelPoJos;
    private HotelClickListener hotelClickListener;

    public HotelAdapter(Context context, ArrayList<HotelPoJo> hotelPoJos, HotelClickListener hotelClickListener) {
        this.context = context;
        this.hotelPoJos = hotelPoJos;
        this.hotelClickListener = hotelClickListener;
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.hotel_single_layout,viewGroup,false);

        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder hotelViewHolder, int i) {

        HotelPoJo hotelPoJo = hotelPoJos.get(i);
        hotelViewHolder.cost_tv.setText(hotelPoJo.getPrice()+ "Taka");
        hotelViewHolder.hotel_name_tv.setText(hotelPoJo.getName());


            Uri uri = Uri.parse(hotelPoJo.getDownloadUrls().get("01"));
            Picasso.get().load(uri).into(hotelViewHolder.image_one_iv);
    }

    @Override
    public int getItemCount() {
        return hotelPoJos.size();
    }

    public class HotelViewHolder extends RecyclerView.ViewHolder {

        TextView hotel_name_tv,cost_tv;
        ImageView image_one_iv;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);

            hotel_name_tv = itemView.findViewById(R.id.hotel_name_tv);
            cost_tv = itemView.findViewById(R.id.cost_tv);
            image_one_iv = itemView.findViewById(R.id.image_one_iv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hotelClickListener.onClickHotel(hotelPoJos.get(getAdapterPosition()));
                }
            });
        }
    }
    public interface HotelClickListener{

        void onClickHotel(HotelPoJo hotelPoJo);
    }
    public void updateHotelList(ArrayList<HotelPoJo> hotelPoJos){

        this.hotelPoJos = hotelPoJos;
        notifyDataSetChanged();

    }
}

package com.example.rent.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.rent.pojo.HomePoJo;
import com.example.rent.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private Context context;
    private ArrayList<HomePoJo>homePoJos;
    private HomeItemClickListener homeItemClickListener;


    public HomeAdapter(Context context, ArrayList<HomePoJo> homePoJos, HomeItemClickListener homeItemClickListener) {
        this.context = context;
        this.homePoJos = homePoJos;
        this.homeItemClickListener = homeItemClickListener;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.home_single_layout,viewGroup,false);

        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder homeViewHolder, int i) {

        HomePoJo homePoJo = homePoJos.get(i);
        homeViewHolder.description_tv.setText(homePoJo.getDescription());
        homeViewHolder.price_tv.setText(homePoJo.getAmount()+ " Taka");

            Uri uri = Uri.parse(homePoJo.getDownloadUrls().get(0));
            Picasso.get().load(uri).into(homeViewHolder.image_one_iv);


    }

    @Override
    public int getItemCount() {
        return homePoJos.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        TextView description_tv,price_tv;
        ImageView image_one_iv;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);

            description_tv = itemView.findViewById(R.id.description_tv);
            price_tv = itemView.findViewById(R.id.price_tv);
            image_one_iv = itemView.findViewById(R.id.image_one_iv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    homeItemClickListener.onClickHomeItemClickListener(homePoJos.get(getAdapterPosition()));
                }
            });
        }
    }
    public void updateHomeList(ArrayList<HomePoJo>homePoJos){
        this.homePoJos = homePoJos;
        notifyDataSetChanged();
    }
    public interface HomeItemClickListener{
        void onClickHomeItemClickListener(HomePoJo homePoJo);
    }
}

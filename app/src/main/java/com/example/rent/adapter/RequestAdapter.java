package com.example.rent.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;


import com.example.rent.pojo.BookingPoJo;
import com.example.rent.pojo.FeedBackPoJo;
import com.example.rent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RequestViewHolder> {

    private Context context;
    private ArrayList<BookingPoJo> bookingPoJos;
    private RequestClickListener clickListener;
    int type;
    private DatabaseReference bookDatabaseReference, rattingDatabaseReference;

    public RequestAdapter(Context context, ArrayList<BookingPoJo> bookingPoJos, RequestClickListener clickListener, int type) {
        this.context = context;
        this.bookingPoJos = bookingPoJos;
        this.clickListener = clickListener;
        this.type = type;
        bookDatabaseReference = FirebaseDatabase.getInstance().getReference("Booking");
        rattingDatabaseReference = FirebaseDatabase.getInstance().getReference("Ratting");

    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.my_booking, parent, false);

        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, final int position) {


        final BookingPoJo bookingPoJo = bookingPoJos.get(position);
        holder.price_tv.setText(bookingPoJo.getPrice());
        int dateCount = bookingPoJo.getDates().size();
        if (dateCount > 1) {
            holder.dates_tv.setText("Date: " + bookingPoJo.getDates().get(0) + " To " + bookingPoJo.getDates().get(dateCount - 1));
        } else {
            holder.dates_tv.setText("Date: " + bookingPoJo.getDates().get(0));
        }

        if (type == 10) {
            //Notification
            holder.description_tv.setText("Booking request for your\n"+bookingPoJo.getDescription());
            holder.checkout_btn.setVisibility(View.GONE);
        } else if (type == 100) {
            //Booking
            holder.description_tv.setText("You have booked\n"+bookingPoJo.getDescription());
            holder.checkout_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bookingPoJos.get(position);
                    checkOut(bookingPoJo);
                }
            });
        } else {
            //Booking History
            holder.description_tv.setText(bookingPoJo.getDescription());
            holder.checkout_btn.setVisibility(View.GONE);
        }


    }

    private void checkOut(final BookingPoJo bookingPoJo) {

        bookDatabaseReference.child("Accept").child(bookingPoJo.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {

                    bookDatabaseReference.child("History").child(bookingPoJo.getId()).setValue(bookingPoJo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                createFeedBackDialog(bookingPoJo.getBookerId(), bookingPoJo.getAdId());
                            }
                        }
                    });
                }

            }
        });
    }

    private void createFeedBackDialog(final String feedBackerId, final String addId) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.feed_back_layout, null);
        builder.setView(dialogView);
        final EditText comment_et = dialogView.findViewById(R.id.comment_et);
        final RatingBar rating_bar = dialogView.findViewById(R.id.rating_bar);
        final Button submit_btn = dialogView.findViewById(R.id.submit_btn);

        final AlertDialog alertDialog = builder.show();

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String comment = comment_et.getText().toString();
                String ratting = String.valueOf(rating_bar.getRating());

                String id = rattingDatabaseReference.push().getKey();
                FeedBackPoJo feedBackPoJo = new FeedBackPoJo(id, feedBackerId, addId, comment, ratting);

                rattingDatabaseReference.child(id).setValue(feedBackPoJo).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(context, "FeedBack Added", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(context, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
    }


    @Override
    public int getItemCount() {
        return bookingPoJos.size();
    }

    public class RequestViewHolder extends RecyclerView.ViewHolder {

        TextView description_tv, price_tv, dates_tv;
        Button checkout_btn;

        public RequestViewHolder(@NonNull final View itemView) {
            super(itemView);

            description_tv = itemView.findViewById(R.id.description_tv);
            price_tv = itemView.findViewById(R.id.price_tv);
            dates_tv = itemView.findViewById(R.id.dates_tv);
            checkout_btn = itemView.findViewById(R.id.checkout_btn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onClick(bookingPoJos.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface RequestClickListener {

        void onClick(BookingPoJo bookingPoJo);
    }

    public void updateBookinglList(ArrayList<BookingPoJo> bookingPoJos) {

        this.bookingPoJos = bookingPoJos;
        notifyDataSetChanged();

    }
}

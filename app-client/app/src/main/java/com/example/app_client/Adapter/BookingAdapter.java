package com.example.app_client.Adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_client.Model.Booking;
import com.example.app_client.Model.User;
import com.example.app_client.R;
import com.example.app_client.Utils.LoginManager;
import com.example.app_client.View.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {
    public interface BookingConfirmation{
        void onBookingDeleted(Booking booking);
        void onBookingConfirmed(Booking booking);
    }

    private BookingConfirmation bookingConfirmation;
    private List<Booking> bookings;
    private String username;

    public BookingAdapter(BookingConfirmation bookingConfirmation) {
        bookings = new ArrayList<>();
        this.bookingConfirmation = bookingConfirmation;
        User user = LoginManager.getUser();
        username = user.getUsername();
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      /*  View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookings_slot,parent,false);
        return new MyViewHolder(view);*/
      return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.MyViewHolder holder, int position) {
        Booking booking = bookings.get(position);

        boolean ownBooking = booking.getUser().equals(username);

        holder.bookTextView.setText(booking.getDate());
        holder.courseTextView.setText(booking.getSubject());
//      holder.startTextView.setText(booking.getStart()); TODO: Get hour
        holder.teacherTextView.setText(booking.getTeacher() + "" + booking.getStatus());
        holder.statusTextView.setText(booking.getStatus());
        holder.tvUser.setText(booking.getUser());
        holder.tvUser.setVisibility(ownBooking ? View.GONE : View.VISIBLE);

        holder.confirmButton.setVisibility(View.GONE);
        holder.statusTextView.setVisibility(View.GONE);

        if (booking.isCancelled()) {
            holder.statusTextView.setVisibility(View.VISIBLE);
            holder.statusTextView.setTextColor(Color.parseColor("#DF0B0B"));
        } else if(booking.isConfirmed()) {
            holder.statusTextView.setVisibility(View.VISIBLE);
            holder.statusTextView.setTextColor(ContextCompat.getColor(
                    holder.bookTextView.getContext(), R.color.colorPrimary));
        }

        if (booking.isInit()){
            // TODO: 13/11/2020 Booking
        }
    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView bookTextView;
        TextView startTextView;
        TextView statusTextView;
        TextView teacherTextView;
        TextView courseTextView,tvUser;
        Button confirmButton;

        public MyViewHolder( View itemView) {
            super(itemView);
//            bookTextView = itemView.findViewById(R.id.tvDate);
//            startTextView = itemView.findViewById(R.id.tvStart);
//            statusTextView = itemView.findViewById(R.id.tvStatus);
//            teacherTextView = itemView.findViewById(R.id.tvTeacher);
//            courseTextView = itemView.findViewById(R.id.tvCourse);
//            confirmButton = itemView.findViewById(R.id.btnConfirm);
//            tvUser = itemView.findViewById(R.id.tvUser);
        }
    }

}

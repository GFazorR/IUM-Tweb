package com.example.app_client.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_client.Model.Booking;
import com.example.app_client.R;
import com.example.app_client.Utils.TimeUtility;
import com.google.android.material.button.MaterialButton;

import java.time.LocalDateTime;
import java.util.ArrayList;



public class DashboardRCAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Booking> bookings;
    LayoutInflater inflater;
    private ClickListener listener;


    public DashboardRCAdapter(ArrayList<Booking> bookings, Context context) {
        this.bookings = bookings;
        this.inflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<Booking> bookings){
        this.bookings.addAll(bookings);
        notifyDataSetChanged();
    }

    public void clear(){
        int size = getItemCount();
        bookings.clear();
        notifyItemRangeRemoved(0,size);
    }

    public void setItemStatus(int position, int status){
        bookings.get(position).setStatus(status);
        notifyItemChanged(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_booking, parent, false);
        if (viewType == 1){
            return new DefaultViewHolder(view);
        }else if (viewType == 2){
            return new ConfirmViewHolder(view);
        }else return new TestViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Booking booking = bookings.get(position);
        switch (getItemViewType(position)){
            case 1:
                DefaultViewHolder defaultViewHolder = (DefaultViewHolder)holder;
                defaultViewHolder.subjectName.setText(booking.getSubject());
                defaultViewHolder.teacherName.setText(booking.getTeacher());
                defaultViewHolder.timeSlot.setText(TimeUtility.formatTimestamp(booking.getDate(),
                        "EEE dd/MM/YY HH:mm"));
                defaultViewHolder.status.setText(booking.getStatusTitle());
                defaultViewHolder.status.setTextColor(Color.GRAY);
                defaultViewHolder.button.setText("Elimina");
                defaultViewHolder.button.setBackgroundColor(Color.parseColor("#B00020"));
                break;
            case 2:
                ConfirmViewHolder confirm = (ConfirmViewHolder)holder;
                confirm.subjectName.setText(booking.getSubject());
                confirm.teacherName.setText(booking.getTeacher());
                confirm.timeSlot.setText(TimeUtility.formatTimestamp(booking.getDate(),
                        "EEE dd/MM/YY HH:mm"));
                confirm.status.setText(booking.getStatusTitle());
                confirm.status.setTextColor(Color.GRAY);
                confirm.button.setBackgroundColor(Color.parseColor("#00c87a"));
                break;
            case 3:
                TestViewHolder noButtonVH = (TestViewHolder)holder;
                noButtonVH.subjectName.setText(booking.getSubject());
                noButtonVH.teacherName.setText(booking.getTeacher());
                noButtonVH.timeSlot.setText(TimeUtility.formatTimestamp(booking.getDate(),
                        "EEE dd/MM/YY HH:mm"));
                noButtonVH.status.setText(booking.getStatusTitle());
                if (booking.getStatus() == 20)
                    noButtonVH.status.setTextColor(Color.parseColor("#B00020"));
                else noButtonVH.status.setTextColor(Color.parseColor("#00c87a"));
                noButtonVH.button.setVisibility(View.GONE);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int getItemViewType(int position) {
        Booking booking = bookings.get(position);
        if (booking.getStatus() == 10){
            if (booking.getDate().compareTo(LocalDateTime.now())>0){
                return 1;
            }
            else return 2;
        }else return 3;
    }


    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public class DefaultViewHolder extends RecyclerView.ViewHolder{
        private TextView subjectName;
        private TextView teacherName;
        private TextView timeSlot;
        private TextView status;
        private MaterialButton button;



        public DefaultViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectBooked);
            teacherName = itemView.findViewById(R.id.teacherName);
            timeSlot = itemView.findViewById(R.id.bookingDate);
            status = itemView.findViewById(R.id.status);
            button = itemView.findViewById(R.id.confirmButton);

            button.setOnClickListener(v -> {
                if (listener != null) listener.onClickCancel(getAdapterPosition());
            });



        }
    }

    public class ConfirmViewHolder extends RecyclerView.ViewHolder{
        private TextView subjectName;
        private TextView teacherName;
        private TextView timeSlot;
        private TextView status;
        private Button button;


        public ConfirmViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectBooked);
            teacherName = itemView.findViewById(R.id.teacherName);
            timeSlot = itemView.findViewById(R.id.bookingDate);
            status = itemView.findViewById(R.id.status);

            button = itemView.findViewById(R.id.confirmButton);
            button.setOnClickListener(v -> {
                if (listener != null) listener.onClickConfirm(getAdapterPosition());
            });

        }
    }

    public class TestViewHolder extends RecyclerView.ViewHolder{
        private TextView subjectName;
        private TextView teacherName;
        private TextView timeSlot;
        private TextView status;
        private Button button;

        public TestViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.subjectBooked);
            teacherName = itemView.findViewById(R.id.teacherName);
            timeSlot = itemView.findViewById(R.id.bookingDate);
            status = itemView.findViewById(R.id.status);
            button = itemView.findViewById(R.id.confirmButton);
        }
    }

    public Booking getItem(int position){
        return bookings.get(position);
    }

    public void setListener(ClickListener listener){
        this.listener = listener;
    }

    public interface ClickListener{
        void onClickConfirm(int position);
        void onClickCancel(int position);
    }
}

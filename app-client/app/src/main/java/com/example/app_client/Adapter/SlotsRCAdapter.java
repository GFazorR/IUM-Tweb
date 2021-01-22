package com.example.app_client.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_client.Model.Slot;
import com.example.app_client.R;
import com.example.app_client.Utils.TimeUtility;
import com.google.android.flexbox.AlignContent;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.button.MaterialButton;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

public class SlotsRCAdapter extends RecyclerView.Adapter<SlotsRCAdapter.ViewHolder> {

    private Map<String, ArrayList<Slot>> calendar;
    private ArrayList<String> keys;
    private LayoutInflater inflater;
    private Activity activity;
    private ClickListener slotSelected;

    public SlotsRCAdapter(Map<String, ArrayList<Slot>> calendar, Context context, Activity activity) {
        this.calendar = calendar;
        this.keys = new ArrayList<>(calendar.keySet());
        this.inflater = LayoutInflater.from(context);
        this.activity = activity;
    }

    public void dataSetChange(){
        this.keys.addAll(calendar.keySet());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.slot_item,parent,false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocalDate date = LocalDate.parse(keys.get(position));
        ArrayList<Slot> daySlots = calendar.get(keys.get(position));
        holder.dayHeader.setText(TimeUtility.formatWeekday(date));
        holder.flexboxLayout.removeAllViews();
        holder.flexboxLayout.setFlexDirection(FlexDirection.COLUMN);
        holder.flexboxLayout.setJustifyContent(JustifyContent.CENTER);
        holder.flexboxLayout.setAlignContent(AlignContent.CENTER);
        for(Slot slot: daySlots){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0,0,10,0);
            MaterialButton button = new MaterialButton(holder.flexboxLayout.getContext());
            button.setMinimumWidth(0);
            button.setPadding(0,0,10,0);
            button.setWidth(getScreenWidth());
            button.setLayoutParams(params);
            button.setText(TimeUtility.formatTimestamp(slot.getDate(),"HH:mm"));
            if(!slot.isAvailable()){

                button.setEnabled(false);
                button.setBackgroundColor(Color.RED);
            }
            button.setOnClickListener(view -> slotSelected.onSlotSelected(slot));
            holder.flexboxLayout.addView(button);
        }


    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x/6;
    }

    @Override
    public int getItemCount() {
        return keys.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayHeader;
        FlexboxLayout flexboxLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayHeader = itemView.findViewById(R.id.day_header);
            flexboxLayout = itemView.findViewById(R.id.flexbox);
        }
    }

    public void setListener(ClickListener listener){
        this.slotSelected = listener;
    }

    public interface ClickListener{
        void onSlotSelected(Slot s);
    }
}

package com.example.app_client.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_client.Model.Slot;
import com.example.app_client.R;
import com.example.app_client.Utils.TimeUtility;

import java.util.ArrayList;

public class SlotRCAdapter extends RecyclerView.Adapter<SlotRCAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Slot> slots;
    private LayoutInflater inflater;

    public SlotRCAdapter(Context context, ArrayList<Slot> slots) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.slots = new ArrayList<>(slots);
    }

    public void setSlots(ArrayList<Slot> slots){
        this.slots = slots;
        notifyDataSetChanged();
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_slot,parent,false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String FORMAT_HOURS = "HH:mm";
        holder.slot.setText(TimeUtility.formatTimestamp(slots.get(position).getDate(),FORMAT_HOURS));
        if (!slots.get(position).isAvailable())
            ((Button) holder.slot).setBackgroundColor(Color.RED);
    }

    @Override
    public int getItemCount() {
        System.out.println(slots.size());
        return slots.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        Button slot;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            slot = itemView.findViewById(R.id.button);
        }
    }
}

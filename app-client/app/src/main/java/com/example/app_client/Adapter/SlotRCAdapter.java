package com.example.app_client.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_client.Model.Slot;
import com.example.app_client.R;
import com.example.app_client.Utils.TimeUtility;

import java.util.ArrayList;

public class SlotRCAdapter extends RecyclerView.Adapter<SlotRCAdapter.ViewHolder> {
    private ArrayList<Slot> slots;
    private LayoutInflater inflater;
    private int selectedItem = RecyclerView.NO_POSITION;
    private Activity activity;
    private ClickListener listener;


    public SlotRCAdapter(ArrayList<Slot> slots, Context context, Activity activity) {
        this.slots = new ArrayList<>(slots);
        this.inflater = LayoutInflater.from(context);
        this.activity = activity;
    }
    public void setData(ArrayList<Slot> slots){
        this.slots.addAll(slots);
        notifyDataSetChanged();
    }



    public void clear(){
        int size = getItemCount();
        slots.clear();
        selectedItem = RecyclerView.NO_POSITION;
        notifyItemRangeRemoved(0,size);
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_slot,parent,false);
        return new ViewHolder(view);
    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x/getItemCount();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.slotTextView.setVisibility(View.GONE);
        if (slots.get(position).isAvailable()){
            holder.slotTextView.setText(TimeUtility.formatTimestamp(slots.get(position).getDate(),"HH:mm"));
            holder.slotTextView.getLayoutParams().width = getScreenWidth();
            holder.slotTextView.setVisibility(View.VISIBLE);
            holder.slotTextView.setSelected(selectedItem == position);
        }
    }

    @Override
    public int getItemCount() {
        return slots.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView slotTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            slotTextView = itemView.findViewById(R.id.slot_text);
            slotTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            notifyItemChanged(selectedItem);
            selectedItem = getLayoutPosition();
            notifyItemChanged(selectedItem);
            if (listener != null) listener.onSlotClick(v, getAdapterPosition());
        }
    }

    public Slot getSelectedItem(){
        return slots.get(selectedItem);
    }

    public void setListener(ClickListener listener) { this.listener = listener; }

    public interface ClickListener{
        void onSlotClick(View view, int position);
    }


}

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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

public class DaysRCAdapter extends RecyclerView.Adapter<DaysRCAdapter.ViewHolder> {
    private Map<String, ArrayList<Slot>> calendar;
    private ArrayList<String> keys;
    private LayoutInflater inflater;
    private Activity activity;
    private int selectedItem = RecyclerView.NO_POSITION;
    private ClickListener listener;

    public DaysRCAdapter(Map<String, ArrayList<Slot>> calendar, Context context,Activity activity) {
        this.calendar = calendar;
        this.keys = new ArrayList<>(calendar.keySet());
        this.inflater = LayoutInflater.from(context);
        this.activity = activity;
    }

    public void setKeys(){
        this.keys.addAll(calendar.keySet());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_teacher,parent,false);
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

        if (calendar.get(keys.get(position)).stream().anyMatch(Slot::isAvailable)){
            LocalDate date = LocalDate.parse(keys.get(position));
            holder.dayTextView.setText(TimeUtility.formatWeekday(date));
            holder.dayTextView.getLayoutParams().width = getScreenWidth();
            holder.dayTextView.setSelected(selectedItem == position);
        }
    }

    @Override
    public int getItemCount() {
        return keys.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView dayTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.teacher_button);
            dayTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            notifyItemChanged(selectedItem);
            selectedItem = getLayoutPosition();
            notifyItemChanged(selectedItem);
            if (listener != null) listener.onDayClick(v, getAdapterPosition());
        }
    }

    public void setListener(DaysRCAdapter.ClickListener listener){this.listener = listener;}

    public String getItem(int position){
        return keys.get(position);
    }

    public interface ClickListener{
        void onDayClick(View view, int position);
    }

}

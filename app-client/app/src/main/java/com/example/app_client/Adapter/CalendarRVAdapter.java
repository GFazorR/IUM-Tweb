package com.example.app_client.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.Set;
import java.util.zip.Inflater;

public class CalendarRVAdapter extends RecyclerView.Adapter<CalendarRVAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private Map<String, ArrayList<Slot>> calendar;
    private ArrayList<String> keys;

    public CalendarRVAdapter(Context context, Map<String, ArrayList<Slot>> calendar) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.calendar = calendar;
        this.keys = new ArrayList<>();
    }

    public void setAllData(Set<String> set){
        this.keys.addAll(set);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_calendar_col,parent,false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocalDate date = LocalDate.parse(keys.get(position));
        holder.header.setText(TimeUtility.formatWeekday(date));
    }

    @Override
    public int getItemCount() {
        return keys.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView header;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.header_id);
        }
    }
}

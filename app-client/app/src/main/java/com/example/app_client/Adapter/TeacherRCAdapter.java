package com.example.app_client.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_client.Model.Teacher;
import com.example.app_client.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class TeacherRCAdapter extends RecyclerView.Adapter<TeacherRCAdapter.ViewHolder> {
    private ArrayList<Teacher> teachers;
    private LayoutInflater inflater;
    private Activity activity;
    private int selectedItem =RecyclerView.NO_POSITION;
    private ClickListener listener;

    public TeacherRCAdapter(ArrayList<Teacher> teachers, Context context, Activity activity) {
        this.teachers = teachers;
        this.inflater = LayoutInflater.from(context);
        this.activity = activity;
    }

    public void setData(ArrayList<Teacher> teachers){
        this.teachers.addAll(teachers);
        notifyDataSetChanged();
    }



    public void clear(){
        int size = getItemCount();
        teachers.clear();
        selectedItem = RecyclerView.NO_POSITION;
        notifyItemRangeRemoved(0,size);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_teacher, parent, false);
        return new ViewHolder(view);
    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size.x/4;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.materialButton.setVisibility(View.GONE);
        holder.materialButton.setText(teachers.get(position).getName());
        holder.materialButton.setVisibility(View.VISIBLE);
        holder.materialButton.setSelected(position == selectedItem);
    }

    @Override
    public int getItemCount() {
        return teachers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        MaterialButton materialButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            materialButton = itemView.findViewById(R.id.teacher_button);
            materialButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            notifyItemChanged(selectedItem);
            selectedItem = getLayoutPosition();
            notifyItemChanged(selectedItem);
            if (listener != null) listener.onTeacherClick(v,getAdapterPosition());
        }
    }

    public Teacher getItem(int position){
        return teachers.get(position);
    }

    public void setListener(ClickListener listener) { this.listener = listener; }

    public interface ClickListener{
        void onTeacherClick(View view, int position);
    }

}

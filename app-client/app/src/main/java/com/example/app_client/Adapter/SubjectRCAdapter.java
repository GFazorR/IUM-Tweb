package com.example.app_client.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_client.Model.Subject;
import com.example.app_client.R;

import java.util.ArrayList;

public class SubjectRCAdapter extends RecyclerView.Adapter<SubjectRCAdapter.ViewHolder> {

    private ArrayList<Subject> subjects;
    private Context context;
    private LayoutInflater inflater;
    private ClickListener listener;

    public SubjectRCAdapter(ArrayList<Subject> subjects, Context context) {
        this.subjects = subjects;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_subject, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.subjectView.setText(subjects.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView subjectView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectView = itemView.findViewById(R.id.subject_name);
            subjectView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) listener.onItemClick(v, getAdapterPosition());
        }
    }

    public Subject getItem(int position){
        return subjects.get(position);
    }

    public void setClickListener(ClickListener clickListener){
        this.listener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View view, int position);
    }
}

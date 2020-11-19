package com.example.app_client.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_client.Adapter.CalendarRVAdapter;
import com.example.app_client.Api.RetrofitClient;
import com.example.app_client.Model.Slot;
import com.example.app_client.Model.Subject;
import com.example.app_client.R;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CalendarFragment extends Fragment {

    private Subject subject;
    private TextView textView;
    private Map<String, ArrayList<Slot>> calendar;
    private RecyclerView calendarView;
    private CalendarRVAdapter adapter;

    public void getSubjectCalendar(){
        RetrofitClient.getApi().getSubjectSlots(subject.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(map-> {
                   calendar.putAll(map);
                   adapter.setAllData(map.keySet());
                });
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar,container,false);
        textView = view.findViewById(R.id.test);
        Subject subject = (Subject) getActivity().getIntent().getSerializableExtra("Subject");
        calendar = new TreeMap<>();
        this.subject = subject;
        textView.setText(subject.getName());

        calendarView = view.findViewById(R.id.calendar_recycler_view);
        calendarView.setLayoutManager(new GridLayoutManager(getContext(),5));
        adapter = new CalendarRVAdapter(getContext(),calendar);
        calendarView.setAdapter(adapter);

        getSubjectCalendar();

        return view;
    }
}

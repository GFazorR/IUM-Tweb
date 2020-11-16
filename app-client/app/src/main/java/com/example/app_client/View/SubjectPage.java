package com.example.app_client.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app_client.Adapter.SubjectAdapter;
import com.example.app_client.Api.RetrofitClient;
import com.example.app_client.Model.Subject;
import com.example.app_client.R;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SubjectPage extends Fragment {

    private ListView listView;
    private ArrayList<Subject> subjectArrayList;
    private SubjectAdapter subjectAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        subjectArrayList = new ArrayList<>();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.subjects_page,container,false);
        listView = (ListView) rootView.findViewById(R.id.subjectListView);
        subjectAdapter = new SubjectAdapter(getContext(), R.layout.list_item, subjectArrayList);
        listView.setAdapter(subjectAdapter);

        getSubjects();

        return  rootView;
    }



    private void getSubjects(){
        RetrofitClient.getApi().getSubjects()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        l -> {
                            subjectArrayList.addAll(l);
                            subjectAdapter.notifyDataSetChanged();
                        }
                );
    }

}

package com.example.app_client.View;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_client.Adapter.SubjectRCAdapter;
import com.example.app_client.Api.RetrofitClient;
import com.example.app_client.Model.Subject;
import com.example.app_client.R;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SubjectFragment extends Fragment implements SubjectRCAdapter.ClickListener{

    private RecyclerView subjectView;
    private SubjectRCAdapter recyclerAdapter;
    private ArrayList<Subject> subjectArrayList;
    private AlertDialog errorDialog;
    private AlertDialog progressDialog;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        subjectArrayList = new ArrayList<>();
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.subjects_fragment,container,false);

        progressDialog = ((MainActivity) getActivity()).getProgressDialog(getContext());
        progressDialog.show();


        subjectView = rootView.findViewById(R.id.recycler_subjects);
        subjectView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAdapter = new SubjectRCAdapter(subjectArrayList, getContext());
        recyclerAdapter.setClickListener(this);
        subjectView.setAdapter(recyclerAdapter);

        getSubjects();

        return  rootView;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showResult(Throwable throwable){
        progressDialog.dismiss();
        if (throwable != null) {
            errorDialog = ((MainActivity) getActivity()).getErrorDialog(getContext(), throwable, v -> getSubjects());
            errorDialog.onBackPressed();
            errorDialog.show();
        }
    }





    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getSubjects(){
        if (errorDialog != null && errorDialog.isShowing())
            errorDialog.dismiss();
        progressDialog.show();
        RetrofitClient.getApi().getSubjects()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        l -> {
                            subjectArrayList.addAll(l);
                            recyclerAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        },
                        this::showResult
                );

    }



    public void startBooking(Subject subject){
        Intent intent = new Intent(getContext(), BookingActivity.class);
        intent.putExtra("Subject", subject);
        startActivity(intent);
    }

    @Override
    public void onItemClick(View view, int position) {
        startBooking(recyclerAdapter.getItem(position));
    }
}

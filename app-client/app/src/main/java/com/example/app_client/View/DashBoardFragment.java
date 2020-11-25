package com.example.app_client.View;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_client.Adapter.DashboardRCAdapter;
import com.example.app_client.Api.RetrofitClient;
import com.example.app_client.Model.Booking;
import com.example.app_client.R;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DashBoardFragment extends Fragment implements DashboardRCAdapter.ClickListener {

    private ArrayList<Booking> bookings;
    private RecyclerView bookedSlots;
    private DashboardRCAdapter dashBoardAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard,container,false);
        // TODO: 24/11/2020 add progress dialog and error dialog
        this.bookings = new ArrayList<>();
        bookedSlots = rootView.findViewById(R.id.booked_slots);
        bookedSlots.setLayoutManager(new LinearLayoutManager(getContext()));
        // TODO: 24/11/2020 adapter
        dashBoardAdapter = new DashboardRCAdapter(bookings, getContext());
        dashBoardAdapter.setListener(this);
        bookedSlots.setAdapter(dashBoardAdapter);

        getBookings();

        return rootView;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        getBookings();

    }


    @SuppressLint("NewApi")
    private void getBookings() {
        RetrofitClient.getApi().getBookings()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(l->{
                    dashBoardAdapter.clear();
                    bookings.clear();
                    bookings = (ArrayList<Booking>) l;
                    dashBoardAdapter.setData(bookings);
                });
    }

    // TODO: 25/11/2020 retrofit call confirm
    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void confirmBooking(int bookingId){
        RetrofitClient.getApi().confirmBookings(bookingId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        v-> getBookings()
                );
    }

    private void onError(Throwable throwable){
        Toast.makeText(getContext(),throwable.getMessage(),Toast.LENGTH_SHORT);
    }


    // TODO: 25/11/2020 retrofit call cancel
    @SuppressLint("CheckResult")
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void cancelBooking(int bookingId){
        RetrofitClient.getApi().cancelBookings(bookingId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        v-> getBookings(),this::onError
                );
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClickConfirm(int position) {
        confirmBooking(dashBoardAdapter.getItem(position).getId());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClickCancel(int position) {
        cancelBooking(dashBoardAdapter.getItem(position).getId());
    }
}

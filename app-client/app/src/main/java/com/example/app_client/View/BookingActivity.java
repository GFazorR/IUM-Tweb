package com.example.app_client.View;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_client.Adapter.DaysRCAdapter;
import com.example.app_client.Adapter.SlotRCAdapter;
import com.example.app_client.Adapter.SlotsRCAdapter;
import com.example.app_client.Adapter.TeacherRCAdapter;
import com.example.app_client.Api.RetrofitClient;
import com.example.app_client.Model.Booking;
import com.example.app_client.Model.Slot;
import com.example.app_client.Model.Subject;
import com.example.app_client.R;
import com.example.app_client.Utils.LoginManager;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;

public class BookingActivity extends BaseActivity implements SlotsRCAdapter.ClickListener, TeacherRCAdapter.ClickListener {
    private String selectedSubject;
    private Subject subject;
    private Booking booking;
    private TextView textView;
    private Map<String, ArrayList<Slot>> calendar;
    private RecyclerView slotsRecycler;
    private SlotsRCAdapter slotsRecyclerAdapter;
    private AlertDialog progressDialog;
    private AlertDialog errorDialog;
    private RecyclerView teacherRecycler;
    private TeacherRCAdapter teacherRCAdapter;
    private MaterialButton bookSlotButton;
    static boolean isRunning = false;

    @Override
    protected void onStart() {
        super.onStart();
        isRunning = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunning = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (booking.getUser() == null && LoginManager.getUser() != null)
            booking.setUser(LoginManager.getUser().getUsername());
    }

    public void setSubject(){
        Subject subject = (Subject) getIntent().getSerializableExtra("Subject");
        this.subject = subject;
        if (LoginManager.getUser() != null)
            this.booking = new Booking(subject.getName(), LoginManager.getUser().getUsername(),10);
        else this.booking = new Booking(subject.getName(), null,10);
    }

    // TODO: 24/11/2020 Code Reorganize and refactor

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        calendar = new TreeMap<>();


        progressDialog = getProgressDialog(this);
        progressDialog.show();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        textView = findViewById(R.id.subject_click);

        setSubject();
        textView.setText(String.format("Seleziona lo slot per %s", subject.getName()));

        slotsRecycler = findViewById(R.id.recview_slots);
        slotsRecycler.setItemAnimator(new DefaultItemAnimator());
        slotsRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        slotsRecyclerAdapter = new SlotsRCAdapter(calendar,this,this);
        slotsRecyclerAdapter.setListener(this);
        slotsRecycler.setAdapter(slotsRecyclerAdapter);

        getSubjectCalendar();

        /*slotsRecycler = findViewById(R.id.recview_slots);
        daysRCAdapter = new DaysRCAdapter(calendar, this, this);
        daysRCAdapter.setListener(this);
        slotsRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        slotsRecycler.setAdapter(daysRCAdapter);*/



        teacherRecycler = findViewById(R.id.teacher_recycler);
        teacherRCAdapter = new TeacherRCAdapter(new ArrayList<>(),this,this);
        teacherRCAdapter.setListener(this);
        teacherRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        teacherRecycler.setAdapter(teacherRCAdapter);

        bookSlotButton = findViewById(R.id.book_slot_button);
        bookSlotButton.setOnClickListener(v -> {
            if (booking.getDate() != null && booking.getTeacherId() != 0){
                RetrofitClient.getApi().bookSlot(subject.getId(), booking.getTeacherId(),
                        booking.getDate().toString())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(b -> {
                            progressDialog.show();
                            System.out.println(b);
                            progressDialog.dismiss();
                            finish();
                        },this::showResult);
            }else
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();

        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showResult(Throwable throwable){
        progressDialog.dismiss();
        if (throwable != null){
            if(throwable instanceof HttpException){
                HttpException exception = (HttpException)throwable;
                if (exception.code() == 401){
                    Toast toast = Toast.makeText(this,
                            "E' necessario effettuare il login",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }else {
                Toast toast = Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT);
                toast.show();
            }

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getSubjectCalendar(){
        RetrofitClient.getApi().getSubjectSlots(subject.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(map-> {
                    calendar.putAll(map);
                    slotsRecyclerAdapter.dataSetChange();
                    progressDialog.dismiss();
                }, this::showResult);
    }


    @Override
    public void onBackPressed() {
        if (errorDialog != null)
            errorDialog.dismiss();
        finish();
    }



    @Override
    public void onTeacherClick(View view, int position) {
        booking.setTeacher(teacherRCAdapter.getItem(position).getName());
        booking.setTeacherId(teacherRCAdapter.getItem(position).getId());

        System.out.println(booking.getSubject());
        System.out.println(booking.getDate());
        System.out.println(booking.getTeacher());
        System.out.println(booking.getTeacherId());
    }

    @Override
    public void onSlotSelected(Slot slot) {
        booking.setDate(slot.getDate());
        teacherRCAdapter.clear();
        teacherRCAdapter.setData(new ArrayList<>(slot.getTeachers()));
    }
}

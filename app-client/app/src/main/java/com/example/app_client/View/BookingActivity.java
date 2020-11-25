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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_client.Adapter.DaysRCAdapter;
import com.example.app_client.Adapter.SlotRCAdapter;
import com.example.app_client.Adapter.TeacherRCAdapter;
import com.example.app_client.Api.RetrofitClient;
import com.example.app_client.Model.Booking;
import com.example.app_client.Model.Slot;
import com.example.app_client.Model.Subject;
import com.example.app_client.R;
import com.example.app_client.Utils.LoginManager;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BookingActivity extends BaseActivity implements DaysRCAdapter.ClickListener, SlotRCAdapter.ClickListener, TeacherRCAdapter.ClickListener {
    private Toolbar actionBar;
    private String selectedSubject;
    private Subject subject;
    private Booking booking;
    private TextView textView;
    private Map<String, ArrayList<Slot>> calendar;
    private RecyclerView daysRecycler;
    private DaysRCAdapter daysRCAdapter;
    private RecyclerView slotRecycler;
    private SlotRCAdapter slotRCAdapter;
    private AlertDialog progressDialog;
    private AlertDialog errorDialog;
    private RecyclerView teacherRecycler;
    private TeacherRCAdapter teacherRCAdapter;
    private Button bookSlotButton;


    public void setSubject(){
        Subject subject = (Subject) getIntent().getSerializableExtra("Subject");
        this.subject = subject;
        this.booking = new Booking(subject.getName(), LoginManager.getUser().getUsername(),10);
    }

    // TODO: 24/11/2020 Code Reorganize and refactor

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        calendar = new TreeMap<>();
        actionBar = findViewById(R.id.booking_toolbar);

        progressDialog = getProgressDialog(this);
        progressDialog.show();

        setSupportActionBar(actionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        textView = findViewById(R.id.subject_click);

        setSubject();
        textView.setText(subject.getName());

        daysRecycler = findViewById(R.id.days_recycler);
        daysRCAdapter = new DaysRCAdapter(calendar, this, this);
        daysRCAdapter.setListener(this);
        daysRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        daysRecycler.setAdapter(daysRCAdapter);

        slotRecycler = findViewById(R.id.slot_recycler);
        slotRCAdapter = new SlotRCAdapter(new ArrayList<>(),this, this);
        slotRCAdapter.setListener(this);
        slotRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        slotRecycler.setAdapter(slotRCAdapter);

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
                            this.finish();
                        },this::showResult);
            }else
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        });





        getSubjectCalendar();

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
            errorDialog = getErrorDialog(this, throwable, l -> finish());
            errorDialog.show();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getSubjectCalendar(){
        RetrofitClient.getApi().getSubjectSlots(subject.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(map-> {
                    calendar.putAll(map);
                    daysRCAdapter.setKeys();
                    progressDialog.dismiss();
                }, this::showResult);
    }


    @Override
    public void onBackPressed() {
        if (errorDialog != null)
            errorDialog.dismiss();
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDayClick(View view, int position) {
        this.selectedSubject = daysRCAdapter.getItem(position);
        slotRCAdapter.clear();
        slotRCAdapter.setData(new ArrayList<>(calendar.get(selectedSubject)));
    }

    @Override
    public void onSlotClick(View view, int position) {
        booking.setDate(slotRCAdapter.getSelectedItem().getDate());
        teacherRCAdapter.clear();
        teacherRCAdapter.setData(new ArrayList<>(calendar.get(selectedSubject).get(position).getTeachers()));
    }

    @Override
    public void onTeacherClick(View view, int position) {
        booking.setTeacher(teacherRCAdapter.getItem(position).getName());
        booking.setTeacherId(teacherRCAdapter.getItem(position).getId());
        System.out.println(booking.getUser());
        System.out.println(booking.getSubject());
        System.out.println(booking.getDate());
        System.out.println(booking.getTeacher());
        System.out.println(booking.getTeacherId());
    }
}

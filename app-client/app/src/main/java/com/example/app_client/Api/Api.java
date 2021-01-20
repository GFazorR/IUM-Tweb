package com.example.app_client.Api;
import com.example.app_client.Model.*;
import com.example.app_client.Utils.LoginManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import io.reactivex.Single;
import io.reactivex.internal.operators.single.SingleMap;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface Api {

    @GET("Slot")
    Single<Map<String, ArrayList<Slot>>> getSubjectSlots(
            @Query("subjectId") int subjectId
    );

    @POST("Login")
    Single<User> login(
            @Query("username") String username,
            @Query("password") String password
    );

    @POST("Logout")
    Single<Result<Void>> logout();

    @GET("Subjects")
    Single<List<Subject>> getSubjects();

    @GET("Booked_slots")
    Single<List<Booking>> getUnavailableSlots(
            @Query("teacher") int idTeacher
    );

    @GET("Bookings")
    Single<List<Booking>> getBookings();

    @POST("Bookings")
    Single<Booking> bookSlot(
            @Query("subject") int subjectId,
            @Query("teacher") int teacherId,
            @Query("date") String dateBooked
            );

    @DELETE("Bookings")
    Single<Response<Void>> cancelBookings(
            @Query("id") int idBooking
    );

    @PUT("Bookings")
    Single<Response<Void>> confirmBookings(
            @Query("id") int idBooking
    );


}

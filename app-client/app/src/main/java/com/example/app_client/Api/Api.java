package com.example.app_client.Api;
import com.example.app_client.Model.*;

import java.time.LocalDate;
import java.util.List;



import io.reactivex.Single;
import io.reactivex.internal.operators.single.SingleMap;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface Api {

    @GET("Slot")
    SingleMap<LocalDate,List<Slot>> getSubjectSlots(
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
    Single<Booking> createBookings(
      @Query("subject") int idSubject
    );

    @DELETE("Bookings")
    Single<Response<Void>> deleteBookings(
            @Query("id") int idBooking
    );

    @PUT("Bookings")
    Single<Booking> confirmBookings(
            @Query("id") int idBooking
    );


}

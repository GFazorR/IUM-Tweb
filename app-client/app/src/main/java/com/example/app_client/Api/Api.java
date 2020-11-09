package com.example.app_client.Api;
import com.example.app_client.Model.*;

import java.util.List;

import javax.xml.transform.Result;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    @POST("login")
    Single<User> login(
            @Query("username") String username,
            @Query("password") String password
    );

    @POST("logout")
    Single<Result<Void>> logout();

    @GET("subjects")
    Single<List<Subject>> getSubjects();


}

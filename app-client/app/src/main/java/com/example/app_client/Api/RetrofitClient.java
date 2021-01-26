package com.example.app_client.Api;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.app_client.Utils.LoginManager;
import com.example.app_client.Utils.UnauthorizedEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://192.168.178.159:8080/backend-v2/api/";
    private static Retrofit retrofit;

    public RetrofitClient() {}

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static Retrofit getRetrofitInstance(){
        if(retrofit == null){
            Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                    (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext)
                    -> LocalDateTime.parse(json.getAsString())).create();

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(getClient())
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient getClient(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(2, TimeUnit.SECONDS);


        httpClient.addInterceptor(chain -> {
            Request request = chain.request();
            HttpUrl.Builder builder = request.url().newBuilder();


            String token = LoginManager.getToken();

            if (token != null)
                builder = builder.addQueryParameter("token",token);


            HttpUrl url = builder.build();

            request = request.newBuilder().url(url).build();
            Response res= chain.proceed(request);

            if(res.code() == 401)
                EventBus.getDefault().post(UnauthorizedEvent.instance());
            return res;
        });

        return httpClient.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Api getApi(){return getRetrofitInstance().create(Api.class);}
}

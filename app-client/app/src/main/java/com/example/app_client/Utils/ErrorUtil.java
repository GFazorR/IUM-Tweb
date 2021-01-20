package com.example.app_client.Utils;

import android.content.Context;
import android.widget.Toast;

import com.example.app_client.Model.ErrorMessage;
import com.google.gson.Gson;

import java.io.InterruptedIOException;

import retrofit2.HttpException;

public class ErrorUtil {
    private ErrorUtil() {}

    public static void showErrorMessage(Context context, Throwable e) {
        Toast.makeText(context, getErrorMessage(e), Toast.LENGTH_LONG).show();
    }


    public static String getErrorMessage( Throwable e) {
        if (e instanceof HttpException){
            try {
                ErrorMessage serverError = new Gson().fromJson(((HttpException) e).response().errorBody().string(), ErrorMessage.class);
                return serverError.getError();

            } catch (Exception ex) { }
        } else if (e instanceof InterruptedIOException) {
            return  "Impossibile contattare il server!";

        }

        return e.getMessage();
    }
}

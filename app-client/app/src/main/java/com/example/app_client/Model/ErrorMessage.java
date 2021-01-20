package com.example.app_client.Model;

import com.google.gson.annotations.SerializedName;

public class ErrorMessage {
    @SerializedName("error")
    private String error;

    public ErrorMessage(String error) { this.error = error; }
    public String getError() { return error; }

}

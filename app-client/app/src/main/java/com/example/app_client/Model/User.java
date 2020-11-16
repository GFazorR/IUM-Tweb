package com.example.app_client.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("isAdmin")
    private boolean isAdmin;

    @SerializedName("token")
    @Expose
    private String token;

    public User(int id, String username, boolean isAdmin, String token) {
        this.id = id;
        this.username = username;
        this.isAdmin = isAdmin;
        this.token = token;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public boolean isAdmin() { return isAdmin; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}

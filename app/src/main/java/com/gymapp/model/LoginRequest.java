package com.gymapp.model;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("email")
    public String email;

    @SerializedName("pass")
    public String pass;

    public LoginRequest(String email, String pass) {
        this.email = email;
        this.pass = pass;
    }
}

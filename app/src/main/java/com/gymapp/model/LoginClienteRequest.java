package com.gymapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Cuerpo de la petición POST /api/auth/login-cliente
 */
public class LoginClienteRequest {

    @SerializedName("email")
    private String email;

    @SerializedName("pass")
    private String pass;

    public LoginClienteRequest(String email, String pass) {
        this.email = email;
        this.pass = pass;
    }

    public String getEmail() { return email; }
    public String getPass()  { return pass; }
}

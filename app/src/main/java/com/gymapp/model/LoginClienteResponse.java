package com.gymapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Respuesta de POST /api/auth/login-cliente
 */
public class LoginClienteResponse {

    @SerializedName("id_cliente")
    private int idCliente;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("email")
    private String email;

    @SerializedName("message")
    private String message;

    public int    getIdCliente() { return idCliente; }
    public String getNombre()    { return nombre; }
    public String getEmail()     { return email; }
    public String getMessage()   { return message; }
}

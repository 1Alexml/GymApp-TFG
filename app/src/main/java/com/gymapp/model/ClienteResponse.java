package com.gymapp.model;

import com.google.gson.annotations.SerializedName;

public class ClienteResponse {
    @SerializedName("id_cliente")
    public int idCliente;

    public String nombre;
    public String email;
    public String documento;
    public String telefono;

    @SerializedName("plan_activo")
    public String planActivo;
}

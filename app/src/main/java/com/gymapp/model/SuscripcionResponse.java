package com.gymapp.model;

import com.google.gson.annotations.SerializedName;

public class SuscripcionResponse {
    @SerializedName("id_suscripcion")
    public int idSuscripcion;

    @SerializedName("nombre_cliente")
    public String nombreCliente;

    @SerializedName("plan")
    public String plan;

    @SerializedName("fecha_inicio")
    public String fechaInicio;

    @SerializedName("fecha_fin")
    public String fechaFin;

    public String estado;
}

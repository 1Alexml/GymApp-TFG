package com.gymapp.model;


import com.google.gson.annotations.SerializedName;

/**
 * Respuesta de GET /api/cliente/{id}/suscripciones
 * Incluye el nombre del gimnasio (compañía) al que pertenece el plan.
 */
public class SuscripcionConGimnasio {

    @SerializedName("id")
    public int id;

    @SerializedName("plan")
    public String plan;

    @SerializedName("tipo")
    public String tipo;

    @SerializedName("precio")
    public double precio;

    @SerializedName("gimnasio")
    public String gimnasio;

    @SerializedName("fecha_inicio")
    public String fechaInicio;

    @SerializedName("fecha_fin")
    public String fechaFin;

    @SerializedName("accesos")
    public int accesos;

    @SerializedName("estado")
    public String estado;

    @SerializedName("dias_restantes")
    public int diasRestantes;
}
package com.gymapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Suscripción activa anidada dentro de PerfilResponse.
 */
public class SuscripcionActiva {

    @SerializedName("id_suscripcion")
    private int idSuscripcion;

    @SerializedName("plan_nombre")
    private String planNombre;

    @SerializedName("plan_tipo")
    private String planTipo;

    @SerializedName("precio")
    private double precio;

    @SerializedName("fecha_inicio")
    private String fechaInicio;

    @SerializedName("fecha_fin")
    private String fechaFin;

    @SerializedName("accesos_restantes")
    private int accesosRestantes;

    @SerializedName("estado")
    private String estado;

    public int    getIdSuscripcion()   { return idSuscripcion; }
    public String getPlanNombre()      { return planNombre; }
    public String getPlanTipo()        { return planTipo; }
    public double getPrecio()          { return precio; }
    public String getFechaInicio()     { return fechaInicio; }
    public String getFechaFin()        { return fechaFin; }
    public int    getAccesosRestantes(){ return accesosRestantes; }
    public String getEstado()          { return estado; }
}

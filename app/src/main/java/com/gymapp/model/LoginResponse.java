package com.gymapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Respuesta de POST /api/auth/login-cliente
 * Incluye perfil del cliente + datos de suscripción activa en una sola llamada.
 */
public class LoginResponse {

    @SerializedName("id_cliente")
    public int idCliente;

    @SerializedName("id_persona")
    public int idPersona;

    public String nombre;
    public String email;
    public String telefono;
    public String documento;
    public String message;

    @SerializedName("plan_activo")
    public String planActivo;

    @SerializedName("accesos_restantes")
    public int accesosRestantes;

    @SerializedName("fecha_fin")
    public String fechaFin;

    @SerializedName("dias_restantes")
    public int diasRestantes;

    @SerializedName("estado_suscripcion")
    public String estadoSuscripcion;
}

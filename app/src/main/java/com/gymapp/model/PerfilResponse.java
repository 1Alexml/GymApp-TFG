package com.gymapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Respuesta de GET /api/cliente/perfil/{id_cliente}
 */
public class PerfilResponse {

    @SerializedName("id_cliente")
    private int idCliente;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("email")
    private String email;

    @SerializedName("telefono")
    private String telefono;

    @SerializedName("documento")
    private String documento;

    @SerializedName("suscripcion_activa")
    private SuscripcionActiva suscripcionActiva;

    public int              getIdCliente()       { return idCliente; }
    public String           getNombre()          { return nombre; }
    public String           getEmail()           { return email; }
    public String           getTelefono()        { return telefono; }
    public String           getDocumento()       { return documento; }
    public SuscripcionActiva getSuscripcionActiva(){ return suscripcionActiva; }
}

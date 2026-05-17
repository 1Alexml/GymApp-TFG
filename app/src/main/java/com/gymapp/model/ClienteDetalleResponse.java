package com.gymapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Respuesta de GET /api/cliente/{id}
 * Perfil completo con historial de suscripciones.
 */
public class ClienteDetalleResponse {

    @SerializedName("id_cliente")
    public int idCliente;

    public String nombre;
    public String email;
    public String documento;
    public String telefono;

    public List<SuscripcionDetalle> suscripciones;

    public static class SuscripcionDetalle {
        public int id;
        public String plan;
        public double precio;

        @SerializedName("fecha_inicio")
        public String fechaInicio;

        @SerializedName("fecha_fin")
        public String fechaFin;

        public int accesos;
        public String estado;   // "activa" | "inactiva" | "vencida"

        @SerializedName("dias_restantes")
        public int diasRestantes;
    }
}

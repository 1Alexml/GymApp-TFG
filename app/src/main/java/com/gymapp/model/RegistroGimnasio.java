package com.gymapp.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Respuesta de GET /api/cliente/{id}/mis-gimnasios
 * Un objeto por cada compañía/gimnasio donde el cliente tiene historial.
 */
public class RegistroGimnasio {

    @SerializedName("id_cliente")
    public int idCliente;

    @SerializedName("id_compania")
    public int idCompania;

    @SerializedName("id_gym")
    public int idGym;

    @SerializedName("nombre_gimnasio")
    public String nombreGimnasio;

    @SerializedName("qr_data")
    public String qrData;          // DNI — mismo para todos los gyms

    @SerializedName("suscripcion_activa")
    public SuscripcionGim suscripcionActiva;   // null si no hay activa

    @SerializedName("ultimas_entradas")
    public List<EntradaItem> ultimasEntradas;

    public static class SuscripcionGim {
        public int    id;
        public String plan;
        public String tipo;
        public double precio;

        @SerializedName("fecha_inicio")
        public String fechaInicio;

        @SerializedName("fecha_fin")
        public String fechaFin;

        public int    accesos;
        public String estado;

        @SerializedName("dias_restantes")
        public int diasRestantes;
    }
}

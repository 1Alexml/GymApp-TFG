package com.gymapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Un registro del historial de entradas al gimnasio.
 * Respuesta de GET /api/cliente/{id}/entradas
 */
public class EntradaItem {

    @SerializedName("id_registro")
    private int idRegistro;

    @SerializedName("fecha_hora_entrada")
    private String fechaHoraEntrada;

    @SerializedName("fecha_hora_salida")
    private String fechaHoraSalida; // puede ser null

    public int    getIdRegistro()      { return idRegistro; }
    public String getFechaHoraEntrada(){ return fechaHoraEntrada; }
    public String getFechaHoraSalida() { return fechaHoraSalida; }
}

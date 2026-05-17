package com.gymapp.model;

import com.google.gson.annotations.SerializedName;

public class RegistroEntradaResponse {
    public String message;

    @SerializedName("id_registro")
    public int idRegistro;

    @SerializedName("fecha_hora_entrada")
    public String fechaHoraEntrada;
}

package com.gymapp.api;

import com.gymapp.model.LoginRequest;
import com.gymapp.model.LoginResponse;
import com.gymapp.model.EntradaItem;
import com.gymapp.model.PerfilResponse;
import com.gymapp.model.SuscripcionConGimnasio;
import com.gymapp.model.RegistroGimnasio;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Interfaz Retrofit que usa la app móvil.
 * Mapea los endpoints del backend ASP.NET que necesitan los fragments.
 *
 * Nota: BASE_URL ya termina en "/api/" (ver RetrofitClient), por lo que
 * las rutas aquí van SIN el prefijo "api/".
 */
public interface ApiService {

    /**
     * POST /api/auth/login-app
     * Login para la app móvil. Acepta clientes y trabajadores que tengan
     * un Cliente asociado a su Persona (es decir, dados de alta en el gym).
     * Devuelve perfil + suscripción activa en una sola llamada.
     */
    @POST("auth/login-app")
    Call<LoginResponse> loginApp(@Body LoginRequest request);

    /**
     * GET /api/cliente/perfil/{id}
     * Perfil del cliente con suscripción activa (objeto único).
     */
    @GET("cliente/perfil/{id}")
    Call<PerfilResponse> getPerfil(@Path("id") int idCliente);

    /**
     * GET /api/cliente/{id}/entradas
     * Historial de entradas al gimnasio del cliente (últimas 20).
     */
    @GET("cliente/{id}/entradas")
    Call<List<EntradaItem>> getEntradas(@Path("id") int idCliente);

    /**
     * GET /api/cliente/{id}/mis-gimnasios
     * Devuelve todos los gimnasios del DNI del cliente con suscripción y entradas por gym.
     */
    @GET("cliente/{id}/mis-gimnasios")
    Call<List<RegistroGimnasio>> getMisGimnasios(@Path("id") int idCliente);

    /**
     * GET /api/cliente/{id}/suscripciones
     * Devuelve TODAS las suscripciones del DNI del cliente, con nombre de gimnasio.
     */
    @GET("cliente/{id}/suscripciones")
    Call<List<SuscripcionConGimnasio>> getSuscripcionesPorDni(@Path("id") int idCliente);
}

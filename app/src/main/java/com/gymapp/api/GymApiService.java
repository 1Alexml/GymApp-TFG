package com.gymapp.api;

import com.gymapp.model.LoginRequest;
import com.gymapp.model.LoginResponse;
import com.gymapp.model.ClienteResponse;
import com.gymapp.model.ClienteDetalleResponse;
import com.gymapp.model.SuscripcionResponse;
import com.gymapp.model.ModificarClienteRequest;
import com.gymapp.model.MensajeResponse;
import com.gymapp.model.RegistroEntradaResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Mapea todos los endpoints del backend ASP.NET que usa la app cliente.
 */
public interface GymApiService {

    // ── AUTH ──────────────────────────────────────────────────────────────────

    /**
     * POST /api/auth/login-cliente
     * Login exclusivo para clientes (app Android).
     * Devuelve id_cliente + perfil + suscripción activa en una sola llamada.
     */
    @POST("api/auth/login-cliente")
    Call<LoginResponse> loginCliente(@Body LoginRequest request);

    // ── CLIENTE ───────────────────────────────────────────────────────────────

    /**
     * GET /api/cliente/{id}
     * Perfil completo con historial de suscripciones.
     * Lo usa SuscripcionActivity para mostrar las suscripciones del cliente.
     */
    @GET("api/cliente/{id}")
    Call<ClienteDetalleResponse> getClienteById(@Path("id") int idCliente);

    /**
     * GET /api/cliente/buscar?q=texto
     * Búsqueda por nombre / email / documento.
     */
    @GET("api/cliente/buscar")
    Call<List<ClienteResponse>> buscarCliente(@Query("q") String query);

    /**
     * PUT /api/cliente/{id}  →  actualizar datos del perfil
     */
    @PUT("api/cliente/{id}")
    Call<MensajeResponse> modificarCliente(
            @Path("id") int idCliente,
            @Body ModificarClienteRequest request
    );

    // ── SUSCRIPCIÓN ───────────────────────────────────────────────────────────

    /**
     * GET /api/suscripcion/recientes
     * Devuelve las últimas suscripciones (se filtra por cliente en la app).
     */
    @GET("api/suscripcion/recientes")
    Call<List<SuscripcionResponse>> getSuscripcionesRecientes();

    // ── REGISTRO ENTRADA ──────────────────────────────────────────────────────

    /**
     * POST /api/registroentrada/checkin
     * El cliente puede registrar su propia entrada buscando por nombre o DNI.
     */
    @POST("api/registroentrada/checkin")
    Call<RegistroEntradaResponse> checkIn(@Body CheckInRequest request);

    // Clase interna de request para el checkin
    class CheckInRequest {
        public String busqueda;
        public CheckInRequest(String busqueda) { this.busqueda = busqueda; }
    }
}
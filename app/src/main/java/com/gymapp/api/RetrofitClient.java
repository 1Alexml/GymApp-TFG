package com.gymapp.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton que provee la instancia de Retrofit configurada para conectar
 * con el backend ASP.NET Core corriendo en localhost:5034.
 *
 * EMULADOR Android → para acceder al localhost del PC host se usa 10.0.2.2
 * DISPOSITIVO FÍSICO → cambia BASE_URL por la IP local de tu PC (ej: http://192.168.1.X:5034/api/)
 */
public class RetrofitClient {

    // ── Cambia esta URL según el entorno ──────────────────────────────────────
    // Emulador Android Studio:
    private static final String BASE_URL = "https://webtfgback-production.up.railway.app/api/";
    // Dispositivo físico (reemplaza con la IP de tu PC en la red local):
    // private static final String BASE_URL = "http://192.168.1.100:5034/api/";
    // ─────────────────────────────────────────────────────────────────────────

    private static Retrofit retrofitInstance = null;

    public static Retrofit getInstance() {
        if (retrofitInstance == null) {
            // Interceptor para ver logs de red en Logcat (solo en debug)
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofitInstance;
    }

    public static ApiService getApiService() {
        return getInstance().create(ApiService.class);
    }
}

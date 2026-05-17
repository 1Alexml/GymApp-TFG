package com.gymapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gymapp.api.ApiService;
import com.gymapp.api.RetrofitClient;
import com.gymapp.databinding.ActivityLoginBinding;
import com.gymapp.model.LoginRequest;
import com.gymapp.model.LoginResponse;
import com.gymapp.utils.SessionManager;
import com.gymapp.utils.ThemeManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Pantalla de inicio de sesión.
 *
 * Llama a POST /api/auth/login-app con email + contraseña.
 *
 * El backend acepta a cualquier persona (cliente o trabajador) que tenga
 * un registro de Cliente asociado a su Persona — es decir, que esté dada
 * de alta en el gimnasio.
 *
 * En una sola llamada el backend devuelve perfil + suscripción activa,
 * por lo que los fragments ya tienen datos antes de hacer su primera petición.
 */
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        apiService     = RetrofitClient.getApiService();
        sessionManager = new SessionManager(this);

        // Aplicar color del tema al progress bar y al botón de login
        int colorTema = new ThemeManager(this).getColorPrimario();
        binding.progressBar.setIndeterminateTintList(
                android.content.res.ColorStateList.valueOf(colorTema));
        binding.btnLogin.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(colorTema));

        binding.btnLogin.setOnClickListener(v -> intentarLogin());
    }

    private void intentarLogin() {
        String email = binding.etEmail.getText().toString().trim();
        String pass  = binding.etPassword.getText().toString().trim();

        // Validación básica
        if (email.isEmpty()) {
            binding.etEmail.setError("Introduce tu email");
            binding.etEmail.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            binding.etPassword.setError("Introduce tu contraseña");
            binding.etPassword.requestFocus();
            return;
        }

        // Mostrar carga
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btnLogin.setEnabled(false);

        LoginRequest req = new LoginRequest(email, pass);

        apiService.loginApp(req).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call,
                                   Response<LoginResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnLogin.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse body = response.body();

                    // Guardar sesión completa (perfil + suscripción activa)
                    sessionManager.guardarSesion(body);

                    // Ir a pantalla principal
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {
                    String msg;
                    if (response.code() == 401) {
                        msg = "Email o contraseña incorrectos, o no estás dado de alta en el gimnasio.";
                    } else if (response.code() == 400) {
                        msg = "Faltan datos en la petición.";
                    } else {
                        msg = "Error al iniciar sesión (código " + response.code() + ")";
                    }
                    Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.btnLogin.setEnabled(true);
                Toast.makeText(LoginActivity.this,
                        "No se pudo conectar con el servidor.\nVerifica que el backend esté corriendo.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}

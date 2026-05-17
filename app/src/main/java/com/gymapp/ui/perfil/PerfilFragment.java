package com.gymapp.ui.perfil;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gymapp.LoginActivity;
import com.gymapp.R;
import com.gymapp.api.ApiService;
import com.gymapp.api.RetrofitClient;
import com.gymapp.databinding.FragmentPerfilBinding;
import com.gymapp.model.PerfilResponse;
import com.gymapp.utils.SessionManager;
import com.gymapp.utils.ThemeManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Muestra los datos personales del cliente y botón de cerrar sesión.
 */
public class PerfilFragment extends Fragment {

    private FragmentPerfilBinding binding;
    private ApiService apiService;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiService     = RetrofitClient.getApiService();
        sessionManager = new SessionManager(requireContext());

        // Aplicar color del tema a la rueda de carga
        binding.progressBar.setIndeterminateTintList(
                android.content.res.ColorStateList.valueOf(
                        new ThemeManager(requireContext()).getColorPrimario()));

        // Aplicar color del tema al avatar circular
        binding.tvAvatar.setBackgroundTintList(
                android.content.res.ColorStateList.valueOf(
                        new ThemeManager(requireContext()).getColorPrimario()));

        cargarPerfil();

        binding.btnCerrarSesion.setOnClickListener(v -> cerrarSesion());
    }

    private void cargarPerfil() {
        binding.progressBar.setVisibility(View.VISIBLE);

        apiService.getPerfil(sessionManager.getIdCliente()).enqueue(new Callback<PerfilResponse>() {
            @Override
            public void onResponse(@NonNull Call<PerfilResponse> call,
                                   @NonNull Response<PerfilResponse> response) {
                binding.progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    PerfilResponse p = response.body();
                    binding.tvNombreCompleto.setText(p.getNombre());
                    binding.tvEmail.setText(p.getEmail() != null ? p.getEmail() : "—");
                    binding.tvTelefono.setText(p.getTelefono() != null ? p.getTelefono() : "—");
                    binding.tvDocumento.setText(p.getDocumento());

                    // Inicial del nombre como avatar
                    if (p.getNombre() != null && !p.getNombre().isEmpty()) {
                        binding.tvAvatar.setText(String.valueOf(p.getNombre().charAt(0)).toUpperCase());
                    }
                } else {
                    Toast.makeText(requireContext(),
                            "Error al cargar perfil", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PerfilResponse> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(requireContext(),
                        "Sin conexión con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cerrarSesion() {
        sessionManager.logout();
        Intent intent = new Intent(requireContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

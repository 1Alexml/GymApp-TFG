package com.gymapp.ui.suscripcion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gymapp.api.ApiService;
import com.gymapp.api.RetrofitClient;
import com.gymapp.databinding.FragmentSuscripcionBinding;
import com.gymapp.model.SuscripcionConGimnasio;
import com.gymapp.utils.SessionManager;
import com.gymapp.utils.ThemeManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Muestra TODAS las suscripciones asociadas al DNI del cliente logueado,
 * diferenciadas por gimnasio.
 */
public class SuscripcionFragment extends Fragment {

    private FragmentSuscripcionBinding binding;
    private ApiService    apiService;
    private SessionManager sessionManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSuscripcionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        apiService     = RetrofitClient.getApiService();
        sessionManager = new SessionManager(requireContext());

        binding.progressBar.setIndeterminateTintList(
                android.content.res.ColorStateList.valueOf(
                        new ThemeManager(requireContext()).getColorPrimario()));

        // Configurar RecyclerView
        binding.recyclerSuscripciones.setLayoutManager(
                new LinearLayoutManager(requireContext()));

        cargarSuscripciones();
        binding.swipeRefresh.setOnRefreshListener(this::cargarSuscripciones);
    }

    private void cargarSuscripciones() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.layoutSinSus.setVisibility(View.GONE);
        binding.recyclerSuscripciones.setVisibility(View.GONE);

        int idCliente = sessionManager.getIdCliente();

        apiService.getSuscripcionesPorDni(idCliente)
                .enqueue(new Callback<List<SuscripcionConGimnasio>>() {

                    @Override
                    public void onResponse(@NonNull Call<List<SuscripcionConGimnasio>> call,
                                           @NonNull Response<List<SuscripcionConGimnasio>> response) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);

                        if (response.isSuccessful() && response.body() != null) {
                            List<SuscripcionConGimnasio> lista = response.body();
                            if (lista.isEmpty()) {
                                binding.layoutSinSus.setVisibility(View.VISIBLE);
                            } else {
                                binding.recyclerSuscripciones.setVisibility(View.VISIBLE);
                                binding.recyclerSuscripciones.setAdapter(
                                        new SuscripcionAdapter(requireContext(), lista));
                            }
                        } else {
                            Toast.makeText(requireContext(),
                                    "Error al cargar suscripciones", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<SuscripcionConGimnasio>> call,
                                          @NonNull Throwable t) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);
                        Toast.makeText(requireContext(),
                                "Sin conexión con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
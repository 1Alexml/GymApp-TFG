package com.gymapp.ui.historial;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gymapp.api.ApiService;
import com.gymapp.api.RetrofitClient;
import com.gymapp.databinding.FragmentHistorialBinding;
import com.gymapp.model.EntradaItem;
import com.gymapp.utils.SessionManager;
import com.gymapp.utils.ThemeManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Muestra el historial de entradas al gimnasio del cliente logueado.
 */
public class HistorialFragment extends Fragment {

    private FragmentHistorialBinding binding;
    private ApiService apiService;
    private SessionManager sessionManager;
    private EntradaAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistorialBinding.inflate(inflater, container, false);
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

        // Configurar RecyclerView
        adapter = new EntradaAdapter();
        binding.recyclerEntradas.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerEntradas.setAdapter(adapter);

        cargarHistorial();
        binding.swipeRefresh.setOnRefreshListener(this::cargarHistorial);
    }

    private void cargarHistorial() {
        binding.progressBar.setVisibility(View.VISIBLE);

        apiService.getEntradas(sessionManager.getIdCliente()).enqueue(new Callback<List<EntradaItem>>() {
            @Override
            public void onResponse(@NonNull Call<List<EntradaItem>> call,
                                   @NonNull Response<List<EntradaItem>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.swipeRefresh.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    List<EntradaItem> entradas = response.body();
                    if (entradas.isEmpty()) {
                        binding.tvSinEntradas.setVisibility(View.VISIBLE);
                        binding.recyclerEntradas.setVisibility(View.GONE);
                    } else {
                        binding.tvSinEntradas.setVisibility(View.GONE);
                        binding.recyclerEntradas.setVisibility(View.VISIBLE);
                        adapter.setEntradas(entradas);
                    }
                } else {
                    Toast.makeText(requireContext(),
                            "Error al cargar historial", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<EntradaItem>> call, @NonNull Throwable t) {
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

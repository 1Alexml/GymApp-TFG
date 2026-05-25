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

import com.google.android.material.tabs.TabLayout;
import com.gymapp.api.ApiService;
import com.gymapp.api.RetrofitClient;
import com.gymapp.databinding.FragmentHistorialBinding;
import com.gymapp.model.RegistroGimnasio;
import com.gymapp.utils.SessionManager;
import com.gymapp.utils.ThemeManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Historial de entradas por gimnasio.
 * Muestra tabs cuando el cliente está en más de un gimnasio.
 */
public class HistorialFragment extends Fragment {

    private FragmentHistorialBinding binding;
    private ApiService     apiService;
    private SessionManager sessionManager;
    private EntradaAdapter adapter;

    private List<RegistroGimnasio> gimnasios = new ArrayList<>();

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

        binding.progressBar.setIndeterminateTintList(
                android.content.res.ColorStateList.valueOf(
                        new ThemeManager(requireContext()).getColorPrimario()));

        adapter = new EntradaAdapter();
        binding.recyclerEntradas.setLayoutManager(
                new LinearLayoutManager(requireContext()));
        binding.recyclerEntradas.setAdapter(adapter);

        cargarMisGimnasios();
        binding.swipeRefresh.setOnRefreshListener(this::cargarMisGimnasios);
    }

    private void cargarMisGimnasios() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.tvSinEntradas.setVisibility(View.GONE);
        binding.recyclerEntradas.setVisibility(View.GONE);

        apiService.getMisGimnasios(sessionManager.getIdCliente())
                .enqueue(new Callback<List<RegistroGimnasio>>() {

                    @Override
                    public void onResponse(@NonNull Call<List<RegistroGimnasio>> call,
                                           @NonNull Response<List<RegistroGimnasio>> response) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);

                        if (response.isSuccessful() && response.body() != null
                                && !response.body().isEmpty()) {
                            gimnasios = response.body();
                            configurarTabs();
                            mostrarEntradasDeGimnasio(gimnasios.get(0));
                        } else {
                            binding.tabGimnasios.setVisibility(View.GONE);
                            mostrarSinEntradas();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<RegistroGimnasio>> call,
                                          @NonNull Throwable t) {
                        binding.progressBar.setVisibility(View.GONE);
                        binding.swipeRefresh.setRefreshing(false);
                        Toast.makeText(requireContext(),
                                "Sin conexión con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void configurarTabs() {
        binding.tabGimnasios.removeAllTabs();

        if (gimnasios.size() <= 1) {
            binding.tabGimnasios.setVisibility(View.GONE);
            return;
        }

        binding.tabGimnasios.setVisibility(View.VISIBLE);
        for (RegistroGimnasio gym : gimnasios) {
            binding.tabGimnasios.addTab(
                    binding.tabGimnasios.newTab().setText(gym.nombreGimnasio));
        }

        binding.tabGimnasios.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mostrarEntradasDeGimnasio(gimnasios.get(tab.getPosition()));
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void mostrarEntradasDeGimnasio(RegistroGimnasio gym) {
        if (gym.ultimasEntradas == null || gym.ultimasEntradas.isEmpty()) {
            mostrarSinEntradas();
        } else {
            binding.tvSinEntradas.setVisibility(View.GONE);
            binding.recyclerEntradas.setVisibility(View.VISIBLE);
            adapter.setEntradas(gym.ultimasEntradas);
        }
    }

    private void mostrarSinEntradas() {
        binding.tvSinEntradas.setVisibility(View.VISIBLE);
        binding.recyclerEntradas.setVisibility(View.GONE);
        adapter.setEntradas(new ArrayList<>());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

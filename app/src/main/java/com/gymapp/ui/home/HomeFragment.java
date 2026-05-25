package com.gymapp.ui.home;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.gymapp.api.ApiService;
import com.gymapp.api.RetrofitClient;
import com.gymapp.databinding.FragmentHomeBinding;
import com.gymapp.model.EntradaItem;
import com.gymapp.model.RegistroGimnasio;
import com.gymapp.utils.QRGenerator;
import com.gymapp.utils.SessionManager;
import com.gymapp.utils.ThemeManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragmento de inicio multi-gimnasio.
 * Muestra tabs por gimnasio con suscripción, QR y últimas visitas.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ApiService     apiService;
    private SessionManager sessionManager;

    private List<RegistroGimnasio> gimnasios = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
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

        binding.tvBienvenida.setText("¡Hola, " + sessionManager.getNombre() + "!");

        cargarMisGimnasios();
        binding.swipeRefresh.setOnRefreshListener(this::cargarMisGimnasios);
    }

    private void cargarMisGimnasios() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.cardSuscripcion.setVisibility(View.GONE);
        binding.cardSinSuscripcion.setVisibility(View.GONE);
        binding.cardEntradas.setVisibility(View.GONE);

        int idCliente = sessionManager.getIdCliente();

        apiService.getMisGimnasios(idCliente).enqueue(new Callback<List<RegistroGimnasio>>() {
            @Override
            public void onResponse(@NonNull Call<List<RegistroGimnasio>> call,
                                   @NonNull Response<List<RegistroGimnasio>> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.swipeRefresh.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null
                        && !response.body().isEmpty()) {
                    gimnasios = response.body();
                    configurarTabs();
                    mostrarGimnasio(gimnasios.get(0));
                } else {
                    // Sin gimnasios ni suscripciones
                    binding.tabGimnasios.setVisibility(View.GONE);
                    binding.cardSuscripcion.setVisibility(View.GONE);
                    binding.cardSinSuscripcion.setVisibility(View.VISIBLE);
                    binding.cardEntradas.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<RegistroGimnasio>> call, @NonNull Throwable t) {
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
                mostrarGimnasio(gimnasios.get(tab.getPosition()));
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void mostrarGimnasio(RegistroGimnasio gym) {
        int colorTema = new ThemeManager(requireContext()).getColorPrimario();

        // ── Suscripción ──────────────────────────────────────────────────────
        if (gym.suscripcionActiva != null) {
            RegistroGimnasio.SuscripcionGim sus = gym.suscripcionActiva;

            binding.cardSuscripcion.setVisibility(View.VISIBLE);
            binding.cardSinSuscripcion.setVisibility(View.GONE);

            binding.tvPlanNombre.setText(sus.plan);
            binding.tvPlanTipo.setText(sus.tipo);
            binding.tvAccesos.setText(String.valueOf(sus.accesos));
            binding.tvFechaFin.setText("Vence: " + sus.fechaFin);
            binding.tvEstado.setText(sus.estado.toUpperCase());

            binding.tvAccesos.setTextColor(colorTema);

            int colorBadge = sus.estado.equals("activa")
                    ? colorTema
                    : requireContext().getColor(com.gymapp.R.color.rojo_inactivo);
            binding.tvEstado.setTextColor(colorBadge);

            GradientDrawable bgBadge = new GradientDrawable();
            bgBadge.setShape(GradientDrawable.RECTANGLE);
            bgBadge.setCornerRadius(60f);
            bgBadge.setColor(Color.argb(26, Color.red(colorBadge),
                    Color.green(colorBadge), Color.blue(colorBadge)));
            bgBadge.setStroke(2, colorBadge);
            binding.tvEstado.setBackground(bgBadge);

            // ── QR (codifica el DNI — funciona en todos los gyms) ───────────
            if (gym.qrData != null && !gym.qrData.isEmpty()) {
                Bitmap qr = QRGenerator.generar(gym.qrData, 600);
                if (qr != null) {
                    binding.ivQr.setImageBitmap(qr);
                    binding.ivQr.setVisibility(View.VISIBLE);
                } else {
                    binding.ivQr.setVisibility(View.GONE);
                }
            } else {
                binding.ivQr.setVisibility(View.GONE);
            }

        } else {
            binding.cardSuscripcion.setVisibility(View.GONE);
            binding.cardSinSuscripcion.setVisibility(View.VISIBLE);
        }

        // ── Últimas visitas ──────────────────────────────────────────────────
        if (gym.ultimasEntradas != null && !gym.ultimasEntradas.isEmpty()) {
            binding.cardEntradas.setVisibility(View.VISIBLE);
            binding.layoutEntradasContenido.removeAllViews();

            // Mostrar máximo 3 entradas en el Home
            int max = Math.min(3, gym.ultimasEntradas.size());
            for (int i = 0; i < max; i++) {
                EntradaItem e = gym.ultimasEntradas.get(i);
                TextView tv = new TextView(requireContext());
                tv.setText("• " + e.getFechaHoraEntrada());
                tv.setTextColor(requireContext().getColor(
                        com.gymapp.R.color.blanco));     // usar color existente
                tv.setTextSize(14f);

                // Pequeño margen inferior entre filas
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.bottomMargin = 6;
                tv.setLayoutParams(lp);

                binding.layoutEntradasContenido.addView(tv);
            }
        } else {
            binding.cardEntradas.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

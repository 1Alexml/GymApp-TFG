package com.gymapp.ui.suscripcion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gymapp.api.ApiService;
import com.gymapp.api.RetrofitClient;
import com.gymapp.databinding.FragmentSuscripcionBinding;
import com.gymapp.model.PerfilResponse;
import com.gymapp.model.SuscripcionActiva;
import com.gymapp.utils.SessionManager;
import com.gymapp.utils.ThemeManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Muestra el detalle completo de la suscripción activa del cliente:
 * plan, precio, fechas y accesos restantes.
 */
public class SuscripcionFragment extends Fragment {

    private FragmentSuscripcionBinding binding;
    private ApiService apiService;
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

        // Aplicar color del tema a la rueda de carga
        binding.progressBar.setIndeterminateTintList(
                android.content.res.ColorStateList.valueOf(
                        new ThemeManager(requireContext()).getColorPrimario()));

        cargarSuscripcion();
        binding.swipeRefresh.setOnRefreshListener(this::cargarSuscripcion);
    }

    private void cargarSuscripcion() {
        binding.progressBar.setVisibility(View.VISIBLE);

        apiService.getPerfil(sessionManager.getIdCliente()).enqueue(new Callback<PerfilResponse>() {
            @Override
            public void onResponse(@NonNull Call<PerfilResponse> call,
                                   @NonNull Response<PerfilResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.swipeRefresh.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    mostrarSuscripcion(response.body().getSuscripcionActiva());
                } else {
                    Toast.makeText(requireContext(),
                            "Error al cargar suscripción", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PerfilResponse> call, @NonNull Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                binding.swipeRefresh.setRefreshing(false);
                Toast.makeText(requireContext(),
                        "Sin conexión con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarSuscripcion(SuscripcionActiva sus) {
        if (sus == null) {
            binding.layoutSinSus.setVisibility(View.VISIBLE);
            binding.layoutConSus.setVisibility(View.GONE);
            return;
        }

        binding.layoutSinSus.setVisibility(View.GONE);
        binding.layoutConSus.setVisibility(View.VISIBLE);

        binding.tvNombrePlan.setText(sus.getPlanNombre());
        binding.tvTipoPlan.setText("Tipo: " + sus.getPlanTipo());
        binding.tvPrecio.setText(String.format("%.2f €/mes", sus.getPrecio()));
        binding.tvFechaInicio.setText("Inicio: " + sus.getFechaInicio());
        binding.tvFechaFin.setText("Vencimiento: " + sus.getFechaFin());
        binding.tvAccesosRestantes.setText(String.valueOf(sus.getAccesosRestantes()));
        binding.tvEstadoSus.setText(sus.getEstado().toUpperCase());

        // Indicador de accesos (barra de progreso visual)
        // Suponemos un máximo típico de 30 accesos — ajusta si necesitas
        int maxAccesos = 30;
        int pct = Math.min(100, (sus.getAccesosRestantes() * 100) / maxAccesos);
        binding.progressAccesos.setProgress(pct);

        // Color del tema → precio, accesos restantes y barra de progreso
        int colorTema = new ThemeManager(requireContext()).getColorPrimario();
        binding.tvPrecio.setTextColor(colorTema);
        binding.tvAccesosRestantes.setTextColor(colorTema);
        binding.progressAccesos.setProgressTintList(
                android.content.res.ColorStateList.valueOf(colorTema));

        // Badge de estado: ACTIVA → color del tema, INACTIVA → rojo
        int colorBadge = sus.getEstado().equals("activa")
                ? colorTema
                : requireContext().getColor(com.gymapp.R.color.rojo_inactivo);
        binding.tvEstadoSus.setTextColor(colorBadge);

        android.graphics.drawable.GradientDrawable bgBadge = new android.graphics.drawable.GradientDrawable();
        bgBadge.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
        bgBadge.setCornerRadius(60f);
        bgBadge.setColor(android.graphics.Color.argb(26,
                android.graphics.Color.red(colorBadge),
                android.graphics.Color.green(colorBadge),
                android.graphics.Color.blue(colorBadge)));
        bgBadge.setStroke(2, colorBadge);
        binding.tvEstadoSus.setBackground(bgBadge);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

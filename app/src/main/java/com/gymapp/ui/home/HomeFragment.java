package com.gymapp.ui.home;

import android.graphics.Bitmap;
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
import com.gymapp.databinding.FragmentHomeBinding;
import com.gymapp.model.PerfilResponse;
import com.gymapp.model.SuscripcionActiva;
import com.gymapp.utils.QRGenerator;
import com.gymapp.utils.SessionManager;
import com.gymapp.utils.ThemeManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Fragmento de inicio: muestra bienvenida, estado de suscripción
 * y accesos restantes del cliente.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ApiService apiService;
    private SessionManager sessionManager;

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

        // Aplicar color del tema a la rueda de carga
        binding.progressBar.setIndeterminateTintList(
                android.content.res.ColorStateList.valueOf(
                        new ThemeManager(requireContext()).getColorPrimario()));

        // Saludo personalizado
        binding.tvBienvenida.setText("¡Hola, " + sessionManager.getNombre() + "!");

        cargarPerfil();

        binding.swipeRefresh.setOnRefreshListener(this::cargarPerfil);
    }

    private void cargarPerfil() {
        binding.progressBar.setVisibility(View.VISIBLE);

        int idCliente = sessionManager.getIdCliente();

        apiService.getPerfil(idCliente).enqueue(new Callback<PerfilResponse>() {
            @Override
            public void onResponse(@NonNull Call<PerfilResponse> call,
                                   @NonNull Response<PerfilResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                binding.swipeRefresh.setRefreshing(false);

                if (response.isSuccessful() && response.body() != null) {
                    mostrarDatos(response.body());
                } else {
                    Toast.makeText(requireContext(),
                            "Error al cargar datos", Toast.LENGTH_SHORT).show();
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

    private void mostrarDatos(PerfilResponse perfil) {
        SuscripcionActiva sus = perfil.getSuscripcionActiva();

        if (sus != null) {
            // Card de suscripción activa
            binding.cardSuscripcion.setVisibility(View.VISIBLE);
            binding.cardSinSuscripcion.setVisibility(View.GONE);

            binding.tvPlanNombre.setText(sus.getPlanNombre());
            binding.tvPlanTipo.setText(sus.getPlanTipo());
            binding.tvAccesos.setText(String.valueOf(sus.getAccesosRestantes()));
            binding.tvFechaFin.setText("Vence: " + sus.getFechaFin());
            binding.tvEstado.setText(sus.getEstado().toUpperCase());

            // Color primario del tema → número grande de accesos
            int colorTema = new ThemeManager(requireContext()).getColorPrimario();
            binding.tvAccesos.setTextColor(colorTema);

            // Badge de estado: ACTIVA → color del tema, INACTIVA → rojo
            int colorBadge = sus.getEstado().equals("activa")
                    ? colorTema
                    : requireContext().getColor(com.gymapp.R.color.rojo_inactivo);
            binding.tvEstado.setTextColor(colorBadge);

            android.graphics.drawable.GradientDrawable bgBadge = new android.graphics.drawable.GradientDrawable();
            bgBadge.setShape(android.graphics.drawable.GradientDrawable.RECTANGLE);
            bgBadge.setCornerRadius(60f);
            bgBadge.setColor(android.graphics.Color.argb(26,
                    android.graphics.Color.red(colorBadge),
                    android.graphics.Color.green(colorBadge),
                    android.graphics.Color.blue(colorBadge)));
            bgBadge.setStroke(2, colorBadge);
            binding.tvEstado.setBackground(bgBadge);

            // Generar QR con el documento del cliente (lo que el backend usa para checkin)
            String documento = sessionManager.getDocumento();
            if (documento != null && !documento.isEmpty()) {
                Bitmap qr = QRGenerator.generar(documento, 600);
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
            // Sin suscripción activa
            binding.cardSuscripcion.setVisibility(View.GONE);
            binding.cardSinSuscripcion.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

package com.gymapp.ui.suscripcion;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gymapp.R;
import com.gymapp.model.SuscripcionConGimnasio;
import com.gymapp.utils.ThemeManager;

import java.util.List;

public class SuscripcionAdapter extends RecyclerView.Adapter<SuscripcionAdapter.ViewHolder> {

    private final List<SuscripcionConGimnasio> lista;
    private final int colorTema;
    private final Context context;

    public SuscripcionAdapter(Context context, List<SuscripcionConGimnasio> lista) {
        this.context   = context;
        this.lista     = lista;
        this.colorTema = new ThemeManager(context).getColorPrimario();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_suscripcion, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int position) {
        SuscripcionConGimnasio s = lista.get(position);

        h.tvGimnasio.setText(s.gimnasio);
        h.tvNombrePlan.setText(s.plan);
        h.tvTipoPlan.setText("Tipo: " + s.tipo);
        h.tvPrecio.setText(String.format("%.2f €/mes", s.precio));
        h.tvFechaInicio.setText("Inicio: " + s.fechaInicio);
        h.tvFechaFin.setText("Vencimiento: " + s.fechaFin);
        h.tvAccesos.setText(String.valueOf(s.accesos));
        h.tvEstado.setText(s.estado.toUpperCase());

        int maxAccesos = 30;
        int pct = Math.min(100, (s.accesos * 100) / maxAccesos);
        h.progressAccesos.setProgress(pct);

        // Colores según estado
        int colorEstado = s.estado.equals("activa")
                ? colorTema
                : context.getColor(R.color.rojo_inactivo);

        h.tvPrecio.setTextColor(colorTema);
        h.tvAccesos.setTextColor(colorTema);
        h.progressAccesos.setProgressTintList(ColorStateList.valueOf(colorTema));
        h.tvEstado.setTextColor(colorEstado);

        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setCornerRadius(60f);
        bg.setColor(Color.argb(26, Color.red(colorEstado),
                Color.green(colorEstado), Color.blue(colorEstado)));
        bg.setStroke(2, colorEstado);
        h.tvEstado.setBackground(bg);
    }

    @Override
    public int getItemCount() { return lista.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView    tvGimnasio, tvNombrePlan, tvTipoPlan, tvPrecio,
                tvFechaInicio, tvFechaFin, tvAccesos, tvEstado;
        ProgressBar progressAccesos;

        ViewHolder(View v) {
            super(v);
            tvGimnasio    = v.findViewById(R.id.tv_item_gimnasio);
            tvNombrePlan  = v.findViewById(R.id.tv_item_nombre_plan);
            tvTipoPlan    = v.findViewById(R.id.tv_item_tipo_plan);
            tvPrecio      = v.findViewById(R.id.tv_item_precio);
            tvFechaInicio = v.findViewById(R.id.tv_item_fecha_inicio);
            tvFechaFin    = v.findViewById(R.id.tv_item_fecha_fin);
            tvAccesos     = v.findViewById(R.id.tv_item_accesos);
            tvEstado      = v.findViewById(R.id.tv_item_estado);
            progressAccesos = v.findViewById(R.id.progress_item_accesos);
        }
    }
}
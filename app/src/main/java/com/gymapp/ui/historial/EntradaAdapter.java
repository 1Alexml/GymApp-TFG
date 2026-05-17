package com.gymapp.ui.historial;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gymapp.R;
import com.gymapp.model.EntradaItem;
import com.gymapp.utils.ThemeManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Adaptador para el RecyclerView del historial de entradas.
 */
public class EntradaAdapter extends RecyclerView.Adapter<EntradaAdapter.EntradaViewHolder> {

    private List<EntradaItem> entradas = new ArrayList<>();

    public void setEntradas(List<EntradaItem> lista) {
        this.entradas = lista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EntradaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_entrada, parent, false);
        return new EntradaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull EntradaViewHolder holder, int position) {
        EntradaItem item = entradas.get(position);
        holder.tvEntrada.setText("Entrada: " + item.getFechaHoraEntrada());
        String salida = item.getFechaHoraSalida() != null
                ? "Salida: " + item.getFechaHoraSalida()
                : "Salida: —";
        holder.tvSalida.setText(salida);
        holder.tvNumero.setText(String.valueOf(position + 1));

        // Aplicar color del tema al número y al círculo de fondo
        int colorTema = new ThemeManager(holder.itemView.getContext()).getColorPrimario();
        holder.tvNumero.setTextColor(colorTema);

        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.OVAL);
        bg.setColor(Color.argb(26, Color.red(colorTema), Color.green(colorTema), Color.blue(colorTema))); // ~10% opacity
        bg.setStroke(2, colorTema);
        holder.tvNumero.setBackground(bg);
    }

    @Override
    public int getItemCount() { return entradas.size(); }

    static class EntradaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumero, tvEntrada, tvSalida;

        EntradaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumero  = itemView.findViewById(R.id.tv_numero);
            tvEntrada = itemView.findViewById(R.id.tv_entrada);
            tvSalida  = itemView.findViewById(R.id.tv_salida);
        }
    }
}

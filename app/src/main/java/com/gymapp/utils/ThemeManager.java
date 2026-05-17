package com.gymapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

import com.gymapp.R;

/**
 * Gestiona la elección del tema (color primario) del usuario.
 * Persiste la elección en SharedPreferences y expone el color para
 * aplicarlo programáticamente en distintas vistas.
 *
 * Sincronizado con los temas de la web:
 *   1 = Naranja  (#FF5C00)  ← por defecto
 *   2 = Azul     (#00B4D8)
 *   3 = Morado   (#A855F7)
 */
public class ThemeManager {

    private static final String PREFS  = "gym_theme_prefs";
    private static final String KEY_ID = "tema_id";

    public static final int TEMA_NARANJA = 1;
    public static final int TEMA_AZUL    = 2;
    public static final int TEMA_MORADO  = 3;

    private final SharedPreferences prefs;
    private final Context ctx;

    public ThemeManager(Context context) {
        this.ctx   = context.getApplicationContext();
        this.prefs = ctx.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    /** Devuelve el id del tema activo (default = 1 = Naranja, igual que la web) */
    public int getTemaId() {
        return prefs.getInt(KEY_ID, TEMA_NARANJA);
    }

    public void setTemaId(int id) {
        prefs.edit().putInt(KEY_ID, id).apply();
    }

    /** Devuelve el color primario en formato int (Color.parseColor) */
    public int getColorPrimario() {
        switch (getTemaId()) {
            case TEMA_AZUL:    return ctx.getColor(R.color.tema_azul);
            case TEMA_MORADO:  return ctx.getColor(R.color.tema_morado);
            case TEMA_NARANJA:
            default:           return ctx.getColor(R.color.tema_naranja);
        }
    }

    public String getNombreTema() {
        switch (getTemaId()) {
            case TEMA_AZUL:    return "Azul";
            case TEMA_MORADO:  return "Morado";
            case TEMA_NARANJA:
            default:           return "Naranja";
        }
    }
}

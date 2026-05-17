package com.gymapp.ui.ajustes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import com.gymapp.utils.ThemeManager;

/**
 * Diálogo "Cambiar apariencia" — permite elegir entre los 3 temas
 * (Naranja, Azul, Morado), igual que la web.
 *
 * Tras elegir, recarga la activity para que el cambio sea inmediato.
 */
public class AparienciaDialog {

    public static void show(Activity activity) {
        ThemeManager tm = new ThemeManager(activity);

        String[] opciones = { "Naranja", "Azul", "Morado" };
        int seleccionado  = tm.getTemaId() - 1;  // 1→0, 2→1, 3→2

        new androidx.appcompat.app.AlertDialog.Builder(activity)
                .setTitle("Cambiar apariencia")
                .setSingleChoiceItems(opciones, seleccionado, (dialog, which) -> {
                    int nuevoId = which + 1;
                    tm.setTemaId(nuevoId);
                    dialog.dismiss();
                    activity.recreate();   // refresca toda la UI con el tema nuevo
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}

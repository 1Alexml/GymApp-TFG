package com.gymapp.ui.ajustes;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog.Builder;

/**
 * Diálogo "Acerca de" — info de la app (mismo contenido que la web).
 */
public class AcercaDeDialog {

    public static void show(Context ctx) {
        String html =
                "<br/>" +
                "<b><big>ADR GYM SOFTWARE</big></b><br/>" +
                "<small>Versión 1.0.0</small><br/><br/>" +
                "App cliente para gestión de suscripciones y accesos al gimnasio. " +
                "Desarrollado como proyecto TFG.<br/><br/>" +
                "<small>© 2026 ADR GYM SOFTWARE. Todos los derechos reservados.</small>";

        new androidx.appcompat.app.AlertDialog.Builder(ctx)
                .setTitle("Acerca de")
                .setMessage(Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY))
                .setPositiveButton("Cerrar", null)
                .show();



    }
}

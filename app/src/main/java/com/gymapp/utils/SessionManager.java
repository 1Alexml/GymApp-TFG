package com.gymapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.gymapp.model.LoginResponse;

/**
 * Gestiona la sesión del cliente en SharedPreferences.
 * Almacena todos los datos del login para no tener que volver a llamar a la API.
 */
public class SessionManager {

    private static final String PREF_NAME = "GymAppSession";

    private static final String KEY_LOGUEADO          = "logueado";
    private static final String KEY_ID_CLIENTE        = "id_cliente";
    private static final String KEY_ID_PERSONA        = "id_persona";
    private static final String KEY_NOMBRE            = "nombre";
    private static final String KEY_EMAIL             = "email";
    private static final String KEY_TELEFONO          = "telefono";
    private static final String KEY_DOCUMENTO         = "documento";
    private static final String KEY_PLAN_ACTIVO       = "plan_activo";
    private static final String KEY_ACCESOS           = "accesos_restantes";
    private static final String KEY_FECHA_FIN         = "fecha_fin";
    private static final String KEY_DIAS_RESTANTES    = "dias_restantes";
    private static final String KEY_ESTADO_SUS        = "estado_suscripcion";

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs  = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    /** Guarda todos los datos devueltos por /api/auth/login-cliente */
    public void guardarSesion(LoginResponse r) {
        editor.putBoolean(KEY_LOGUEADO,       true)
              .putInt    (KEY_ID_CLIENTE,      r.idCliente)
              .putInt    (KEY_ID_PERSONA,      r.idPersona)
              .putString (KEY_NOMBRE,          r.nombre    != null ? r.nombre    : "")
              .putString (KEY_EMAIL,           r.email     != null ? r.email     : "")
              .putString (KEY_TELEFONO,        r.telefono  != null ? r.telefono  : "")
              .putString (KEY_DOCUMENTO,       r.documento != null ? r.documento : "")
              .putString (KEY_PLAN_ACTIVO,     r.planActivo != null ? r.planActivo : "")
              .putInt    (KEY_ACCESOS,         r.accesosRestantes)
              .putString (KEY_FECHA_FIN,       r.fechaFin  != null ? r.fechaFin  : "")
              .putInt    (KEY_DIAS_RESTANTES,  r.diasRestantes)
              .putString (KEY_ESTADO_SUS,      r.estadoSuscripcion != null ? r.estadoSuscripcion : "sin_suscripcion")
              .apply();
    }

    /** Actualiza en local los campos de suscripción (tras un checkin, por ejemplo) */
    public void actualizarAccesos(int nuevosAccesos) {
        editor.putInt(KEY_ACCESOS, nuevosAccesos).apply();
    }

    public boolean estaLogueado()       { return prefs.getBoolean(KEY_LOGUEADO,       false); }
    public boolean isLoggedIn()         { return estaLogueado(); }
    public int     getIdCliente()       { return prefs.getInt    (KEY_ID_CLIENTE,      -1);    }
    public int     getIdPersona()       { return prefs.getInt    (KEY_ID_PERSONA,      -1);    }
    public String  getNombre()          { return prefs.getString (KEY_NOMBRE,          "");    }
    public String  getEmail()           { return prefs.getString (KEY_EMAIL,           "");    }
    public String  getTelefono()        { return prefs.getString (KEY_TELEFONO,        "");    }
    public String  getDocumento()       { return prefs.getString (KEY_DOCUMENTO,       "");    }
    public String  getPlanActivo()      { return prefs.getString (KEY_PLAN_ACTIVO,     "");    }
    public int     getAccesos()         { return prefs.getInt    (KEY_ACCESOS,         0);     }
    public String  getFechaFin()        { return prefs.getString (KEY_FECHA_FIN,       "");    }
    public int     getDiasRestantes()   { return prefs.getInt    (KEY_DIAS_RESTANTES,  0);     }
    public String  getEstadoSus()       { return prefs.getString (KEY_ESTADO_SUS,      "");    }

    /** Guarda sesión simplificada (desde LoginClienteResponse) */
    public void saveSession(int idCliente, String nombre, String email) {
        editor.putBoolean(KEY_LOGUEADO,  true)
              .putInt    (KEY_ID_CLIENTE, idCliente)
              .putString (KEY_NOMBRE,     nombre != null ? nombre : "")
              .putString (KEY_EMAIL,      email  != null ? email  : "")
              .apply();
    }

    public void cerrarSesion() {
        editor.clear().apply();
    }

    /** Alias para cerrarSesion (usado en PerfilFragment) */
    public void logout() {
        cerrarSesion();
    }
}
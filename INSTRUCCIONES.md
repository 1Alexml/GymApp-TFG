# GymClientApp — Instrucciones de configuración

## 1. CAMBIOS EN EL BACKEND (ASP.NET Core)

Reemplaza los dos archivos del backend con los ficheros de la carpeta `Backend_Nuevos_Endpoints/`:

| Fichero a reemplazar | Nuevo fichero |
|---|---|
| `Controllers/AuthController.cs` | `AuthController_MODIFICADO.cs` |
| `Controllers/api/ClienteController.cs` | `ClienteController_MODIFICADO.cs` |

### Nuevos endpoints añadidos:
- `POST /api/auth/login-cliente` — Login exclusivo para clientes (app Android)
- `GET  /api/cliente/perfil/{id_cliente}` — Perfil completo + suscripción activa
- `GET  /api/cliente/{id_cliente}/entradas` — Historial de entradas (últimas 20)

---

## 2. IMPORTAR EL PROYECTO EN ANDROID STUDIO

1. Abre **Android Studio** → `File → Open`
2. Selecciona la carpeta `GymClientApp/`
3. Espera a que Gradle sincronice (descarga dependencias automáticamente)

---

## 3. URL DEL BACKEND

El archivo clave es:
```
app/src/main/java/com/gymapp/api/RetrofitClient.java
```

### Emulador de Android Studio (por defecto):
```java
private static final String BASE_URL = "http://10.0.2.2:5034/api/";
```
> `10.0.2.2` es la dirección que el emulador usa para acceder al localhost del PC.

### Dispositivo físico (Android real):
Cambia la URL a la IP local de tu PC en tu red WiFi:
```java
private static final String BASE_URL = "http://192.168.1.XXX:5034/api/";
```
Encuentra tu IP con `ipconfig` en Windows → busca "Adaptador de LAN inalámbrica".

---

## 4. ORDEN DE ARRANQUE

1. Arranca **MySQL Workbench** (base de datos `gym_db`)
2. Arranca el **backend** en Visual Studio (`http://localhost:5034`)
3. Arranca la **app Android** en el emulador o dispositivo

---

## 5. CREDENCIALES DE PRUEBA

Usa el email y contraseña de cualquier cliente registrado en la web.
Los clientes los gestiona el panel web (trabajadores). La app solo es para clientes.

---

## 6. ESTRUCTURA DEL PROYECTO

```
GymClientApp/
├── app/src/main/
│   ├── AndroidManifest.xml
│   ├── java/com/gymapp/
│   │   ├── api/
│   │   │   ├── RetrofitClient.java   ← Configuración URL backend
│   │   │   └── ApiService.java       ← Definición de endpoints
│   │   ├── model/                    ← POJOs (mapean JSON de la API)
│   │   ├── utils/
│   │   │   └── SessionManager.java   ← Gestión de sesión (SharedPreferences)
│   │   ├── SplashActivity.java       ← Pantalla de inicio
│   │   ├── LoginActivity.java        ← Login del cliente
│   │   ├── MainActivity.java         ← Actividad principal + nav
│   │   └── ui/
│   │       ├── home/HomeFragment.java          ← Inicio (estado suscripción)
│   │       ├── suscripcion/SuscripcionFragment ← Detalle del plan
│   │       ├── historial/HistorialFragment     ← Entradas al gimnasio
│   │       └── perfil/PerfilFragment           ← Datos personales
│   └── res/
│       ├── layout/     ← Diseños XML de cada pantalla
│       ├── navigation/ ← Grafo de navegación (nav_graph.xml)
│       ├── menu/       ← Menú bottom navigation
│       ├── drawable/   ← Íconos e ilustraciones
│       └── values/     ← Colores, strings, temas
└── app/build.gradle    ← Dependencias (Retrofit, Material, etc.)
```

---

## 7. DEPENDENCIAS USADAS

| Librería | Uso |
|---|---|
| Retrofit 2 | Peticiones HTTP a la API REST |
| Gson | Conversión JSON ↔ objetos Java |
| OkHttp Logging | Logs de red en Logcat |
| Material Components | Diseño de UI |
| Navigation Component | Navegación entre fragments |
| SwipeRefreshLayout | Pull-to-refresh en las pantallas |

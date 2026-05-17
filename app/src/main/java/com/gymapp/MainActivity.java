package com.gymapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.gymapp.databinding.ActivityMainBinding;
import com.gymapp.ui.ajustes.AcercaDeDialog;
import com.gymapp.ui.ajustes.AparienciaDialog;
import com.gymapp.utils.SessionManager;
import com.gymapp.utils.ThemeManager;

/**
 * Actividad principal que aloja el Bottom Navigation Bar
 * y los tres fragmentos: Inicio, Suscripción e Historial.
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private SessionManager sessionManager;
    private ThemeManager   themeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sessionManager = new SessionManager(this);
        themeManager   = new ThemeManager(this);
        setSupportActionBar(binding.toolbar);

        // Configurar Navigation Component con Bottom Nav
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            AppBarConfiguration appBarConfig = new AppBarConfiguration.Builder(
                    R.id.homeFragment,
                    R.id.suscripcionFragment,
                    R.id.historialFragment,
                    R.id.perfilFragment
            ).build();

            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
            NavigationUI.setupWithNavController(binding.bottomNavigation, navController);
        }

        // Aplicar color del tema al BottomNavigation (item seleccionado)
        aplicarTemaBottomNav();
    }

    /** Tinta el icono y el texto del item seleccionado del bottom nav con el color del tema. */
    private void aplicarTemaBottomNav() {
        int colorPrim   = themeManager.getColorPrimario();
        int colorUnsel  = getColor(R.color.nav_unselected);

        int[][] states  = { new int[]{ android.R.attr.state_checked },
                            new int[]{ -android.R.attr.state_checked } };
        int[]   colors  = { colorPrim, colorUnsel };
        ColorStateList csl = new ColorStateList(states, colors);

        binding.bottomNavigation.setItemIconTintList(csl);
        binding.bottomNavigation.setItemTextColor(csl);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_ajustes) {
            mostrarMenuAjustes(item);
            return true;
        }

        if (id == R.id.action_logout) {
            sessionManager.logout();
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Menú emergente con las opciones "Cambiar apariencia" y "Acerca de". */
    private void mostrarMenuAjustes(MenuItem anchorItem) {
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.action_ajustes));
        popup.getMenu().add(0, 1, 0, "Cambiar apariencia");
        popup.getMenu().add(0, 2, 1, "Acerca de");

        popup.setOnMenuItemClickListener(it -> {
            if (it.getItemId() == 1) {
                AparienciaDialog.show(this);
            } else if (it.getItemId() == 2) {
                AcercaDeDialog.show(this);
            }
            return true;
        });

        popup.show();
    }
}

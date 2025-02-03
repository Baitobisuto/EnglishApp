package com.example.englishapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.englishapp.Vocabulario.Vocabulario1;
import com.example.englishapp.Vocabulario.Vocabulario2;
import com.example.englishapp.Vocabulario.Vocabulario3;
import com.example.englishapp.Vocabulario.Vocabulario4;
import com.example.englishapp.Vocabulario.Vocabulario5;
import com.example.englishapp.Vocabulario.Vocabulario6;

public class Niveles extends AppCompatActivity {

    private ImageButton bt1, bt2, bt3, bt4, bt5, bt6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_niveles);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inicializarVariables();
        actualizarNiveles();
    }

    public void inicializarVariables() {
        bt1 = findViewById(R.id.bt_uno);
        bt2 = findViewById(R.id.bt_dos);
        bt3 = findViewById(R.id.bt_tres);
        bt4 = findViewById(R.id.bt_cuatro);
        bt5 = findViewById(R.id.bt_cinco);
        bt6 = findViewById(R.id.bt_seis);
    }

    public void actualizarNiveles() {
        // Aquí controlas el desbloqueo de los niveles según el progreso.
        int nivelCompletado = obtenerNivelCompletado();  // Este método obtiene el nivel completado

        // Nivel 1: siempre desbloqueado
        bt1.setEnabled(true);  // Nivel 1 siempre está habilitado
        bt1.clearColorFilter();  // Quitar cualquier filtro de color (si existe)

        // Nivel 2: desbloqueado si nivel 1 está completado
        if (nivelCompletado >= 1) {
            bt2.setEnabled(true);
            bt2.clearColorFilter();  // Quitar filtro de color
        } else {
            bt2.setEnabled(false);
            bt2.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);  // Aplicar filtro gris
        }

        // Nivel 3: desbloqueado si nivel 2 está completado
        if (nivelCompletado >= 2) {
            bt3.setEnabled(true);
            bt3.clearColorFilter();
        } else {
            bt3.setEnabled(false);
            bt3.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        }

        // Nivel 4: desbloqueado si nivel 3 está completado
        if (nivelCompletado >= 3) {
            bt4.setEnabled(true);
            bt4.clearColorFilter();
        } else {
            bt4.setEnabled(false);
            bt4.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        }

        // Nivel 5: desbloqueado si nivel 4 está completado
        if (nivelCompletado >= 4) {
            bt5.setEnabled(true);
            bt5.clearColorFilter();
        } else {
            bt5.setEnabled(false);
            bt5.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        }

        // Nivel 6: desbloqueado si nivel 5 está completado
        if (nivelCompletado >= 5) {
            bt6.setEnabled(true);
            bt6.clearColorFilter();
        } else {
            bt6.setEnabled(false);
            bt6.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        }
    }

    public int obtenerNivelCompletado() {
        // Este es un ejemplo, puedes usar SharedPreferences para obtener el último nivel completado.
        return getSharedPreferences("Juego", MODE_PRIVATE).getInt("NivelCompletado", 0);
    }

    // Métodos para cambiar de nivel
    public void cambiarNivelUno(View view) {
        Intent intent = new Intent(Niveles.this, Vocabulario1.class);
        startActivity(intent);
    }

    public void cambiarNivelDos(View view) {
        Intent intent = new Intent(Niveles.this, Vocabulario2.class);
        startActivity(intent);
    }

    public void cambiarNivelTres(View view) {
        Intent intent = new Intent(Niveles.this, Vocabulario3.class);
        startActivity(intent);
    }

    public void cambiarNivelCuatro(View view) {
        Intent intent = new Intent(Niveles.this, Vocabulario4.class);
        startActivity(intent);
    }

    public void cambiarNivelCinco(View view) {
        Intent intent = new Intent(Niveles.this, Vocabulario5.class);
        startActivity(intent);
    }

    public void cambiarNivelSeis(View view) {
        Intent intent = new Intent(Niveles.this, Vocabulario6.class);
        startActivity(intent);
    }

    public void back(View view) {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }
}
package com.example.englishapp.Vocabulario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.englishapp.Menu;
import com.example.englishapp.Niveles;
import com.example.englishapp.R;

public class Vocabulario1 extends AppCompatActivity {
    private int puntos;
    private int vidas;
    private int indiceFruta = 0;
    private EditText etNombreFruta;
    private TextView tvPuntos;
    private ImageView imagen;
    private ImageButton bt_exit, bt_ok;
    private ImageView[] iconosVidas;

    private int[] imagenes = {R.drawable.fresa, R.drawable.aguacate, R.drawable.manzana, R.drawable.naranja};
    private String[] respuestasCorrectas = {"fresa", "aguacate", "manzana", "naranja"};
    private SharedPreferences sharedPreferences; // necesario para heredar vidas y puntos en todos los juegos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vocabulario1);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializaVariables();
    }
    public void inicializaVariables() {
        etNombreFruta = findViewById(R.id.et_NombreFruta);
        tvPuntos = findViewById(R.id.tv_puntos);
        imagen = findViewById(R.id.img_fruta);
        bt_exit = findViewById(R.id.bt_back);
        bt_ok = findViewById(R.id.bt_ok);
        iconosVidas = new ImageView[]{
                findViewById(R.id.imageButton7),
                findViewById(R.id.imageButton8),
                findViewById(R.id.imageButton9)
        };

        // Recuperar los puntos y las vidas desde SharedPreferences
        sharedPreferences = getSharedPreferences("Juego", MODE_PRIVATE);
        puntos = sharedPreferences.getInt("Puntos", 0);  // Si no existe, se inicializa en 0
        vidas = sharedPreferences.getInt("Vidas", 3);    // Si no existe, se inicializa en 3

        // Establecer la primera fruta
        imagen.setImageResource(imagenes[indiceFruta]);

        // Actualizar puntos y vidas
        actualizarPuntos();
        actualizarVidas();
    }

    public void verificarRespuesta(View view) {
        String respuesta = etNombreFruta.getText().toString().trim().toLowerCase();
        String respuestaCorrecta = respuestasCorrectas[indiceFruta];

        if (respuesta.equals(respuestaCorrecta)) {
            puntos += 3;  // Solo sumar puntos si la respuesta es correcta
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
            indiceFruta++;

            if (indiceFruta < imagenes.length) {
                imagen.setImageResource(imagenes[indiceFruta]);
            } else {
                Toast.makeText(this, "¡Juego terminado! Vamos al siguiente nivel!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Niveles.class));
                finish();  // Finalizar la actividad actual
            }
        } else {
            vidas--;  // Solo restar vidas, no puntos
            Toast.makeText(this, "Incorrecto. La respuesta correcta era " + respuestaCorrecta, Toast.LENGTH_SHORT).show();
            actualizarVidas();
            if (vidas == 0) {
                Toast.makeText(this, "¡Juego terminado! Has perdido.", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        }
        // Guardar los puntos y las vidas después de cada intento
        guardarEstadoJuego();
        actualizarPuntos();
        etNombreFruta.setText("");  // Limpiar campo de texto
    }

    private void actualizarPuntos() {
        tvPuntos.setText("Puntos: " + puntos);
    }

    private void actualizarVidas() {
        for (int i = 0; i < 3; i++) {
            if (i < vidas) {
                iconosVidas[i].setVisibility(View.VISIBLE);
            } else {
                iconosVidas[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    // Método para guardar puntos y vidas en SharedPreferences
    private void guardarEstadoJuego() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Puntos", puntos);  // Guardar los puntos
        editor.putInt("Vidas", vidas);    // Guardar las vidas
        editor.apply();  // Aplicar los cambios
    }

    public void botonBack(View view) {
        startActivity(new Intent(this, Menu.class));
    }
}
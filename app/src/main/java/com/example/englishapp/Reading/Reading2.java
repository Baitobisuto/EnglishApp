package com.example.englishapp.Reading;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.englishapp.Menu;
import com.example.englishapp.R;

public class Reading2 extends AppCompatActivity {

    private TextView tvUsuario, tvPuntos, tvPregunta;
    private ImageView ivImagen1, ivImagen2, ivImagen3, ivImagen4;
    private int puntos = 0;
    private int vidasRestantes = 3;
    private String usuario;
    private SharedPreferences sharedPreferences;
    private int[] imagenesGato = {
            R.drawable.gato1,  // Reemplaza con tus recursos de imagen reales
            R.drawable.gato2,
            R.drawable.gato3,
            R.drawable.gato4
    };

    private String[] preguntasGato = {
            "What is the cat doing?",
            "What is the cat doing?",
            "What is the cat doing?",
            "What is the cat doing?"
    };

    private int respuestaCorrecta = 0; // Índice de la imagen correcta (0-3)
    private int preguntaActual = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reading2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sharedPreferences = getSharedPreferences("Juego", MODE_PRIVATE);
        puntos = sharedPreferences.getInt("Puntos", 0);

        inicializarVariables();
        cargarPregunta();
    }

    private void inicializarVariables() {
        tvUsuario = findViewById(R.id.tv_usuario);
        tvPuntos = findViewById(R.id.tv_puntos);
        tvPregunta = findViewById(R.id.tv_pregunta);
        ivImagen1 = findViewById(R.id.iv_imagen1);
        ivImagen2 = findViewById(R.id.iv_imagen2);
        ivImagen3 = findViewById(R.id.iv_imagen3);
        ivImagen4 = findViewById(R.id.iv_imagen4);

        usuario = getIntent().getStringExtra("username");
        if (usuario != null) {
            tvUsuario.setText(usuario);
        }

        tvPuntos.setText(String.valueOf(puntos));

        ivImagen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarRespuesta(0);
            }
        });

        ivImagen2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarRespuesta(1);
            }
        });

        ivImagen3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarRespuesta(2);
            }
        });

        ivImagen4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarRespuesta(3);
            }
        });

        findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botonAtras();
            }
        });
    }

    private void cargarPregunta() {
        if (preguntaActual < preguntasGato.length) {
            tvPregunta.setText(preguntasGato[preguntaActual]);
            ivImagen1.setImageResource(imagenesGato[0]);
            ivImagen2.setImageResource(imagenesGato[1]);
            ivImagen3.setImageResource(imagenesGato[2]);
            ivImagen4.setImageResource(imagenesGato[3]);

            // Establece la respuesta correcta según la pregunta actual. Esto asume
            // que el índice de la imagen correcta coincide con el índice de la pregunta.
            // Cambia esto si tus datos están estructurados de manera diferente.
            respuestaCorrecta = preguntaActual;
        } else {
            Toast.makeText(this, "Juego terminado. Puntuación final: " + puntos, Toast.LENGTH_SHORT).show();
            // Agrega código para reiniciar el juego o cambiar de actividad.
        }
    }

    private void verificarRespuesta(int respuestaUsuario) {
        if (respuestaUsuario == respuestaCorrecta) {
            puntos += 3;
            tvPuntos.setText(String.valueOf(puntos));
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
            preguntaActual++;
            cargarPregunta();
        } else {
            vidasRestantes--;
            if (vidasRestantes >= 0) {
                // El acceso al array vidas no está presente en este código. Tendrás que agregarlo.
                Toast.makeText(this, "Incorrecto. Te quedan " + (vidasRestantes + 1) + " vidas.", Toast.LENGTH_SHORT).show();
                if (vidasRestantes == 0) {
                    Toast.makeText(this, "¡Game Over!", Toast.LENGTH_SHORT).show();
                    finish(); // O reinicia el juego.
                }
            }
        }
    }

    public void botonAtras() {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
        finish();
    }
}

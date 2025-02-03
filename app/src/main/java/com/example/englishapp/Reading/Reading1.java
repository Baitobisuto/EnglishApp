package com.example.englishapp.Reading;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.englishapp.Menu;
import com.example.englishapp.R;

public class Reading1 extends AppCompatActivity {
    private TextView tvUsuario, tvPuntos, tvFrase, tvPregunta;
    private EditText etRespuesta;
    private ImageButton[] vidas;
    private ImageButton btBack, btOk;
    private int puntos = 0;
    private int vidasRestantes = 3;
    private String usuario;
    private SharedPreferences sharedPreferences;

    private String[] frases = {
            "The cat sat on the mat.",
            "Birds fly in the sky.",
            "The sun is shining brightly.",
            "She reads a book every week.",
            "They are playing football now."
    };

    private String[] preguntas = {
            "Where did the cat sit?",
            "Where do birds fly?",
            "What is shining brightly?",
            "What does she do every week?",
            "What are they doing now?"
    };

    private String[] respuestas = {
            "mat",
            "sky",
            "sun",
            "read a book", // Variación de la respuesta para "reads"
            "playing football" // Variación de la respuesta para "are playing football"
    };

    private int fraseActual = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reading1);
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
        tvFrase = findViewById(R.id.tv_frase);
        tvPregunta = findViewById(R.id.tv_pregunta);
        etRespuesta = findViewById(R.id.et_respuesta);
        vidas = new ImageButton[]{
                findViewById(R.id.imageButton7),
                findViewById(R.id.imageButton8),
                findViewById(R.id.imageButton9)
        };
        btBack = findViewById(R.id.bt_back);
        btOk = findViewById(R.id.bt_ok);

        usuario = getIntent().getStringExtra("username");
        if (usuario != null) {
            tvUsuario.setText(usuario);
        }

        tvPuntos.setText(String.valueOf(puntos));

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botonBack();
            }
        });

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarRespuesta();
            }
        });

        for (ImageButton vida : vidas) {
            vida.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

    private void cargarPregunta() {
        if (fraseActual < frases.length) {
            tvFrase.setText(frases[fraseActual]);
            tvPregunta.setText(preguntas[fraseActual]);
            etRespuesta.setText("");
        } else {
            Toast.makeText(this, "Juego terminado. Puntuación final: " + puntos, Toast.LENGTH_SHORT).show();
        }
    }

    private void verificarRespuesta() {
        String respuestaUsuario = etRespuesta.getText().toString().trim().toLowerCase();
        String respuestaCorrecta = respuestas[fraseActual].toLowerCase();

        if (respuestaUsuario.contains(respuestaCorrecta) || respuestaCorrecta.contains(respuestaUsuario)) { // Contiene o es igual
            puntos += 3;
            tvPuntos.setText(String.valueOf(puntos));
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
            fraseActual++;
            cargarPregunta();
        } else {
            vidasRestantes--;
            if (vidasRestantes >= 0) {
                vidas[vidasRestantes].setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Incorrecto. Te quedan " + (vidasRestantes + 1) + " vidas.", Toast.LENGTH_SHORT).show();
                if (vidasRestantes == 0) {
                    Toast.makeText(this, "¡Game Over!", Toast.LENGTH_SHORT).show();
                    // Aquí puedes poner código para reiniciar el juego, guardar la puntuación, etc.
                    finish();
                }
            }
        }
    }

    public void botonBack() {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
        finish();
    }
}
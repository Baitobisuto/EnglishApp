package com.example.englishapp.Gramatica;

import android.content.Intent;
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

import com.example.englishapp.ConexionMethods;
import com.example.englishapp.Menu;
import com.example.englishapp.R;

public class Gramatica1 extends AppCompatActivity {

    private TextView tvUsuario, tvPuntos, tvFrase;
    private EditText etRespuesta;
    private ImageButton[] vidas;
    private ImageButton btBack, btOk;
    private ConexionMethods conexion;
    private int puntos = 0;
    private int vidasRestantes = 3;
    private String usuario;
    private String[] frases = {
            "I ____ to the store yesterday.", // went
            "She ____ a book every week.", // reads
            "They ____ playing football now.", // are
            "He ____ dinner last night.", // ate
            "We ____ to Paris next month."  // are going
    };
    private String[] respuestas = {
            "went",
            "reads",
            "are",
            "ate",
            "are going"
    };
    private int fraseActual = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gramatica1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarVariables();
        conexion = new ConexionMethods();
        cargarFrase();
    }

    private void inicializarVariables() {
        tvUsuario = findViewById(R.id.tv_usuario);
        tvPuntos = findViewById(R.id.tv_puntos);
        tvFrase = findViewById(R.id.tv_frase);
        etRespuesta = findViewById(R.id.et_respuesta);
        vidas = new ImageButton[]{
                findViewById(R.id.imageButton7),
                findViewById(R.id.imageButton8),
                findViewById(R.id.imageButton9)
        };
        btBack = findViewById(R.id.bt_back);
        btOk = findViewById(R.id.bt_ok);

        // Obtener el nombre de usuario (puedes pasarlo desde la actividad anterior)
        usuario = getIntent().getStringExtra("username"); // Reemplazar "username" con la clave correcta
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

    private void cargarFrase() {
        if (fraseActual < frases.length) {
            tvFrase.setText(frases[fraseActual]);
            etRespuesta.setText(""); // Limpia el EditText
        } else {
            Toast.makeText(this, "Juego terminado. Puntuación final: " + puntos, Toast.LENGTH_SHORT).show();
        }
    }

    private void verificarRespuesta() {
        String respuestaUsuario = etRespuesta.getText().toString().trim().toLowerCase();
        String respuestaCorrecta = respuestas[fraseActual].toLowerCase();

        if (respuestaUsuario.equals(respuestaCorrecta)) {
            puntos++;
            tvPuntos.setText(String.valueOf(puntos));
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
            fraseActual++;
            cargarFrase();
        } else {
            vidasRestantes--;
            if (vidasRestantes >= 0) {
                vidas[vidasRestantes].setVisibility(View.INVISIBLE);
                Toast.makeText(this, "Incorrecto. Te quedan " + (vidasRestantes + 1) + " vidas.", Toast.LENGTH_SHORT).show();
                if (vidasRestantes == 0) {
                    Toast.makeText(this, "¡Game Over!", Toast.LENGTH_SHORT).show();
                    // Puedes mostrar un diálogo, reiniciar el juego, etc.
                    finish(); // Cierra la actividad actual
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
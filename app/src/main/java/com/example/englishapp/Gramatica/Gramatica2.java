package com.example.englishapp.Gramatica;

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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Gramatica2 extends AppCompatActivity {

    private TextView tvUsuario, tvPuntos, tvFrase;
    private EditText etRespuesta;
    private ImageButton[] vidas;
    private ImageButton btBack, btOk;
    private int puntos = 0;
    private int vidasRestantes = 3;
    private String usuario;
    private SharedPreferences sharedPreferences;

    private String[] frases = {
            "I went to the store yesterday.",
            "She reads a book every week.",
            "They are playing football now.",
            "He ate dinner last night.",
            "We are going to Paris next month."
    };
    private int fraseActual = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gramatica2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences("Juego", MODE_PRIVATE);
        puntos = sharedPreferences.getInt("Puntos", 0);

        inicializarVariables();
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

        usuario = getIntent().getStringExtra("username");
        if (usuario != null) {
            tvUsuario.setText(usuario);
        }

        tvPuntos.setText(String.valueOf(puntos));

        btBack.setOnClickListener(v -> botonBack());
        btOk.setOnClickListener(v -> verificarRespuesta());

        for (ImageButton vida : vidas) {
            vida.setOnClickListener(v -> {
                // Si necesitas alguna acción al tocar las vidas, va aquí.
            });
        }
    }

    private void cargarFrase() {
        if (fraseActual < frases.length) {
            String fraseDesordenada = desordenarFrase(frases[fraseActual]);
            tvFrase.setText(fraseDesordenada);
            etRespuesta.setText("");
        } else {
            Toast.makeText(this, "Juego terminado. Puntuación final: " + puntos, Toast.LENGTH_SHORT).show();
        }
    }

    private String desordenarFrase(String frase) {
        List<String> palabras = Arrays.asList(frase.split(" "));
        Collections.shuffle(palabras);
        StringBuilder sb = new StringBuilder();
        for (String palabra : palabras) {
            sb.append(palabra).append(" ");
        }
        return sb.toString().trim();
    }


    private void verificarRespuesta() {
        String respuestaUsuario = etRespuesta.getText().toString().trim();
        String respuestaCorrecta = frases[fraseActual];

        if (respuestaUsuario.equals(respuestaCorrecta)) {
            puntos += 3;
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

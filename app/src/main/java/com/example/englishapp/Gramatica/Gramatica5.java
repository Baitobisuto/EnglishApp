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

import java.util.HashMap;
import java.util.Map;

public class Gramatica5 extends AppCompatActivity {

    private TextView tvUsuario, tvPuntos, tvFrase;
    private EditText etRespuesta;
    private ImageButton[] vidas;
    private ImageButton btBack, btOk;
    private int puntos = 0;
    private int vidasRestantes = 3;
    private String usuario;
    private SharedPreferences sharedPreferences;

    private Map<String, String> preguntas = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gramatica5);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sharedPreferences = getSharedPreferences("Juego", MODE_PRIVATE);
        puntos = sharedPreferences.getInt("Puntos", 0);

        inicializarVariables();
        cargarPreguntas();
        mostrarPregunta();
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

    private void cargarPreguntas() {
        // Aquí puedes añadir más preguntas y sus transformaciones
        preguntas.put("Is he happy?", "He is happy.");
        preguntas.put("Are you a student?", "I am a student.");
        preguntas.put("Does she like cats?", "She likes cats.");
        preguntas.put("Did they go to the party?", "They went to the party.");
        preguntas.put("Will you travel next year?", "I will travel next year.");
    }

    private void mostrarPregunta() {
        String[] preguntasArray = preguntas.keySet().toArray(new String[0]);
        int indice = (int) (Math.random() * preguntasArray.length);
        String pregunta = preguntasArray[indice];

        tvFrase.setText(pregunta);
        etRespuesta.setText("");
    }

    private void verificarRespuesta() {
        String respuestaUsuario = etRespuesta.getText().toString().trim();
        String pregunta = tvFrase.getText().toString();
        String respuestaCorrecta = preguntas.get(pregunta);

        if (respuestaUsuario.equalsIgnoreCase(respuestaCorrecta)) {
            puntos += 3;
            tvPuntos.setText(String.valueOf(puntos));
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
            mostrarPregunta();
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

package com.example.englishapp.Vocabulario;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.englishapp.R;

public class Vocabulario6 extends AppCompatActivity {

    private TextView textViewPregunta, textViewPuntos;
    private ImageButton button1, button2, button3, buttonExit;
    private ImageButton vida1, vida2, vida3;
    private int puntos;
    private int vidas = 3;
    private int respuestaCorrectaIndice; // Índice de la fruta correcta
    private SharedPreferences sharedPreferences;

    private String[] frutas = {"Manzana", "Fresa", "Plátano"};
    private int[] imagenes = {R.drawable.manzana, R.drawable.fresa, R.drawable.platano};
    private int[] vidasImagenes = {R.drawable.vida, R.drawable.vida, R.drawable.vida}; // imágenes para re


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vocabulario6);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sharedPreferences = getSharedPreferences("Juego", MODE_PRIVATE);
        puntos = sharedPreferences.getInt("Puntos", 0);  // Recuperar puntos previos

        inicializarVariables();
        cargarPregunta();
    }


    private void inicializarVariables() {
        textViewPregunta = findViewById(R.id.textView);
        textViewPuntos = findViewById(R.id.textView8);
        button1 = findViewById(R.id.imageButton28);  // Manzana
        button2 = findViewById(R.id.imageButton29);  // Fresa
        button3 = findViewById(R.id.imageButton27);  // Plátano
        buttonExit = findViewById(R.id.bt_exit7);

        // Botones de vidas
        vida1 = findViewById(R.id.imageButton20);
        vida2 = findViewById(R.id.imageButton25);
        vida3 = findViewById(R.id.imageButton26);
    }

    private void cargarPregunta() {
        // Si ya no hay vidas, finalizar el juego
        if (vidas <= 0) {
            Toast.makeText(this, "¡Juego Terminado! Puntos finales: " + puntos, Toast.LENGTH_LONG).show();
            return;
        }

        respuestaCorrectaIndice = (int) (Math.random() * frutas.length);

        // Mostrar el nombre de la fruta
        textViewPregunta.setText(frutas[respuestaCorrectaIndice]);

        // Actualizar las imágenes de las opciones
        button1.setImageResource(imagenes[0]);  // Manzana
        button2.setImageResource(imagenes[1]);  // Fresa
        button3.setImageResource(imagenes[2]);  // Plátano

        // Mostrar los puntos actualizados
        textViewPuntos.setText("Puntos: " + puntos);

        // Actualizar las imágenes de vidas
        actualizarVidas();
    }

    private void actualizarVidas() {
        // Mostrar las vidas restantes en la interfaz
        switch (vidas) {
            case 3:
                vida1.setVisibility(View.VISIBLE);
                vida2.setVisibility(View.VISIBLE);
                vida3.setVisibility(View.VISIBLE);
                break;
            case 2:
                vida1.setVisibility(View.VISIBLE);
                vida2.setVisibility(View.VISIBLE);
                vida3.setVisibility(View.INVISIBLE);
                break;
            case 1:
                vida1.setVisibility(View.VISIBLE);
                vida2.setVisibility(View.INVISIBLE);
                vida3.setVisibility(View.INVISIBLE);
                break;
            case 0:
                vida1.setVisibility(View.INVISIBLE);
                vida2.setVisibility(View.INVISIBLE);
                vida3.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public void comprobarRespuesta(View view) {
        int respuestaUsuario = -1;

        // Verificar qué botón fue presionado (usando el parámetro view)
        if (view == button1) {
            respuestaUsuario = 0;
        } else if (view == button2) {
            respuestaUsuario = 1;
        } else if (view == button3) {
            respuestaUsuario = 2;
        }

        // Verificar si la respuesta es correcta
        if (respuestaUsuario == respuestaCorrectaIndice) {
            puntos += 3;  // Sumar puntos si es correcta
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
        } else {
            puntos -= 3;  // Restar puntos si es incorrecta
            vidas--;  // Restar una vida
            Toast.makeText(this, "¡Incorrecto!", Toast.LENGTH_SHORT).show();
        }

        // Actualizar puntos y vidas
        textViewPuntos.setText("Puntos: " + puntos);

        // Actualizar las SharedPreferences con los nuevos puntos
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Puntos", puntos);  // Guardar puntos
        editor.apply();

        // Actualizar las vidas visibles en la interfaz
        actualizarVidas();

        // Si ya no hay vidas, finalizar el juego
        if (vidas <= 0) {
            Toast.makeText(this, "Juego terminado, no tienes más vidas.", Toast.LENGTH_LONG).show();
        } else {
            cargarPregunta();  // Cargar la siguiente pregunta
        }
    }

    // Método para manejar el botón de salida
    public void botonExit(View view) {
        finish();  // Finaliza la actividad actual
    }
}
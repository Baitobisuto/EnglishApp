package com.example.englishapp.Vocabulario;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishapp.R;

public class Vocabulario5 extends AppCompatActivity {

    private ImageView imagenEstancia;
    private TextView nombreEstancia;
    private TextView tvPuntos; // Declaramos el TextView para mostrar los puntos
    private int puntos = 0;  // Inicializamos los puntos en 10
    private int vidas = 3;
    private String respuestaCorrecta;
    private int indiceEstanciaActual = 0;

    private String[] estancias = {"Kitchen", "Dining Room", "Double bedroom", "Bedroom", "Bathroom"};
    private int[] imagenesConBordeVerde = {
            R.drawable.kitchenverde, R.drawable.diningverde, R.drawable.room1verde, R.drawable.bathroomverde, R.drawable.room2verde};
    private int[] imagenesConBordeRojo = {
            R.drawable.kitchenrojo, R.drawable.diningrojo, R.drawable.room1rojo, R.drawable.room2rojo, R.drawable.bathroomrojo};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulario5);

        imagenEstancia = findViewById(R.id.imageViewNumero);
        nombreEstancia = findViewById(R.id.textView5);
        tvPuntos = findViewById(R.id.tvPuntos);

        generarPregunta();
    }

    public void generarPregunta() {
        respuestaCorrecta = estancias[indiceEstanciaActual];
        nombreEstancia.setText(respuestaCorrecta);
        imagenEstancia.setImageResource(R.drawable.casa);
    }

    public void verificarRespuesta(View view) {
        int id = view.getId();
        int opcionSeleccionada = -1;

        if (id == R.id.botonKitchen) {
            opcionSeleccionada = 0; // Kitchen
        } else if (id == R.id.botonDiningRoom) {
            opcionSeleccionada = 1; // Dining Room
        } else if (id == R.id.botonroom1) {
            opcionSeleccionada = 2; // Bedroom2
        } else if (id == R.id.botonBathroom) {
            opcionSeleccionada = 3; // Bathroom
        } else if (id == R.id.botonRoom2) {
            opcionSeleccionada = 4; // Bedroom
        }

        if (respuestaCorrecta.equalsIgnoreCase(estancias[opcionSeleccionada])) {
            puntos += 3;  // Sumar puntos
            imagenEstancia.setImageResource(imagenesConBordeVerde[opcionSeleccionada]); // Mostrar la imagen con borde verde
            Toast.makeText(this, "¡Correcto! Puntos: " + puntos, Toast.LENGTH_SHORT).show();
        } else {
            puntos -= 3;  // Restar puntos
            vidas--;  // Restar una vida
            imagenEstancia.setImageResource(imagenesConBordeRojo[opcionSeleccionada]); // Mostrar la imagen con borde rojo
            Toast.makeText(this, "¡Incorrecto! Puntos: " + puntos + " Vidas restantes: " + vidas, Toast.LENGTH_SHORT).show();
        }

        // Actualizamos el TextView con los puntos actuales
        tvPuntos.setText("Puntos: " + puntos);

        // Verificar si se ha terminado el juego
        if (vidas <= 0) {
            Toast.makeText(this, "¡Juego terminado! Puntos finales: " + puntos, Toast.LENGTH_SHORT).show();
            finish();  // Finalizar la actividad
        } else {
            // Avanzar a la siguiente estancia
            indiceEstanciaActual++;
            if (indiceEstanciaActual >= estancias.length) {
                indiceEstanciaActual = 0; // Si se acaba el juego, reiniciar
            }
            generarPregunta(); // Generar nueva pregunta
        }
    }
}
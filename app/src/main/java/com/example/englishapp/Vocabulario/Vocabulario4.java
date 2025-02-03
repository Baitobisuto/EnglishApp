package com.example.englishapp.Vocabulario;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import com.example.englishapp.R;

public class Vocabulario4 extends AppCompatActivity {

    private int puntos = 10;  // Puntos iniciales
    private int vidas = 3;  // Vidas iniciales
    private int numeroCorrecto = 1;  // El número actual que debe adivinar el jugador
    private EditText etRespuesta;
    private TextView tvPuntos;  // TextView donde se mostrarán los puntos
    private ImageButton[] vidasBotones;  // Arreglo de botones de vida
    private ImageView imagenNumero;  // Imagen que representa el número a adivinar
    private Button botonComprobar;
    private String[] numerosEnIngles = {"one", "two", "three", "four", "five"};  // Los números en inglés


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vocabulario4);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inicializarVariables();
        configurarBotonComprobar();
        actualizarPuntos();
    }

    public void inicializarVariables() {
        etRespuesta = findViewById(R.id.etRespuesta);
        tvPuntos = findViewById(R.id.tvPuntos);
        imagenNumero = findViewById(R.id.imageViewNumero);
        botonComprobar = findViewById(R.id.id_boton);

        // Inicializamos los botones de vida
        vidasBotones = new ImageButton[]{
                findViewById(R.id.imageButton14),
                findViewById(R.id.imageButton15),
                findViewById(R.id.imageButton16)
        };

        // Establecemos la imagen del primer número (uno)
        imagenNumero.setImageResource(R.drawable.uno);
    }

    // Método para configurar el botón de comprobar
    public void configurarBotonComprobar() {
        botonComprobar.setOnClickListener(view -> comprobarRespuesta());
    }

    // Método que comprueba si la respuesta es correcta
    public void comprobarRespuesta() {
        String respuesta = etRespuesta.getText().toString().trim().toLowerCase();  // Convertir la respuesta a minúsculas

        // Verificar si la respuesta es correcta
        if (respuesta.equals(numerosEnIngles[numeroCorrecto - 1])) {
            puntos += 3;  // Si la respuesta es correcta, sumar puntos
            Toast.makeText(this, "¡Correcto! Puntos: " + puntos, Toast.LENGTH_SHORT).show();

            // Incrementar el número y actualizar la imagen
            numeroCorrecto++;

            // Cambiar la imagen para el próximo número
            if (numeroCorrecto <= numerosEnIngles.length) {
                cambiarImagenNumero();
            } else {
                // Si ya se han mostrado todos los números, terminar el juego
                Toast.makeText(this, "¡Juego terminado! Puntos finales: " + puntos, Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            // Si la respuesta es incorrecta
            if (puntos > 3) {
                puntos -= 3;  // Restar 3 puntos si no estamos en 0
            } else {
                puntos = 0;  // Si los puntos están en 0 o menos, dejarlos en 0
            }

            vidas--;  // Restar una vida
            Toast.makeText(this, "Incorrecto. Puntos: " + puntos, Toast.LENGTH_SHORT).show();

            // Ocultar un ícono de vida
            if (vidas >= 0) {
                vidasBotones[vidas].setVisibility(View.INVISIBLE);
            }

            // Si el jugador se queda sin vidas, terminar el juego
            if (vidas == 0) {
                Toast.makeText(this, "¡Juego terminado! Puntos finales: " + puntos, Toast.LENGTH_SHORT).show();
                finish();  // Terminar la actividad
            }
        }

        // Limpiar el campo de texto
        etRespuesta.setText("");
        actualizarPuntos();  // Actualizar la visualización de los puntos
    }

    // Método para cambiar la imagen del número
    private void cambiarImagenNumero() {
        switch (numeroCorrecto) {
            case 2:
                imagenNumero.setImageResource(R.drawable.dos);  // Cambiar imagen a "dos"
                break;
            case 3:
                imagenNumero.setImageResource(R.drawable.tres);  // Cambiar imagen a "tres"
                break;
            case 4:
                imagenNumero.setImageResource(R.drawable.siete);  // Cambiar imagen a "cuatro"
                break;
            case 5:
                imagenNumero.setImageResource(R.drawable.uno);  // Cambiar imagen a "cinco"
                break;
            default:
                break;
        }
    }

    // Método para actualizar el TextView de puntos
    private void actualizarPuntos() {
        tvPuntos.setText("Puntos: " + puntos);  // Mostrar los puntos actuales en el TextView
    }
}
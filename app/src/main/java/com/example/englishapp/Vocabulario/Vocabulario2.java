package com.example.englishapp.Vocabulario;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishapp.R;

public class Vocabulario2 extends AppCompatActivity {

    private ImageView imagen;
    private TextView frase, tvPuntos;
    private int indiceActual = 0;
    private int puntos;
    private int vidas;
    private Button[] botonesRespuesta;
    private ImageButton[] botonVidas; // Los botones que representan las vidas
    private SharedPreferences sharedPreferences;

    // Las imágenes y las respuestas correspondientes (verbos y preposiciones)
    private int[] imagenes = {R.drawable.gato1, R.drawable.gato2, R.drawable.gato3, R.drawable.gato4}; // Gato debajo de la mesa, durmiendo, saltando, encima de la mesa
    private String[][] respuestas = {
            {"UNDER", "IN", "SLEEPING", "JUMPING"},   // Respuestas para la imagen 0 (gato debajo de la mesa)
            {"SLEEPING", "UNDER", "JUMPING", "IN"},   // Respuestas para la imagen 1 (gato durmiendo)
            {"JUMPING", "SLEEPING", "UNDER", "IN"},   // Respuestas para la imagen 2 (gato saltando)
            {"ON", "UNDER", "IN", "JUMPING"}          // Respuestas para la imagen 3 (gato encima de la mesa)
    };

    private String[] frases = {
            "The cat is ___ the table",  // Para la imagen 0
            "The cat is ___ the table",  // Para la imagen 1
            "The cat is ___",            // Para la imagen 2
            "The cat is ___"             // Para la imagen 3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulario2);

        sharedPreferences = getSharedPreferences("Juego", MODE_PRIVATE);
        puntos = sharedPreferences.getInt("Puntos", 0);  // Recuperar puntos guardados
        vidas = sharedPreferences.getInt("Vidas", 3);    // Recuperar vidas guardadas

        inicializarElementos();
        mostrarPreguntas();
    }

    public void inicializarElementos() {
        imagen = findViewById(R.id.iv_imagen);
        frase = findViewById(R.id.tv_frase);
        tvPuntos = findViewById(R.id.tv_puntos);

        botonesRespuesta = new Button[] {
                findViewById(R.id.bt_option1),
                findViewById(R.id.bt_option2),
                findViewById(R.id.bt_option3),
                findViewById(R.id.bt_option4)
        };

        botonVidas = new ImageButton[]{
                findViewById(R.id.imageButton10),
                findViewById(R.id.imageButton11),
                findViewById(R.id.imageButton12)
        };
    }

    public void mostrarPreguntas() {
        if (indiceActual < frases.length) {
            frase.setText(frases[indiceActual]);
            imagen.setImageResource(imagenes[indiceActual]);

            // Actualizar las respuestas según la imagen actual
            String[] opcionesRespuesta = respuestas[indiceActual];
            for (int i = 0; i < botonesRespuesta.length; i++) {
                botonesRespuesta[i].setText(opcionesRespuesta[i]);
            }
        } else {
            Toast.makeText(this, "¡Juego terminado! Puntos finales: " + puntos, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void verificarRespuesta(View view) {
        String respuestaSeleccionada = ((Button) view).getText().toString();

        // Verificar si la respuesta seleccionada es correcta
        if (respuestas[indiceActual][0].equalsIgnoreCase(respuestaSeleccionada)) {
            puntos += 5;
            Toast.makeText(this, "¡Correcto! Puntos: " + puntos, Toast.LENGTH_SHORT).show();
            actualizarPuntos();
            indiceActual++;
            if (indiceActual < frases.length) {
                mostrarPreguntas();
            } else {
                Toast.makeText(this, "¡Juego terminado! Puntos finales: " + puntos, Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            // Restar solo vidas, no puntos
            vidas--;
            Toast.makeText(this, "¡Incorrecto! Inténtalo de nuevo. Vidas restantes: " + vidas, Toast.LENGTH_SHORT).show();

            // Ocultar el ícono de vida correspondiente
            if (vidas > 0) {
                botonVidas[vidas].setVisibility(View.INVISIBLE);
            }

            if (vidas == 0) {
                Toast.makeText(this, "¡Juego terminado! Puntos finales: " + puntos, Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        // Guardar el estado del juego (puntos y vidas) después de cada intento
        guardarEstadoJuego();
    }

    private void actualizarPuntos() {
        tvPuntos.setText("Puntos: " + puntos);
    }

    // Método para guardar puntos y vidas en SharedPreferences
    private void guardarEstadoJuego() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("Puntos", puntos);  // Guardar los puntos
        editor.putInt("Vidas", vidas);    // Guardar las vidas
        editor.apply();  // Aplicar los cambios
    }
}
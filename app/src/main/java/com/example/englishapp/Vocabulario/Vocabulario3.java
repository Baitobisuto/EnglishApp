package com.example.englishapp.Vocabulario;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.englishapp.R;


public class Vocabulario3 extends AppCompatActivity {

    private int puntos = 0; // Puntos iniciales
    private int indiceParteCuerpo = 0;
    private int vidas = 3; // Número de vidas iniciales
    private EditText etNombreParte;
    private ImageView imagen;
    private ImageButton[] botonVidas; // Botones de vidas
    private int[] imagenes = {R.drawable.boca, R.drawable.cuello, R.drawable.pie, R.drawable.mano};
    private String[] respuestasCorrectas = {"mouth", "neck", "foot", "hand"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulario3);

        inicializarVariables();
    }

    // Método para inicializar las variables
    public void inicializarVariables() {
        etNombreParte = findViewById(R.id.et_NombreParte);
        imagen = findViewById(R.id.id_pera);
        imagen.setImageResource(imagenes[indiceParteCuerpo]);

        // Inicialización de los botones de vida
        botonVidas = new ImageButton[]{
                findViewById(R.id.imageButton),  // Vida 1
                findViewById(R.id.imageButton5),  // Vida 2
                findViewById(R.id.imageButton13)  // Vida 3
        };
    }

    // Método que se llama para verificar la respuesta
    public void verificarRespuesta(View view) {
        String respuesta = etNombreParte.getText().toString().trim().toLowerCase();

        if (respuesta.equals(respuestasCorrectas[indiceParteCuerpo])) {
            puntos += 3; // Sumar puntos si la respuesta es correcta
            Toast.makeText(this, "¡Correcto! Puntos: " + puntos, Toast.LENGTH_SHORT).show();
            indiceParteCuerpo++; // Avanzar a la siguiente pregunta

            if (indiceParteCuerpo < imagenes.length) {
                imagen.setImageResource(imagenes[indiceParteCuerpo]);
            } else {
                Toast.makeText(this, "¡Juego terminado! Puntos finales: " + puntos, Toast.LENGTH_SHORT).show();
            }
        } else {
            puntos -= 3; // Restar puntos si la respuesta es incorrecta
            vidas--; // Restar una vida
            Toast.makeText(this, "Incorrecto. La respuesta correcta es " + respuestasCorrectas[indiceParteCuerpo] + ". Puntos: " + puntos, Toast.LENGTH_SHORT).show();

            // Ocultar el ícono de vida correspondiente
            if (vidas >= 0) {
                botonVidas[vidas].setVisibility(View.INVISIBLE); // Ocultamos el ícono de vida perdido
            }

            // Verificar si se han acabado las vidas
            if (vidas == 0) {
                Toast.makeText(this, "¡Juego terminado! Puntos finales: " + puntos, Toast.LENGTH_SHORT).show();
                finish(); // Finaliza la actividad
            }
        }
        etNombreParte.setText(""); // Limpiar el campo de texto
    }
}
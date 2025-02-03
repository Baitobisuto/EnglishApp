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

public class Gramatica4 extends AppCompatActivity {

    private TextView tvUsuario, tvPuntos, tvFrase;
    private EditText etRespuesta;
    private ImageButton[] vidas;
    private ImageButton btBack, btOk;
    private int puntos = 0;
    private int vidasRestantes = 3;
    private String usuario;
    private SharedPreferences sharedPreferences;

    private Map<String, String> verbos = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gramatica4);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences("Juego", MODE_PRIVATE);
        puntos = sharedPreferences.getInt("Puntos", 0);

        inicializarVariables();
        cargarVerbos();
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

    private void cargarVerbos() {
        // Aquí puedes añadir más verbos y sus formas en pasado
        verbos.put("go", "went");
        verbos.put("read", "read"); // read se escribe igual en pasado, pero se pronuncia diferente
        verbos.put("play", "played");
        verbos.put("eat", "ate");
        verbos.put("travel", "traveled");
    }

    private void cargarFrase() {
        String[] presentes = verbos.keySet().toArray(new String[0]); // Obtener todos los verbos en presente
        int indice = (int) (Math.random() * presentes.length); // Elegir uno al azar
        String presente = presentes[indice];
        String frase = "I " + presente + " to the store yesterday."; // Crear la frase en presente

        tvFrase.setText(frase);
        etRespuesta.setText("");
    }

    private void verificarRespuesta() {
        String respuestaUsuario = etRespuesta.getText().toString().trim().toLowerCase();
        String presente = tvFrase.getText().toString().split(" ")[1]; // Obtener el verbo en presente de la frase
        String pasadoCorrecto = verbos.get(presente); // Obtener el pasado correcto del verbo

        if (respuestaUsuario.equals(pasadoCorrecto)) {
            puntos += 3;
            tvPuntos.setText(String.valueOf(puntos));
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
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
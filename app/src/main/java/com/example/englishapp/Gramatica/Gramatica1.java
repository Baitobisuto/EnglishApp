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
import com.example.englishapp.DAO.UsuariosDAO;
import com.example.englishapp.Menu;
import com.example.englishapp.R;
import com.example.englishapp.Usuario;

public class Gramatica1 extends AppCompatActivity {

    private TextView tvUsuario, tvPuntos, tvFrase;
    private EditText etRespuesta;
    private ImageButton[] vidas = new ImageButton[3];
    private int puntuacion, vidasRestantes, avatar, idUsuario;
    private String nombreUsuario;
    private int fraseActual = 0;
    private UsuariosDAO usuariosDAO;

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
        usuariosDAO = new UsuariosDAO(this); // Inicializar UsuariosDAO
        cargarDatosUsuario();
        cargarFrase();
    }

    private void cargarDatosUsuario() {
        idUsuario = getIntent().getIntExtra("idUsuario", -1);

        if (idUsuario != -1) {
            Usuario usuario = usuariosDAO.obtenerUsuarioPorId(idUsuario);

            if (usuario != null) {
                nombreUsuario = usuario.getNombre();
                puntuacion = usuario.getPuntuacion();
                vidasRestantes = usuario.getVidas();
                avatar = usuario.getAvatar();

                tvUsuario.setText(nombreUsuario);
                tvPuntos.setText(String.valueOf(puntuacion));
                actualizarVidas();
            } else {
                Toast.makeText(this, R.string.error_cargar_usuario, Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, R.string.error_id_usuario, Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private void inicializarVariables() {
        tvUsuario = findViewById(R.id.tv_usuario);
        tvPuntos = findViewById(R.id.tv_puntos);
        tvFrase = findViewById(R.id.tv_frase);
        etRespuesta = findViewById(R.id.et_respuesta);

        vidas[0] = findViewById(R.id.imageButton7);
        vidas[1] = findViewById(R.id.imageButton8);
        vidas[2] = findViewById(R.id.imageButton9);

        ImageButton btBack = findViewById(R.id.bt_back);
        ImageButton btOk = findViewById(R.id.bt_ok);

        btBack.setOnClickListener(v -> botonBack());
        btOk.setOnClickListener(v -> verificarRespuesta());
    }

    private void cargarFrase() {
        if (fraseActual < frases.length && vidasRestantes > 0) {
            tvFrase.setText(frases[fraseActual]);
            etRespuesta.setText("");
        } else {
            Toast.makeText(this, getString(R.string.juego_terminado_gramatica + puntuacion), Toast.LENGTH_SHORT).show();
            guardarPuntuacion();
            finish();
        }
    }

    private void verificarRespuesta() {
        String respuestaUsuario = etRespuesta.getText().toString().trim().toLowerCase();
        String respuestaCorrecta = respuestas[fraseActual].toLowerCase();

        if (respuestaUsuario.equals(respuestaCorrecta)) {
            puntuacion += 10;
            tvPuntos.setText(String.valueOf(puntuacion));
            Toast.makeText(this, R.string.correcto, Toast.LENGTH_SHORT).show();
            fraseActual++;
            cargarFrase();
        } else {
            vidasRestantes--;
            actualizarVidas();
            if (vidasRestantes >= 0) {
                Toast.makeText(this, getString(R.string.incorrecto_te_quedan_vidas + vidasRestantes + 1), Toast.LENGTH_SHORT).show();
            }
            if (vidasRestantes == 0) {
                Toast.makeText(this, R.string.game_over, Toast.LENGTH_SHORT).show();
                guardarPuntuacion();
                finish();
            }
        }
    }

    private void guardarPuntuacion() {
        boolean actualizado = usuariosDAO.actualizarPuntuacion(idUsuario, puntuacion);

        if (!actualizado) {
            Toast.makeText(this, R.string.error_guardar_puntuacion, Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarVidas() {
        for (int i = 0; i < vidas.length; i++) {
            vidas[i].setVisibility(i < vidasRestantes ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void botonBack() {
        Intent intent = new Intent(this, Menu.class);
        intent.putExtra("idUsuario", idUsuario);
        startActivity(intent);
        finish();
    }
}
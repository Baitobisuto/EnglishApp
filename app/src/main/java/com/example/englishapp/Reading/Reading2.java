package com.example.englishapp.Reading;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.englishapp.DAO.UsuariosDAO;
import com.example.englishapp.Menu;
import com.example.englishapp.R;
import com.example.englishapp.Usuario;

public class Reading2 extends AppCompatActivity {

    private TextView tvPuntos, tvPregunta;
    private ImageView ivImagen1, ivImagen2, ivImagen3, ivImagen4;
    private ImageButton[] vidas; // Array de ImageButton para las vidas
    private int puntos = 0;
    private int vidasRestantes = 3;
    private UsuariosDAO usuariosDAO; // Instancia de UsuariosDAO
    private Usuario usuario; // Instancia de Usuario

    private int[] imagenesGato = {
            R.drawable.gato1,
            R.drawable.gato2,
            R.drawable.gato3,
            R.drawable.gato4
    };

    private String[] preguntasGato = {
            "What is the cat doing?",
            "What is the cat doing?",
            "What is the cat doing?",
            "What is the cat doing?"
    };

    private int respuestaCorrecta = 0;
    private int preguntaActual = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reading2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        usuariosDAO = new UsuariosDAO(this); // Inicializa UsuariosDAO

        inicializarVariables();
        cargarDatosUsuario(); // Carga datos del usuario desde la BD
        cargarPregunta();
    }

    public void cargarDatosUsuario() {
        Intent intent = getIntent();
        int idUsuario = intent.getIntExtra("idUsuario", -1);

        if (idUsuario != -1) {
            usuario = usuariosDAO.obtenerUsuarioPorId(idUsuario);
            if (usuario != null) {
                puntos = usuario.getPuntuacion();
                vidasRestantes = usuario.getVidas();
                actualizarPuntos();
                actualizarVidas();
            } else {
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "ID de usuario no válido", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void actualizarPuntos() {
        tvPuntos.setText(String.valueOf(puntos));
    }

    public void actualizarVidas() {
        for (int i = 0; i < vidas.length; i++) {
            vidas[i].setVisibility(i < vidasRestantes ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void inicializarVariables() {
        tvPuntos = findViewById(R.id.tv_puntos);
        tvPregunta = findViewById(R.id.tv_pregunta);
        ivImagen1 = findViewById(R.id.iv_imagen1);
        ivImagen2 = findViewById(R.id.iv_imagen2);
        ivImagen3 = findViewById(R.id.iv_imagen3);
        ivImagen4 = findViewById(R.id.iv_imagen4);
        vidas = new ImageButton[]{
                findViewById(R.id.imageButton7),
                findViewById(R.id.imageButton8),
                findViewById(R.id.imageButton9)
        };

        tvPuntos.setText(String.valueOf(puntos));

        ivImagen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarRespuesta(0);
            }
        });

        ivImagen2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarRespuesta(1);
            }
        });

        ivImagen3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarRespuesta(2);
            }
        });

        ivImagen4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificarRespuesta(3);
            }
        });

        findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botonAtras();
            }
        });
    }

    public void cargarPregunta() {
        if (preguntaActual < preguntasGato.length) {
            tvPregunta.setText(preguntasGato[preguntaActual]);
            ivImagen1.setImageResource(imagenesGato[0]);
            ivImagen2.setImageResource(imagenesGato[1]);
            ivImagen3.setImageResource(imagenesGato[2]);
            ivImagen4.setImageResource(imagenesGato[3]);

            respuestaCorrecta = preguntaActual;
        } else {
            Toast.makeText(this, "Juego terminado. Puntuación final: " + puntos, Toast.LENGTH_SHORT).show();
            usuario.setPuntuacion(puntos);
            usuario.setVidas(vidasRestantes);
            usuariosDAO.actualizarUsuario(usuario);
            Intent intent = new Intent(this, Menu.class);
            intent.putExtra("idUsuario", usuario.getId());
            startActivity(intent);
            finish();
        }
    }

    private void verificarRespuesta(int respuestaUsuario) {
        if (respuestaUsuario == respuestaCorrecta) {
            puntos += 3;
            actualizarPuntos();
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
            preguntaActual++;
            cargarPregunta();
        } else {
            vidasRestantes--;
            actualizarVidas();
            if (vidasRestantes >= 0) {
                Toast.makeText(this, "Incorrecto. Te quedan " + (vidasRestantes + 1) + " vidas.", Toast.LENGTH_SHORT).show();
                if (vidasRestantes == 0) {
                    Toast.makeText(this, "¡Game Over!", Toast.LENGTH_SHORT).show();
                    usuario.setPuntuacion(puntos);
                    usuario.setVidas(vidasRestantes);
                    usuariosDAO.actualizarUsuario(usuario);
                    finish();
                }
            }
        }
    }

    public void botonAtras() {
        Intent intent = new Intent(this, Menu.class);
        intent.putExtra("idUsuario", usuario.getId());
        startActivity(intent);
        finish();
    }
}
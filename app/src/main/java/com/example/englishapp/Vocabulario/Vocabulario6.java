package com.example.englishapp.Vocabulario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
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
import com.example.englishapp.R;
import com.example.englishapp.Usuario;

public class Vocabulario6 extends AppCompatActivity {

    private TextView textViewPregunta, textViewPuntos, tvUsuario;
    private ImageButton button1, button2, button3, buttonExit;
    private ImageButton[] vidasBotones; // Array para las vidas
    private ImageView ivAvatar;
    private int puntos;
    private int vidas = 3;
    private int nivelCompletado;
    private int respuestaCorrectaIndice;
    private UsuariosDAO usuariosDAO;
    private Usuario usuario;

    private String[] frutas = {"Manzana", "Fresa", "Plátano"};
    private int[] imagenes = {R.drawable.manzana, R.drawable.fresa, R.drawable.platano};


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
        usuariosDAO = new UsuariosDAO(this);
        inicializarVariables();
        cargarDatosUsuario(); // Cargar datos del usuario
        cargarPregunta();
    }


    private void inicializarVariables() {
        textViewPregunta = findViewById(R.id.textView);
        textViewPuntos = findViewById(R.id.tvPuntos);
        tvUsuario = findViewById(R.id.tvUsuario);
        ivAvatar = findViewById(R.id.iv_avatar);
        button1 = findViewById(R.id.imageButton28);
        button2 = findViewById(R.id.imageButton29);
        button3 = findViewById(R.id.imageButton27);
        buttonExit = findViewById(R.id.bt_exit7);

        vidasBotones = new ImageButton[]{ // Inicializar el array
                findViewById(R.id.imageButton20),
                findViewById(R.id.imageButton25),
                findViewById(R.id.imageButton26)
        };
    }

    public void cargarDatosUsuario() {
        Intent intent = getIntent();
        int idUsuario = intent.getIntExtra("idUsuario", -1);

        if (idUsuario != -1) {
            usuario = usuariosDAO.obtenerUsuarioPorId(idUsuario);
            if (usuario != null) {
                puntos = usuario.getPuntuacion();
                vidas = usuario.getVidas();
                nivelCompletado = usuario.getNivelCompletado();
                establecerImagenAvatar(usuario.getAvatar());
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

    private void establecerImagenAvatar(int avatar) {
        int avatarDrawable = getAvatarDrawable(avatar);
        if (avatarDrawable != 0) {
            ivAvatar.setImageResource(avatarDrawable);
        } else {
            Toast.makeText(this, "No se ha encontrado ningún avatar", Toast.LENGTH_SHORT).show();
        }
    }

    private int getAvatarDrawable(int avatar) {
        switch (avatar) {
            case 0:
                return R.drawable.spiderman;
            case 1:
                return R.drawable.capitanamerica;
            case 2:
                return R.drawable.batman;
            case 3:
                return R.drawable.hulk;
            default:
                return 0;
        }
    }

    private void actualizarPuntos() {
        textViewPuntos.setText("Puntos: " + puntos);
    }

    private void cargarPregunta() {
        if (vidas <= 0) {
            Toast.makeText(this, "¡Juego Terminado! Puntos finales: " + puntos, Toast.LENGTH_LONG).show();
            // Guardar puntos y vidas antes de finalizar
            usuario.setPuntuacion(puntos);
            usuario.setVidas(vidas);
            usuariosDAO.actualizarUsuario(usuario);
            finish();
            return;
        }

        respuestaCorrectaIndice = (int) (Math.random() * frutas.length);
        textViewPregunta.setText(frutas[respuestaCorrectaIndice]);

        button1.setImageResource(imagenes[0]);
        button2.setImageResource(imagenes[1]);
        button3.setImageResource(imagenes[2]);

        textViewPuntos.setText("Puntos: " + puntos);
        actualizarVidas();
    }

    private void actualizarVidas() {
        for (int i = 0; i < vidasBotones.length; i++) {
            vidasBotones[i].setVisibility(i < vidas ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void comprobarRespuesta(View view) {
        int respuestaUsuario = -1;

        if (view == button1) {
            respuestaUsuario = 0;
        } else if (view == button2) {
            respuestaUsuario = 1;
        } else if (view == button3) {
            respuestaUsuario = 2;
        }

        if (respuestaUsuario == respuestaCorrectaIndice) {
            puntos += 3;
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
        } else {
            puntos -= 3;
            vidas--;
            Toast.makeText(this, "¡Incorrecto!", Toast.LENGTH_SHORT).show();
        }

        textViewPuntos.setText("Puntos: " + puntos);

        if (usuario != null) {
            usuario.setPuntuacion(puntos);
            usuario.setVidas(vidas);
            usuariosDAO.actualizarUsuario(usuario);
        }

        actualizarVidas();

        if (vidas <= 0) {
            Toast.makeText(this, "Juego terminado, no tienes más vidas.", Toast.LENGTH_LONG).show();
            // Guardar puntos y vidas antes de finalizar
            usuario.setPuntuacion(puntos);
            usuario.setVidas(vidas);
            usuariosDAO.actualizarUsuario(usuario);
            finish();
        } else {
            cargarPregunta();
        }
    }

    public void botonExit(View view) {
        Intent intent = new Intent(this, Menu.class);
        intent.putExtra("idUsuario", usuario.getId());
        startActivity(intent);
        finish();
    }
}
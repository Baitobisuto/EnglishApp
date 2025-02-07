package com.example.englishapp.Vocabulario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

import com.example.englishapp.DAO.UsuariosDAO;
import com.example.englishapp.Menu;
import com.example.englishapp.Niveles;
import com.example.englishapp.R;
import com.example.englishapp.Usuario;

public class Vocabulario1 extends AppCompatActivity {
    private int puntos;
    private int vidas;
    private int indiceFruta = 0;
    private TextView tvPuntos;
    private ImageView imagen;
    private ImageView[] iconosVidas;
    private ImageView ivAvatar;
    private int avatar;
    private int idUsuario;
    private int nivelCompletado;

    private int[] imagenes = {R.drawable.fresa, R.drawable.aguacate, R.drawable.manzana, R.drawable.naranja};
    private String[] respuestas = {"fresa", "aguacate", "manzana", "naranja"};
    private UsuariosDAO usuariosDAO;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulario1);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usuariosDAO = new UsuariosDAO(this);
        inicializaVariables();
        cargarDatosUsuario();
    }

    public void cargarDatosUsuario() {
        Intent intent = getIntent();
        idUsuario = intent.getIntExtra("idUsuario", -1);

        if (idUsuario != -1) {
            usuario = usuariosDAO.obtenerUsuarioPorId(idUsuario);
            if (usuario != null) {
                avatar = usuario.getAvatar();
                puntos = usuario.getPuntuacion();
                vidas = usuario.getVidas();
                nivelCompletado = usuario.getNivelCompletado();

                establecerImagenAvatar(avatar);
                actualizarPuntos();
                actualizarVidas();
                imagen.setImageResource(imagenes[indiceFruta]);
            } else {
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "ID de usuario no válido", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    public void inicializaVariables() {
        tvPuntos = findViewById(R.id.tv_puntos);
        imagen = findViewById(R.id.img_fruta);
        iconosVidas = new ImageView[]{
                findViewById(R.id.imageButton7),
                findViewById(R.id.imageButton8),
                findViewById(R.id.imageButton9)
        };
        ivAvatar = findViewById(R.id.iv_avatar);
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

    public void verificarRespuesta(View view) {
        EditText etNombreFruta = findViewById(R.id.et_NombreFruta);
        String respuesta = etNombreFruta.getText().toString().trim().toLowerCase();

        if (respuesta.equals(respuestas[indiceFruta])) {
            puntos += 3;
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
            indiceFruta++;

            if (indiceFruta < imagenes.length) {
                imagen.setImageResource(imagenes[indiceFruta]);
            } else {
                Toast.makeText(this, "¡Juego terminado! Vamos al siguiente nivel.", Toast.LENGTH_SHORT).show();

                nivelCompletado++; // Incrementa el nivel completado
                usuario.setNivelCompletado(nivelCompletado); // Actualiza el objeto usuario
                usuariosDAO.actualizarUsuario(usuario); // Actualiza la base de datos

                Intent intent = new Intent(this, Niveles.class);
                intent.putExtra("idUsuario", idUsuario);
                startActivity(intent);
                finish();
            }
        } else {
            vidas--;
            Toast.makeText(this, "Incorrecto", Toast.LENGTH_SHORT).show();
            actualizarVidas();
            if (vidas == 0) {
                Toast.makeText(this, "¡Juego terminado! Has perdido.", Toast.LENGTH_SHORT).show();
                guardarProgresoYFinalizar();
                return;
            }
        }

        actualizarPuntos();
        etNombreFruta.setText("");
    }

    public void guardarProgresoYFinalizar() {
        if (idUsuario != -1 && usuario != null) {
            usuario.setPuntuacion(puntos);
            usuario.setVidas(vidas);
            usuariosDAO.actualizarUsuario(usuario);
        }
        Intent intent = new Intent(this, Niveles.class);
        intent.putExtra("idUsuario", idUsuario);
        startActivity(intent);
        finish();
    }

    private void actualizarPuntos() {
        tvPuntos.setText("Puntos: " + puntos);
    }

    private void actualizarVidas() {
        for (int i = 0; i < 3; i++) {
            iconosVidas[i].setVisibility(i < vidas ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void botonBack(View view) {
        finish();
    }
}
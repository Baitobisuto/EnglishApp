package com.example.englishapp.Vocabulario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
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

import com.example.englishapp.DAO.UsuariosDAO;
import com.example.englishapp.R;
import com.example.englishapp.Usuario;

public class Vocabulario4 extends AppCompatActivity {

    private int puntos;
    private int vidas;
    private int nivelVocabulario; // Nivel de vocabulario
    private MediaPlayer sonidoNivelCompletado;
    private MediaPlayer sonidoError;
    private int numeroCorrecto = 1;
    private EditText etRespuesta;
    private TextView tvPuntos, tvUsuario;
    private ImageButton[] vidasBotones;
    private ImageView imagenNumero, ivAvatar;
    private ImageButton botonComprobar;
    private String[] numerosEnIngles = {"one", "two", "three", "four", "five"};
    private UsuariosDAO usuariosDAO;
    private Usuario usuario;


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
        sonidoNivelCompletado = MediaPlayer.create(this, R.raw.nivel_completado);
        sonidoError = MediaPlayer.create(this, R.raw.fallo);

        if (sonidoNivelCompletado == null || sonidoError == null) {
            Toast.makeText(this, "Error al cargar sonidos", Toast.LENGTH_SHORT).show();
        }

        usuariosDAO = new UsuariosDAO(this); // Inicializar UsuariosDAO
        inicializarVariables();
        cargarDatosUsuario(); // Cargar datos del usuario
        configurarBotonComprobar();
    }

    public void inicializarVariables() {
        etRespuesta = findViewById(R.id.etRespuesta);
        tvPuntos = findViewById(R.id.tvPuntos);
        tvUsuario = findViewById(R.id.textView5);
        imagenNumero = findViewById(R.id.imageViewNumero);
        ivAvatar = findViewById(R.id.iv_avatar);
        botonComprobar = findViewById(R.id.id_boton);

        vidasBotones = new ImageButton[]{
                findViewById(R.id.imageButton),
                findViewById(R.id.imageButton5),
                findViewById(R.id.imageButton13)
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
                nivelVocabulario = usuario.getNivelVocabulario();
                establecerImagenAvatar(usuario.getAvatar());
                actualizarPuntos();
                actualizarVidas();
                imagenNumero.setImageResource(R.drawable.uno); // Mostrar la primera imagen
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

    public void configurarBotonComprobar() {
        botonComprobar.setOnClickListener(view -> comprobarRespuesta());
    }

    public void comprobarRespuesta() {
        String respuesta = etRespuesta.getText().toString().trim().toLowerCase();

        if (respuesta.equals(numerosEnIngles[numeroCorrecto - 1])) {
            puntos += 3;
            Toast.makeText(this, "¡Correcto! Puntos: " + puntos, Toast.LENGTH_SHORT).show();
            numeroCorrecto++;

            if (numeroCorrecto <= numerosEnIngles.length) {
                cambiarImagenNumero();
            } else {
                Toast.makeText(this, "¡Juego terminado! Puntos finales: " + puntos, Toast.LENGTH_SHORT).show();

                // Actualiza el nivel de vocabulario *solo si es necesario*
                if (nivelVocabulario < 6) { // Asumiendo 6 niveles máximos
                    nivelVocabulario++;
                    usuario.setNivelVocabulario(nivelVocabulario);
                    usuariosDAO.actualizarUsuario(usuario);

                    if (sonidoNivelCompletado != null) {
                        sonidoNivelCompletado.start();
                    }
                }

                Intent intent = new Intent(this, Menu.class);
                intent.putExtra("idUsuario", usuario.getId());
                startActivity(intent);
                finish();
            }
        } else {
            if (puntos > 3) {
                puntos -= 3;
            } else {
                puntos = 0;
            }

            vidas--;
            if (sonidoError != null) {
                sonidoError.start();
            }
            Toast.makeText(this, "Incorrecto. Puntos: " + puntos, Toast.LENGTH_SHORT).show();
            actualizarVidas();

            if (vidas == 0) {
                Toast.makeText(this, "¡Juego terminado! Puntos finales: " + puntos, Toast.LENGTH_SHORT).show();
                // Guardar puntos y vidas antes de finalizar
                usuario.setPuntuacion(puntos);
                usuario.setVidas(vidas);
                usuariosDAO.actualizarUsuario(usuario);
                finish();
            }
        }

        etRespuesta.setText("");
        actualizarPuntos();
        guardarEstadoJuego();
    }

    private void cambiarImagenNumero() {
        switch (numeroCorrecto) {
            case 2:
                imagenNumero.setImageResource(R.drawable.dos);
                break;
            case 3:
                imagenNumero.setImageResource(R.drawable.tres);
                break;
            case 4:
                imagenNumero.setImageResource(R.drawable.cuatro);
                break;
            case 5:
                imagenNumero.setImageResource(R.drawable.cinco);
                break;
            default:
                break;
        }
    }

    private void actualizarPuntos() {
        tvPuntos.setText("Puntos: " + puntos);
    }

    private void actualizarVidas() {
        for (int i = 0; i < vidasBotones.length; i++) { // Usar la longitud correcta
            vidasBotones[i].setVisibility(i < vidas ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void guardarEstadoJuego() {
        if (usuario != null) {
            usuario.setPuntuacion(puntos);
            usuario.setVidas(vidas);
            usuariosDAO.actualizarUsuario(usuario);
        }
    }

    public void botonExit(View view) {
        Intent intent = new Intent(this, Menu.class);
        intent.putExtra("idUsuario", usuario.getId()); // Pasar ID del usuario
        startActivity(intent);
        finish();
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (sonidoNivelCompletado != null) {
            sonidoNivelCompletado.release();
            sonidoNivelCompletado = null;
        }
        if (sonidoError != null) {
            sonidoError.release();
            sonidoError = null;
        }
    }
}

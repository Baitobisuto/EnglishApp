package com.example.englishapp.Vocabulario;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishapp.DAO.UsuariosDAO;
import com.example.englishapp.R;
import com.example.englishapp.Usuario;

public class Vocabulario2 extends AppCompatActivity {

    private ImageView imagen;
    private TextView frase, tvPuntos, tvUsuario;
    private ImageView ivAvatar;
    private int indiceActual = 0;
    private int puntos;
    private int vidas;
    private int nivelCompletado; // Nuevo: Nivel completado
    private Button[] botonesRespuesta;
    private ImageButton[] botonVidas;
    private UsuariosDAO usuariosDAO;
    private Usuario usuario;

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

        usuariosDAO = new UsuariosDAO(this);
        inicializarElementos();
        cargarDatosUsuario();
    }

    public void inicializarElementos() {
        imagen = findViewById(R.id.iv_imagen);
        frase = findViewById(R.id.tv_frase);
        tvPuntos = findViewById(R.id.tv_puntos);
        tvUsuario = findViewById(R.id.tv_usuario);
        ivAvatar = findViewById(R.id.iv_avatar);

        botonesRespuesta = new Button[]{
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

    public void cargarDatosUsuario() {
        Intent intent = getIntent();
        int idUsuario = intent.getIntExtra("idUsuario", -1); // Obtener ID del usuario

        if (idUsuario != -1) {
            usuario = usuariosDAO.obtenerUsuarioPorId(idUsuario); // Obtener usuario por ID
            if (usuario != null) {
                puntos = usuario.getPuntuacion();
                vidas = usuario.getVidas();
                nivelCompletado = usuario.getNivelCompletado(); // Cargar nivel completado
                establecerImagenAvatar(usuario.getAvatar()); // Establecer avatar
                actualizarPuntos();
                actualizarVidas();
                mostrarPreguntas(); // Mostrar las preguntas después de cargar los datos
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
    public void mostrarPreguntas() {
        if (indiceActual < frases.length) {
            frase.setText(frases[indiceActual]);
            imagen.setImageResource(imagenes[indiceActual]);
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
            puntos += 3;
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

            vidas--;
            Toast.makeText(this, "¡Incorrecto! Inténtalo de nuevo. Vidas restantes: " + vidas, Toast.LENGTH_SHORT).show();

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

    private void actualizarVidas() {
        for (int i = 0; i < 3; i++) {
            if (i < vidas) {
                botonVidas[i].setVisibility(View.VISIBLE);
            } else {
                botonVidas[i].setVisibility(View.INVISIBLE);
            }
        }
    }

    private void guardarEstadoJuego() {
        if (usuario != null) { // Usar el objeto Usuario
            usuario.setPuntuacion(puntos);
            usuario.setVidas(vidas);
            // Actualizar nivel completado si es necesario (ej. al final del juego)
            // usuario.setNivelCompletado(nivelCompletado);
            usuariosDAO.actualizarUsuario(usuario); // Actualizar el usuario en la base de datos
        }
    }

    public void botonExit(View view) {
        Intent intent = new Intent(this, Menu.class);
        intent.putExtra("idUsuario", usuario.getId()); // Pasar el ID del usuario al menú
        startActivity(intent);
        finish();
    }
}
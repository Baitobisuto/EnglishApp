package com.example.englishapp.Vocabulario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.englishapp.DAO.UsuariosDAO;
import com.example.englishapp.R;
import com.example.englishapp.Usuario;


public class Vocabulario3 extends AppCompatActivity {

    private int puntos;
    private int indiceParteCuerpo = 0;
    private int vidas;
    private int nivelCompletado; // Nivel completado
    private EditText etNombreParte;
    private ImageView imagen, ivAvatar;
    private TextView tvPuntos, tvUsuario;
    private ImageButton[] botonVidas;
    private UsuariosDAO usuariosDAO;
    private Usuario usuario;

    private int[] imagenes = {R.drawable.boca, R.drawable.cuello, R.drawable.pie, R.drawable.mano};
    private String[] respuestasCorrectas = {"mouth", "neck", "foot", "hand"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulario3);

        usuariosDAO = new UsuariosDAO(this);
        inicializarVariables();
        cargarDatosUsuario();
    }

    public void inicializarVariables() {
        etNombreParte = findViewById(R.id.et_NombreParte);
        imagen = findViewById(R.id.id_pera);
        ivAvatar = findViewById(R.id.iv_avatar);
        tvPuntos = findViewById(R.id.tvPuntos);
        tvUsuario = findViewById(R.id.textView4);

        botonVidas = new ImageButton[]{
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
                nivelCompletado = usuario.getNivelCompletado(); // Cargar nivel completado
                establecerImagenAvatar(usuario.getAvatar());
                actualizarPuntos();
                actualizarVidas();
                imagen.setImageResource(imagenes[indiceParteCuerpo]); // Mostrar la primera imagen
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


    public void verificarRespuesta(View view) {
        String respuesta = etNombreParte.getText().toString().trim().toLowerCase();

        if (respuesta.equals(respuestasCorrectas[indiceParteCuerpo])) {
            puntos += 3;
            Toast.makeText(this, "¡Correcto! Puntos: " + puntos, Toast.LENGTH_SHORT).show();
            indiceParteCuerpo++;

            if (indiceParteCuerpo < imagenes.length) {
                imagen.setImageResource(imagenes[indiceParteCuerpo]);
            } else {
                Toast.makeText(this, "¡Juego terminado! Puntos finales: " + puntos, Toast.LENGTH_SHORT).show();
                // Actualizar nivel completado y guardarlo
                nivelCompletado++;
                usuario.setNivelCompletado(nivelCompletado);
                usuariosDAO.actualizarUsuario(usuario);
                // Volver al menú
                Intent intent = new Intent(this, Menu.class);
                intent.putExtra("idUsuario", usuario.getId());
                startActivity(intent);
                finish();
            }
        } else {
            vidas--;
            Toast.makeText(this, "Incorrecto. La respuesta correcta es " + respuestasCorrectas[indiceParteCuerpo] + ". Vidas restantes: " + vidas, Toast.LENGTH_SHORT).show();
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

        etNombreParte.setText("");
        guardarEstadoJuego();
    }

    private void actualizarPuntos() {
        tvPuntos.setText("Puntos: " + puntos);
    }

    private void actualizarVidas() {
        for (int i = 0; i < botonVidas.length; i++) { // Usar la longitud del array botonVidas
            botonVidas[i].setVisibility(i < vidas ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void guardarEstadoJuego() {
        if (usuario != null) {
            usuario.setPuntuacion(puntos);
            usuario.setVidas(vidas);
            // Actualizar nivel completado si es necesario
            usuariosDAO.actualizarUsuario(usuario);
        }
    }

    public void botonExit(View view) {
        Intent intent = new Intent(this, Menu.class);
        intent.putExtra("idUsuario", usuario.getId());
        startActivity(intent);
        finish();
    }
}
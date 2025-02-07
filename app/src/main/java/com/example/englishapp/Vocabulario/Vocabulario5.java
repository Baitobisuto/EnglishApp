package com.example.englishapp.Vocabulario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishapp.DAO.UsuariosDAO;
import com.example.englishapp.R;
import com.example.englishapp.Usuario;

public class Vocabulario5 extends AppCompatActivity {

    private ImageView imagenEstancia, ivAvatar;
    private TextView nombreEstancia, tvPuntos; // tvUsuario eliminado
    private int puntos;
    private int vidas = 3;
    private int nivelVocabulario; // Nivel de vocabulario
    private MediaPlayer sonidoNivelCompletado;
    private MediaPlayer sonidoError;
    private String respuestaCorrecta;
    private int indiceEstanciaActual = 0;
    private ImageButton[] vidasBotones;
    private String[] estancias = {"Kitchen", "Dining Room", "Double bedroom", "Bedroom", "Bathroom"};
    private int[] imagenesConBordeVerde = {
            R.drawable.kitchenverde, R.drawable.diningverde, R.drawable.room1verde, R.drawable.bathroomverde, R.drawable.room2verde};
    private int[] imagenesConBordeRojo = {
            R.drawable.kitchenrojo, R.drawable.diningrojo, R.drawable.room1rojo, R.drawable.room2rojo, R.drawable.bathroomrojo};
    private UsuariosDAO usuariosDAO;
    private Usuario usuario;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulario5);


        sonidoNivelCompletado = MediaPlayer.create(this, R.raw.nivel_completado);
        sonidoError = MediaPlayer.create(this, R.raw.fallo);

        if (sonidoNivelCompletado == null || sonidoError == null) {
            Toast.makeText(this, "Error al cargar sonidos", Toast.LENGTH_SHORT).show();
        }

        usuariosDAO = new UsuariosDAO(this);
        inicializarElementos();
        cargarDatosUsuario();
        generarPregunta();
    }

    public void inicializarElementos() {
        imagenEstancia = findViewById(R.id.imageViewNumero);
        nombreEstancia = findViewById(R.id.textView5);
        tvPuntos = findViewById(R.id.tvPuntos);
        ivAvatar = findViewById(R.id.iv_avatar);
        vidasBotones = new ImageButton[]{
                findViewById(R.id.imageButton), // IDs actualizados
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

    public void generarPregunta() {
        respuestaCorrecta = estancias[indiceEstanciaActual];
        nombreEstancia.setText(respuestaCorrecta);
        imagenEstancia.setImageResource(R.drawable.casa);
    }

    public void verificarRespuesta(View view) {
        int id = view.getId();
        int opcionSeleccionada = -1;

        if (id == R.id.botonKitchen) {
            opcionSeleccionada = 0;
        } else if (id == R.id.botonDiningRoom) {
            opcionSeleccionada = 1;
        } else if (id == R.id.botonroom1) {
            opcionSeleccionada = 2;
        } else if (id == R.id.botonRoom2) {
            opcionSeleccionada = 4;
        } else if (id == R.id.botonBathroom) {
            opcionSeleccionada = 3;
        }

        if (respuestaCorrecta.equalsIgnoreCase(estancias[opcionSeleccionada])) {
            puntos += 3;
            imagenEstancia.setImageResource(imagenesConBordeVerde[opcionSeleccionada]);
            Toast.makeText(this, "¡Correcto! Puntos: " + puntos, Toast.LENGTH_SHORT).show();

            indiceEstanciaActual++;

            if (indiceEstanciaActual >= estancias.length) {
                // Juego completado para este nivel de vocabulario
                Toast.makeText(this, "¡Nivel de vocabulario completado! Puntos finales: " + puntos, Toast.LENGTH_SHORT).show();

                // Actualizar nivelVocabulario *solo si es necesario*
                if (nivelVocabulario < 6) { // Asumiendo 6 niveles máximos de vocabulario
                    nivelVocabulario++;
                    usuario.setNivelVocabulario(nivelVocabulario);
                    usuariosDAO.actualizarUsuario(usuario);

                    if (sonidoNivelCompletado != null) {
                        sonidoNivelCompletado.start();
                    }
                }

                // Volver al menú (o a la pantalla de niveles)
                Intent intent = new Intent(this, Menu.class); // O Niveles.class si quieres volver a la pantalla de niveles
                intent.putExtra("idUsuario", usuario.getId());
                startActivity(intent);
                finish();

            } else {
                generarPregunta(); // Generar nueva pregunta para la siguiente estancia
            }

        } else {
            puntos -= 3;
            vidas--;
            if (sonidoError != null) {
                sonidoError.start();
            }
            imagenEstancia.setImageResource(imagenesConBordeRojo[opcionSeleccionada]);
            Toast.makeText(this, "¡Incorrecto! Puntos: " + puntos + " Vidas restantes: " + vidas, Toast.LENGTH_SHORT).show();
            actualizarVidas();

            if (vidas <= 0) {
                // Juego terminado por falta de vidas
                Toast.makeText(this, "¡Juego terminado! Puntos finales: " + puntos, Toast.LENGTH_SHORT).show();

                // Guardar puntos y vidas antes de finalizar
                usuario.setPuntuacion(puntos);
                usuario.setVidas(vidas);
                usuariosDAO.actualizarUsuario(usuario);

                finish();
            }
        }

        actualizarPuntos();
        guardarEstadoJuego();
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
        intent.putExtra("idUsuario", usuario.getId());
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

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.englishapp.DAO.UsuariosDAO;
import com.example.englishapp.R;
import com.example.englishapp.Usuario;

public class Vocabulario5 extends AppCompatActivity {

    private ImageView imagenEstancia, ivAvatar;
    private TextView nombreEstancia, tvPuntos, tvUsuario;
    private int puntos;
    private int vidas = 3;
    private int nivelCompletado;
    private String respuestaCorrecta;
    private int indiceEstanciaActual = 0;
    private ImageButton[] botonVidas;
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


        usuariosDAO = new UsuariosDAO(this); // Inicializar UsuariosDAO
        inicializarElementos();
        cargarDatosUsuario(); // Cargar datos del usuario
        generarPregunta();
    }

    public void inicializarElementos() {
        imagenEstancia = findViewById(R.id.imageViewNumero);
        nombreEstancia = findViewById(R.id.textView5);
        tvPuntos = findViewById(R.id.tvPuntos);
        tvUsuario = findViewById(R.id.textView7);
        ivAvatar = findViewById(R.id.iv_avatar);
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

    public void generarPregunta() {
        respuestaCorrecta = estancias[indiceEstanciaActual];
        nombreEstancia.setText(respuestaCorrecta);
        imagenEstancia.setImageResource(R.drawable.casa); // Imagen por defecto
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
        } else if (id == R.id.botonBathroom) {
            opcionSeleccionada = 4; // Corregido el índice
        } else if (id == R.id.botonRoom2) {
            opcionSeleccionada = 3; // Corregido el índice
        }

        if (respuestaCorrecta.equalsIgnoreCase(estancias[opcionSeleccionada])) {
            puntos += 3;
            imagenEstancia.setImageResource(imagenesConBordeVerde[opcionSeleccionada]);
            Toast.makeText(this, "¡Correcto! Puntos: " + puntos, Toast.LENGTH_SHORT).show();
        } else {
            puntos -= 3;
            vidas--;
            imagenEstancia.setImageResource(imagenesConBordeRojo[opcionSeleccionada]);
            Toast.makeText(this, "¡Incorrecto! Puntos: " + puntos + " Vidas restantes: " + vidas, Toast.LENGTH_SHORT).show();
            actualizarVidas();
        }

        actualizarPuntos();

        if (vidas <= 0) {
            Toast.makeText(this, "¡Juego terminado! Puntos finales: " + puntos, Toast.LENGTH_SHORT).show();
            // Guardar puntos y vidas antes de finalizar
            usuario.setPuntuacion(puntos);
            usuario.setVidas(vidas);
            usuariosDAO.actualizarUsuario(usuario);
            finish();
        } else {
            indiceEstanciaActual++;
            if (indiceEstanciaActual >= estancias.length) {
                indiceEstanciaActual = 0;
                nivelCompletado++; // Incrementar nivel completado
                usuario.setNivelCompletado(nivelCompletado); // Guardar nivel completado
                usuariosDAO.actualizarUsuario(usuario); // Actualizar usuario en la base de datos
                Toast.makeText(this, "¡Nivel completado! Puntos finales: " + puntos, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, Menu.class);
                intent.putExtra("idUsuario", usuario.getId());
                startActivity(intent);
                finish();
            }
            generarPregunta();
        }
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
}
package com.example.englishapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
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
import com.example.englishapp.Gramatica.Gramatica1;
import com.example.englishapp.Gramatica.Gramatica2;
import com.example.englishapp.Gramatica.Gramatica3;
import com.example.englishapp.Gramatica.Gramatica4;
import com.example.englishapp.Gramatica.Gramatica5;
import com.example.englishapp.Reading.Reading1;
import com.example.englishapp.Reading.Reading2;
import com.example.englishapp.Vocabulario.Vocabulario1;
import com.example.englishapp.Vocabulario.Vocabulario2;
import com.example.englishapp.Vocabulario.Vocabulario3;
import com.example.englishapp.Vocabulario.Vocabulario4;
import com.example.englishapp.Vocabulario.Vocabulario5;
import com.example.englishapp.Vocabulario.Vocabulario6;


public class Niveles extends AppCompatActivity {

    private ImageButton[] botonesNivel = new ImageButton[6]; // Array para los botones de nivel
    private int puntuacion, vidas, avatar, idUsuario; // Incluir idUsuario
    private String nombreUsuario;
    private TextView tvNombreUsuario, tvPuntuacion;
    private ImageView ivAvatar, vida1, vida2, vida3;
    private UsuariosDAO usuariosDAO;
    private MediaPlayer sonidoSubeNivel,sonidoNivelCompletado;
    private int nivelVocabulario, nivelGramatica, nivelReading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_niveles);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sonidoSubeNivel = MediaPlayer.create(this, R.raw.sube_nivel);
        sonidoNivelCompletado = MediaPlayer.create(this, R.raw.nivel_completado);

        if (sonidoSubeNivel == null || sonidoNivelCompletado == null) {
            Toast.makeText(this, "Error al cargar sonidos", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        usuariosDAO = new UsuariosDAO(this);
        inicializarVariables();
        cargarDatosUsuario();
        actualizarNiveles();
        actualizarVidas();
    }

    public void cargarDatosUsuario() {
        idUsuario = getIntent().getIntExtra("idUsuario", -1); // Obtener idUsuario del Intent

        if (idUsuario != -1) {
            Usuario usuario = usuariosDAO.obtenerUsuarioPorId(idUsuario); // Obtener usuario de la BD

            if (usuario != null) {
                nombreUsuario = usuario.getNombre();
                puntuacion = usuario.getPuntuacion();
                vidas = usuario.getVidas();
                avatar = usuario.getAvatar();

                tvNombreUsuario.setText(nombreUsuario);
                tvPuntuacion.setText(String.valueOf(puntuacion));
                establecerImagenAvatar(avatar);
                actualizarVidas();
            } else {
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "ID de usuario no encontrado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void establecerImagenAvatar(int avatar) {
        switch (avatar) {
            case 0:
                ivAvatar.setImageResource(R.drawable.spiderman);
                break;
            case 1:
                ivAvatar.setImageResource(R.drawable.capitanamerica);
                break;
            case 2:
                ivAvatar.setImageResource(R.drawable.batman);
                break;
            case 3:
                ivAvatar.setImageResource(R.drawable.hulk);
                break;
            default:
                Toast.makeText(this, R.string.selecciona_avatar, Toast.LENGTH_SHORT).show(); // Usar strings.xml
        }
    }

    public void inicializarVariables() {
        botonesNivel[0] = findViewById(R.id.bt_uno);
        botonesNivel[1] = findViewById(R.id.bt_dos);
        botonesNivel[2] = findViewById(R.id.bt_tres);
        botonesNivel[3] = findViewById(R.id.bt_cuatro);
        botonesNivel[4] = findViewById(R.id.bt_cinco);
        botonesNivel[5] = findViewById(R.id.bt_seis);

        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        tvPuntuacion = findViewById(R.id.tvPuntuacion);
        ivAvatar = findViewById(R.id.ivAvatar);

        vida1 = findViewById(R.id.vida1);
        vida2 = findViewById(R.id.vida2);
        vida3 = findViewById(R.id.vida3);

        sonidoSubeNivel = MediaPlayer.create(this, R.raw.sube_nivel);
        if (sonidoSubeNivel == null) {
            Toast.makeText(this, "Error al cargar el sonidos", Toast.LENGTH_SHORT).show();
        }

    }

    public void actualizarNiveles() {
        Usuario usuario = usuariosDAO.obtenerUsuarioPorId(idUsuario);
        if (usuario != null) {
            nivelVocabulario = usuario.getNivelVocabulario();
            nivelGramatica = usuario.getNivelGramatica();
            nivelReading = usuario.getNivelReading();
        } else {
            nivelVocabulario = 1; // Valores predeterminados si el usuario es null
            nivelGramatica = 1;
            nivelReading = 1;
        }

        String tipoNivel = getIntent().getStringExtra("tipo_nivel");
        int nivelMaximo = 0;

        if (tipoNivel != null) {
            switch (tipoNivel) {
                case "vocabulary":
                    nivelMaximo = nivelVocabulario;
                    break;
                case "grammar":
                    nivelMaximo = nivelGramatica;
                    break;
                case "reading":
                    nivelMaximo = nivelReading;
                    break;
            }
        }

        for (int i = 0; i < botonesNivel.length; i++) {
            actualizarNivel(botonesNivel[i], i + 1, nivelMaximo);
        }
    }


    public void actualizarNivel(ImageButton boton, int nivel, int nivelMaximo) {
        boolean habilitado = nivelMaximo >= nivel - 1;
        boton.setEnabled(habilitado);

        ImageView candado = null;

        switch (nivel) {
            case 2:
                candado = boton.findViewById(R.id.c2);
                break;
            case 3:
                candado = boton.findViewById(R.id.c3);
                break;
            case 4:
                candado = boton.findViewById(R.id.c4);
                break;
            case 5:
                candado = boton.findViewById(R.id.c5);
                break;
            case 6:
                candado = boton.findViewById(R.id.c6);
                break;
        }

        if (candado != null) {
            if (habilitado) {
                boton.clearColorFilter();
                candado.setVisibility(View.INVISIBLE);
                if (sonidoSubeNivel != null) {
                    sonidoSubeNivel.start();
                }
            } else {
                boton.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                candado.setVisibility(View.VISIBLE);
            }
        }
    }


    public void actualizarVidas() {
        vida1.setVisibility(vidas >= 1 ? View.VISIBLE : View.INVISIBLE);
        vida2.setVisibility(vidas >= 2 ? View.VISIBLE : View.INVISIBLE);
        vida3.setVisibility(vidas >= 3 ? View.VISIBLE : View.INVISIBLE);
    }

    public void iniciarNivel(View view) {
        Intent intent = null;
        int nivel = -1;
        String tipoNivel = getIntent().getStringExtra("tipo_nivel");

        if (view != null) {
            int viewId = view.getId();

            for (int i = 0; i < botonesNivel.length; i++) {
                if (viewId == botonesNivel[i].getId()) {
                    nivel = i + 1;
                    break;
                }
            }

            if (nivel != -1 && tipoNivel != null) {
                switch (tipoNivel) {
                    case "reading":
                        switch (nivel) {
                            case 1:
                                intent = new Intent(Niveles.this, Reading1.class);
                                break;
                            case 2:
                                intent = new Intent(Niveles.this, Reading2.class);
                                break;
                        }
                        break;
                    case "grammar":
                        switch (nivel) {
                            case 1:
                                intent = new Intent(Niveles.this, Gramatica1.class);
                                break;
                            case 2:
                                intent = new Intent(Niveles.this, Gramatica2.class);
                                break;
                            case 3:
                                intent = new Intent(Niveles.this, Gramatica3.class);
                                break;
                            case 4:
                                intent = new Intent(Niveles.this, Gramatica4.class);
                                break;
                            case 5:
                                intent = new Intent(Niveles.this, Gramatica5.class);
                                break;
                        }
                        break;
                    case "vocabulary":
                        switch (nivel) {
                            case 1:
                                intent = new Intent(Niveles.this, Vocabulario1.class);
                                break;
                            case 2:
                                intent = new Intent(Niveles.this, Vocabulario2.class);
                                break;
                            case 3:
                                intent = new Intent(Niveles.this, Vocabulario3.class);
                                break;
                            case 4:
                                intent = new Intent(Niveles.this, Vocabulario4.class);
                                break;
                            case 5:
                                intent = new Intent(Niveles.this, Vocabulario5.class);
                                break;
                            case 6:
                                intent = new Intent(Niveles.this, Vocabulario6.class);
                                break;
                        }
                        break;
                }
            } else {
                Toast.makeText(this, R.string.selecciona_nivel, Toast.LENGTH_SHORT).show();
                return;
            }

            if (intent != null) {
                intent.putExtra("idUsuario", idUsuario);
                intent.putExtra("nivel", nivel);
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.selecciona_nivel, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void back(View view) {
        Intent intent = new Intent(this, Menu.class);
        intent.putExtra("idUsuario", idUsuario);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sonidoSubeNivel != null) {
            sonidoSubeNivel.release();
            sonidoSubeNivel = null;
        }
    }


}
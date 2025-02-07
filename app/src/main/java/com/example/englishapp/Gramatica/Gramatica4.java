package com.example.englishapp.Gramatica;

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
import com.example.englishapp.R;
import com.example.englishapp.Usuario;

import java.util.HashMap;
import java.util.Map;

public class Gramatica4 extends AppCompatActivity {

    private TextView tvPuntos, tvFrase;
    private EditText etRespuesta;
    private ImageButton[] vidas;
    private ImageButton btBack, btOk;
    private ImageView ivAvatar;
    private int puntos = 0;
    private int vidasRestantes = 3;
    private UsuariosDAO usuariosDAO;
    private Usuario usuario;

    private Map<String, String> verbos = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gramatica4);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usuariosDAO = new UsuariosDAO(this);

        inicializarVariables();
        cargarDatosUsuario();
        cargarVerbos();
        cargarFrase();
    }

    private void cargarDatosUsuario() {
        Intent intent = getIntent();
        int idUsuario = intent.getIntExtra("idUsuario", -1);

        if (idUsuario != -1) {
            usuario = usuariosDAO.obtenerUsuarioPorId(idUsuario);
            if (usuario != null) {
                puntos = usuario.getPuntuacion();
                vidasRestantes = usuario.getVidas();
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
        tvPuntos.setText(String.valueOf(puntos));
    }

    private void actualizarVidas() {
        for (int i = 0; i < vidas.length; i++) {
            vidas[i].setVisibility(i < vidasRestantes ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void inicializarVariables() {
        tvPuntos = findViewById(R.id.tv_puntos);
        tvFrase = findViewById(R.id.tv_frase);
        etRespuesta = findViewById(R.id.et_respuesta);
        vidas = new ImageButton[]{
                findViewById(R.id.imageButton7),
                findViewById(R.id.imageButton8),
                findViewById(R.id.imageButton9)
        };
        btBack = findViewById(R.id.bt_back);
        btOk = findViewById(R.id.bt_ok);
        ivAvatar = findViewById(R.id.iv_avatar);

        btBack.setOnClickListener(v -> botonBack());
        btOk.setOnClickListener(v -> verificarRespuesta());

        // ... (código para cargar el avatar, similar a Vocabulario2)
    }

    private void cargarVerbos() {
        verbos.put("go", "went");
        verbos.put("read", "read");
        verbos.put("play", "played");
        verbos.put("eat", "ate");
        verbos.put("travel", "traveled");
    }

    private void cargarFrase() {
        String[] presentes = verbos.keySet().toArray(new String[0]);
        int indice = (int) (Math.random() * presentes.length);
        String presente = presentes[indice];
        String frase = "I " + presente + " to the store yesterday.";

        tvFrase.setText(frase);
        etRespuesta.setText("");
    }

    private void verificarRespuesta() {
        String respuestaUsuario = etRespuesta.getText().toString().trim().toLowerCase();
        String presente = tvFrase.getText().toString().split(" ")[1];
        String pasadoCorrecto = verbos.get(presente);

        if (respuestaUsuario.equals(pasadoCorrecto)) {
            puntos += 3;
            actualizarPuntos();
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
            cargarFrase();
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

    public void botonBack() {
        Intent intent = new Intent(this, Menu.class);
        intent.putExtra("idUsuario", usuario.getId());
        startActivity(intent);
        finish();
    }
}
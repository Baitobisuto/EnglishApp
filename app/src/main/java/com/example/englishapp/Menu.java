package com.example.englishapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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


public class Menu extends AppCompatActivity {

    private TextView tvUsuario, tvPuntos;
    private ImageView ivAvatar;
    private ImageView[] vidas = new ImageView[3];
    private int idUsuario; // ID del usuario
    private int avatar;
    private int puntuacion;
    private int vidasUsuario;
    private UsuariosDAO usuariosDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        usuariosDAO = new UsuariosDAO(this);
        inicializarVariables();
        obtenerDatosUsuario();

    }

    public void obtenerDatosUsuario() {
        idUsuario = getIntent().getIntExtra("idUsuario", -1);

        if (idUsuario != -1) {
            Usuario usuario = usuariosDAO.obtenerUsuarioPorId(idUsuario);

            if (usuario != null) {
                avatar = usuario.getAvatar();
                puntuacion = usuario.getPuntuacion();
                vidasUsuario = usuario.getVidas();

                // Actualizar la interfaz de usuario (solo avatar, puntuación y vidas)
                tvPuntos.setText(String.valueOf(puntuacion));
                establecerImagenAvatar(avatar);
                mostrarVidas(vidasUsuario);
            } else {
                Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "ID de usuario no encontrado", Toast.LENGTH_SHORT).show();
        }
    }


    public void establecerImagenAvatar(int avatar) {
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
                Toast.makeText(this, "No se ha encontrado ningún avatar", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    public void inicializarVariables() {
        tvUsuario = findViewById(R.id.textView);
        ivAvatar = findViewById(R.id.ivAvatar);
        tvPuntos = findViewById(R.id.tvPuntos);


        vidas[0] = findViewById(R.id.imageButton3);
        vidas[1] = findViewById(R.id.imageButton4);
        vidas[2] = findViewById(R.id.imageButton2);
    }


    public void mostrarVidas(int vidasUsuario) {
        for (int i = 0; i < vidas.length; i++) {
            vidas[i].setVisibility(i < vidasUsuario ? View.VISIBLE : View.INVISIBLE);
        }
    }



    public void botonReading(View view) {
        Intent intent = new Intent(Menu.this, Niveles.class);
        intent.putExtra("idUsuario", idUsuario);
        intent.putExtra("tipo_nivel", "reading"); // Pasar el tipo de nivel
        startActivity(intent);
    }

    public void botonGrammar(View view) {
        Intent intent = new Intent(Menu.this, Niveles.class);
        intent.putExtra("idUsuario", idUsuario);
        intent.putExtra("tipo_nivel", "grammar"); // Pasar el tipo de nivel
        startActivity(intent);
    }

    public void botonVocabulary(View view) {
        Intent intent = new Intent(Menu.this, Niveles.class);
        intent.putExtra("idUsuario", idUsuario);
        intent.putExtra("tipo_nivel", "vocabulary"); // Pasar el tipo de nivel
        startActivity(intent);
    }
}
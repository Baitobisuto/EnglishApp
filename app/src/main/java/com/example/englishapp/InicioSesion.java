package com.example.englishapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.englishapp.DAO.UsuariosDAO;
import com.jcraft.jsch.jbcrypt.BCrypt;

public class InicioSesion extends AppCompatActivity {

    private EditText usuario, password;
    private UsuariosDAO usuariosDAO;
    private MediaPlayer sonidoInicioSesion;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);
        inicializarVariables();
        usuariosDAO = new UsuariosDAO(this);

        sonidoInicioSesion = MediaPlayer.create(this, R.raw.inicio_sesion);
        if(sonidoInicioSesion == null){
            Toast.makeText(this,"no se pudo cargar el sonido",Toast.LENGTH_SHORT).show();
        }
    }

    public void inicializarVariables() {
        usuario = findViewById(R.id.et_user);
        password = findViewById(R.id.et_pass);
    }

    public void login(View view) {
        String username = usuario.getText().toString().trim();
        String passwd = password.getText().toString().trim();

        if (username.isEmpty() || passwd.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa el nombre de usuario y la contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        Usuario usuarioObj = usuariosDAO.obtenerUsuario(username);

        if (usuarioObj == null) {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        String passwordHasheadaAlmacenada = usuarioObj.getPassword();

        System.out.println("Contraseña ingresada (sin hashear): " + passwd);
        System.out.println("Contraseña hasheada almacenada: " + passwordHasheadaAlmacenada);

        try {
            if (BCrypt.checkpw(passwd, passwordHasheadaAlmacenada)) {
                // Contraseña correcta
                sonidoInicioSesion.start();
                Intent intent = new Intent(InicioSesion.this, Menu.class);
                intent.putExtra("idUsuario", usuarioObj.getId());
                intent.putExtra("nombreUsuario", usuarioObj.getUsername());
                intent.putExtra("puntuacion", usuarioObj.getPuntuacion());
                intent.putExtra("vidas", usuarioObj.getVidas());
                intent.putExtra("avatar", usuarioObj.getAvatar());
                startActivity(intent);
                finish();
            } else {
                // Contraseña incorrecta
                Toast.makeText(this, R.string.credenciales_incorrectas, Toast.LENGTH_SHORT).show();
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace(); // Imprime el error para depuración
            Toast.makeText(this, "Error en la verificación de la contraseña", Toast.LENGTH_SHORT).show(); // Mensaje genérico al usuario
        }
    }



    public void registro(View view) {
        Intent intent = new Intent(InicioSesion.this, RegistroUsuario.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (sonidoInicioSesion != null) {
            sonidoInicioSesion.release();
            sonidoInicioSesion = null;
        }
    }
}
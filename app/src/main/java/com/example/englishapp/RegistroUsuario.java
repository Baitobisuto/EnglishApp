package com.example.englishapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.englishapp.DAO.UsuariosDAO;


public class RegistroUsuario extends AppCompatActivity {

    private EditText etNom, etApe, etUser, etPass, etPass2;
    private ImageButton[] avatares;
    private int avatarSeleccionado = -1;
    private UsuariosDAO usuariosDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_usuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inicializarVariables();
        inicializarAvatares();
        usuariosDAO = new UsuariosDAO(this);
    }

    public void crearTablas(View view){
         usuariosDAO.createUsuariosTable();
    }

    public void inicializarVariables() {
        etNom = findViewById(R.id.etNombre);
        etApe = findViewById(R.id.etApellidos);
        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.et_Pass);
        etPass2 = findViewById(R.id.et_Pass2);
    }

    private void inicializarAvatares() {
        avatares = new ImageButton[]{
                findViewById(R.id.imageButton30),//Batman
                findViewById(R.id.imageButton31),// Hulk
                findViewById(R.id.imageButton32),// Capitán América
                findViewById(R.id.imageButton33) // Thor
        };

        for (int i = 0; i < avatares.length; i++) {
            final int indice = i;
            avatares[i].setOnClickListener(v -> {
                if (avatarSeleccionado != -1) {
                    avatares[avatarSeleccionado].setBackgroundColor(Color.GRAY);
                }

                avatarSeleccionado = indice;
                avatares[avatarSeleccionado].setBackgroundColor(Color.RED);
            });
        }
    }

    public void registroUsuario(View view) {
        String name = etNom.getText().toString().trim();
        String lastname = etApe.getText().toString().trim();
        String username = etUser.getText().toString().trim();
        String password = etPass.getText().toString().trim();
        String password2 = etPass2.getText().toString().trim(); // Obtener la confirmación de contraseña

        if (name.isEmpty() || lastname.isEmpty() || username.isEmpty() || password.isEmpty() || password2.isEmpty()) {
            Toast.makeText(this, R.string.error_campos_vacios, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(password2)) { // Validar que las contraseñas coincidan
            Toast.makeText(this, R.string.contraseñas_no_coinciden, Toast.LENGTH_SHORT).show();
            return;
        }

        if (usuariosDAO.usuarioExiste(username)) {
            Toast.makeText(this, R.string.usuario_ya_existe, Toast.LENGTH_SHORT).show();
            return;
        }

        if (avatarSeleccionado == -1) {
            Toast.makeText(this, R.string.selecciona_avatar, Toast.LENGTH_SHORT).show();
            return;
        }

        boolean okCreate = usuariosDAO.registrarUsuario(username, password, name, lastname, avatarSeleccionado);
        if (okCreate) {
            Toast.makeText(this, R.string.registro_exitoso, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegistroUsuario.this, InicioSesion.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, R.string.error_registro, Toast.LENGTH_SHORT).show();
        }
    }

    public void botonExit(View view) {
        Intent intent = new Intent(RegistroUsuario.this, InicioSesion.class);
        startActivity(intent);
    }
}
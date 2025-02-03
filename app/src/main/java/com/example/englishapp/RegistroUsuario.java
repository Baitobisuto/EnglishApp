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

import java.sql.Connection;

public class RegistroUsuario extends AppCompatActivity {

    private EditText etNom, etApe, etUser, etPass, etPass2;
    private ConexionMethods conexion;
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
        conexion = new ConexionMethods();
        inicializarAvatares();
        usuariosDAO = new UsuariosDAO();
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
                findViewById(R.id.imageButton30),
                findViewById(R.id.imageButton31),
                findViewById(R.id.imageButton32),
                findViewById(R.id.imageButton33)
        };

        for (int i = 0; i < avatares.length; i++) {
            final int indice = i; // Necesario para usar 'i' dentro de la clase interna

            avatares[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Restablecer el color de fondo del avatar previamente seleccionado (si hay uno)
                    if (avatarSeleccionado != -1) {
                        avatares[avatarSeleccionado].setBackgroundColor(Color.TRANSPARENT);
                    }

                    avatarSeleccionado = indice;
                    // Resaltar el avatar seleccionado (ejemplo: cambiar el color de fondo)
                    avatares[avatarSeleccionado].setBackgroundColor(Color.LTGRAY);

                    Toast.makeText(RegistroUsuario.this, "Avatar " + (indice + 1) + " seleccionado", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void registroUsuario(View view) {
        String name = etNom.getText().toString();
        String lastname = etApe.getText().toString();
        String username = etUser.getText().toString();
        String password = etPass.getText().toString();
        String passwd2 = etPass2.getText().toString();

        if (name.isEmpty() || password.isEmpty() || passwd2.isEmpty() || lastname.isEmpty() || username.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(passwd2)) {
            Toast.makeText(this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (usuariosDAO.usuarioExiste(username)) {
            Toast.makeText(this, "El usuario ya existe.", Toast.LENGTH_SHORT).show();
        } else {
            boolean okCreate = usuariosDAO.registrarUsuario(name, lastname, username, password);
            if (okCreate) {
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegistroUsuario.this, InicioSesion.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Error al registrar el usuario.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

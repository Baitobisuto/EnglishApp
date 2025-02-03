package com.example.englishapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.bumptech.glide.Glide;

public class InicioSesion extends AppCompatActivity {

    private EditText usuario, password;
    private ImageView gif;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio_sesion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        inicializarVariables();

        //gif = findViewById(R.id.imageGif);
        //Glide.with(this)
        //      .asGif() // especifico que es un GIF
        //      .load(R.drawable.spidermangif) // Indico la ruta
        //      .into(gif); // Lo cargo en el ImageView
    }

    public void inicializarVariables() {
        usuario = findViewById(R.id.et_user);
        password = findViewById(R.id.et_pass);


    }


    public void login(View view) {
        String username = usuario.getText().toString();
        String passwd = password.getText().toString();

        if (username.isEmpty() || passwd.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa el nombre de usuario y la contraseña", Toast.LENGTH_SHORT).show();
        } else {
            if (username.equals(usuario) && passwd.equals(password)) {
                Intent intent = new Intent(InicioSesion.this, Menu.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void registro(View view) {
        Intent intent = new Intent(InicioSesion.this, RegistroUsuario.class);
        startActivity(intent);
    }
}
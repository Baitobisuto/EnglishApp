package com.example.englishapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Menu extends AppCompatActivity {

    private ImageButton bt_exit,bt1, bt2, bt3;

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
        inicializarVariables();
    }

    public void inicializarVariables() {
        bt1 = findViewById(R.id.bt_vocabulary);
        bt2 = findViewById(R.id.bt_grammar);
        bt3 = findViewById(R.id.bt_reading);
        bt_exit = findViewById(R.id.bt_back);
    }

    public void botonReading(View view) {
        //Intent intent = new Intent(Menu.this,ReadingActivity.class);
        //startActivity(intent);
    }

    public void botonGrammar(View view) {
        //Intent intent = new Intent(Menu.this,GrammarActivity.class);
        //startActivity(intent);
    }

    public void botonVocabulary(View view) {
        Intent intent = new Intent(Menu.this, Niveles.class);
        startActivity(intent);
    }

    public void botonExit(View view) {
        Intent intent = new Intent(Menu.this, InicioSesion.class);
        startActivity(intent);
        //finish();
    }
}
package com.nibemi.controluisrael;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PrincipalActivity extends AppCompatActivity {

    Button btn_cerrar;
    Button btn_bloqueo, btn_localizar, btn_ver_imagenes, btn_activar_seguros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        btn_cerrar = (Button) findViewById(R.id.btn_cerrar);

        btn_bloqueo = (Button)findViewById(R.id.btn_bloqueo);
        btn_activar_seguros = (Button)findViewById(R.id.btn_activar_seguros);
        btn_ver_imagenes = (Button)findViewById(R.id.btn_ver_imagenes);
        btn_localizar = (Button)findViewById(R.id.btn_localizar);

        btn_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btn_bloqueo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_activar_seguros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_ver_imagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btn_localizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
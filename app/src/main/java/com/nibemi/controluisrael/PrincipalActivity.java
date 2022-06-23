package com.nibemi.controluisrael;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PrincipalActivity extends AppCompatActivity {

    Button btn_cerrar;
    Button btn_bloqueo, btn_localizar, btn_ver_imagenes, btn_activar_seguros;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefLocacion = database.getReference("locacion");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        btn_cerrar = (Button) findViewById(R.id.btn_cerrar);

        btn_bloqueo = (Button)findViewById(R.id.btn_bloqueo);
        btn_activar_seguros = (Button)findViewById(R.id.btn_activar_seguros);
        btn_ver_imagenes = (Button)findViewById(R.id.btn_ver_imagenes);
        btn_localizar = (Button)findViewById(R.id.btn_localizar);

        final int[] bloqueo = {0};
        final int[] activacion = {0};

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
                DatabaseReference myRef = database.getReference("bloqueo");

                myRef.setValue(bloqueo[0]);

                if (bloqueo[0] == 0){
                    bloqueo[0] = 1;
                }else{
                    bloqueo[0] = 0;
                }
            }
        });

        btn_activar_seguros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference myRef = database.getReference("activacion");

                myRef.setValue(activacion[0]);

                if (activacion[0] == 0){
                    activacion[0] = 1;
                }else{
                    activacion[0] = 0;
                }
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
                myRefLocacion.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.w("DEBUG", "Failed to read value.", error.toException());
                    }
                });
            }
        });
    }
}
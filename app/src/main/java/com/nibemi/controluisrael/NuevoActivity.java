package com.nibemi.controluisrael;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NuevoActivity extends AppCompatActivity {

    TextView txt_nombre_usuario;
    Button btn_opencv;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    int estado = 0;

    DatabaseReference myRefInformacion = database.getReference("informacion");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo);

        txt_nombre_usuario = (TextView) findViewById(R.id.txt_nombre_usuario);
        btn_opencv = (Button) findViewById(R.id.btn_opencv);

        btn_opencv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                if (estado == 0){
                    String usuario = txt_nombre_usuario.getText().toString();

                    if (!usuario.isEmpty()){
                        DatabaseReference myRef = database.getReference("nuevo");
                        DatabaseReference myRefName = database.getReference("nombreUsuario");

                        Toast.makeText(getApplicationContext(), "El proceso iniciará en 10 segundos. Por favor, póngase frente a la cámara del vehículo.", Toast.LENGTH_LONG).show();


                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Iniciando creación de usuario...", Toast.LENGTH_LONG).show();
                                myRefName.setValue(usuario);
                                myRef.setValue(1);

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        myRef.setValue(0);
                                    }
                                }, 5000);
                            }
                        }, 10000);
                    }else{
                        Toast.makeText(getApplicationContext(), "Debe ingresar un nombre de usuario.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Entrenando sistema de reconocimiento.", Toast.LENGTH_LONG).show();
                    DatabaseReference myRefEntrena = database.getReference("entrena");
                    myRefEntrena.setValue(1);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            myRefEntrena.setValue(0);
                            estado = 0;
                        }
                    }, 5000);

                }


            }
        });

        myRefInformacion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                if (!value.isEmpty()){
                    if (value.equals("Iniciar Entrenamiento.")) {
                        btn_opencv.setText("INICIAR ENTRENAMIENTO");
                        estado = 1;
                    }else if (value.equals("Entrenado.")){
                        Toast.makeText(getApplicationContext(), "Usuario creado correctamente.", Toast.LENGTH_LONG).show();
                        finish();
                        Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }
}
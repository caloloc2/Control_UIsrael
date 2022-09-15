package com.nibemi.controluisrael;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class PrincipalActivity extends AppCompatActivity {

    Button btn_cerrar, btn_apagar;
    Button btn_bloqueo, btn_localizar, btn_ver_imagenes, btn_activar_seguros;
    TextView txt_alarma, txt_ahorro;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefLocacion = database.getReference("locacion");
    DatabaseReference myRefAlarma = database.getReference("alarma");
    DatabaseReference myRefAhorro = database.getReference("ahorro");
    DatabaseReference myRefInformacion = database.getReference("informacion");
    private long firstTime=0,secondTime=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        txt_alarma = (TextView)findViewById(R.id.txt_alarma);
        txt_ahorro = (TextView)findViewById(R.id.txt_ahorro);

        btn_cerrar = (Button) findViewById(R.id.btn_cerrar);
        btn_apagar = (Button) findViewById(R.id.btn_apagar);

        btn_bloqueo = (Button)findViewById(R.id.btn_bloqueo);
        btn_activar_seguros = (Button)findViewById(R.id.btn_activar_seguros);
        btn_ver_imagenes = (Button)findViewById(R.id.btn_ver_imagenes);
        btn_localizar = (Button)findViewById(R.id.btn_localizar);

        final int[] bloqueo = {0};
        final int[] activacion = {0};

        btn_apagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference myRef = database.getReference("apagado");
                Toast.makeText(getApplicationContext(), "Apagando equipo...", Toast.LENGTH_LONG).show();
                myRef.setValue(1);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myRef.setValue(0);
                        Toast.makeText(getApplicationContext(), "Equipo Apagado", Toast.LENGTH_LONG).show();
                    }
                }, 5000);
            }
        });

        btn_cerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        btn_bloqueo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getActionMasked();

                if (action == MotionEvent.ACTION_DOWN) {
                    firstTime = System.currentTimeMillis();
                } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    secondTime = System.currentTimeMillis();


                    if(secondTime-firstTime>=3000){ // at least 3000 ms touch down time

                        DatabaseReference myRef = database.getReference("bloqueo");

                        if (bloqueo[0] == 0){
                            Toast.makeText(getApplicationContext(), "Bloqueo Activado", Toast.LENGTH_LONG).show();
                            bloqueo[0] = 1;
                        }else{
                            Toast.makeText(getApplicationContext(), "Bloqueo Desactivado", Toast.LENGTH_LONG).show();
                            bloqueo[0] = 0;
                        }

                        myRef.setValue(bloqueo[0]);
                    }else{ //ignore it}
                        firstTime=0; //reseting the value for the next time
                        secondTime=0;//reseting the value for the next time
                    }
                }
                // TODO Auto-generated method stub
                return false;
            }
        });

        btn_activar_seguros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activacion[0] == 0){
                    activacion[0] = 1;
                    DatabaseReference myRef = database.getReference("activacion");
                    myRef.setValue(1);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            myRef.setValue(0);
                        }
                    }, 3000);
                    btn_activar_seguros.setText("Desactivar Seguros Puertas");
                }else{
                    activacion[0] = 0;
                    DatabaseReference myRef = database.getReference("desactivacion");
                    myRef.setValue(1);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            myRef.setValue(0);
                        }
                    }, 3000);
                    btn_activar_seguros.setText("Activar Seguros Puertas");
                }


            }
        });

        btn_ver_imagenes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(getApplicationContext(), NuevoActivity.class);
                startActivity(intent);
            }
        });

        btn_localizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRefLocacion.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String lat = "";
                        String lng = "";

                        for (DataSnapshot child : snapshot.getChildren()) {
                            String post = child.getValue(String.class);
                            if(post != null){
                                if (lat.isEmpty()){
                                    lat = post;
                                }else if (lng.isEmpty()){
                                    lng = post;
                                }
                            }
                        }
                        Uri gmmIntentUri = Uri.parse("geo:"+lng+","+lat+"?q="+lng+","+lat+"?z=19");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("DEBUG", "Failed to read value.", error.toException());
                    }
                });
//                finish();
//                Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
//                startActivity(intent);
//                myRefLocacion.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        String value = dataSnapshot.getValue(String.class);
//                        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError error) {
//                        Log.w("DEBUG", "Failed to read value.", error.toException());
//                    }
//                });
            }
        });

//        myRefAlarma.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String value = snapshot.getValue(String.class);
//                if (value.equals("1")){
//                    txt_alarma.setText("ALARMA ACTIVADA");
//                }else{
//                    txt_alarma.setText("");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.w("TAG", "Failed to read value.", error.toException());
//            }
//        });

        myRefInformacion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                if (!value.isEmpty()){
                    txt_alarma.setText(value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });

        myRefAhorro.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                if (value.equals("0")){
                    txt_ahorro.setText("ALARMA ACTIVADA");
                }else{
                    txt_ahorro.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }
}
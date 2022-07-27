package com.nibemi.controluisrael;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    Button btn_login;
    TextView txt_username, txt_password;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefLogin = database.getReference("inicio");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_username = (TextView) findViewById(R.id.txt_username);
        txt_password = (TextView) findViewById(R.id.txt_password);
        btn_login = (Button) findViewById(R.id.btn_logueo);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txt_username.getText().toString();
                String password = txt_password.getText().toString();

                if ((!username.isEmpty()) && (!password.isEmpty())){
                    finish();
                    Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(), "Debe especificar un correo y contrasena", Toast.LENGTH_SHORT).show();
                }

            }
        });

        myRefLogin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                if (value.equals("1")){
                    Toast.makeText(getApplicationContext(), "Inicio de sesion con NFC", Toast.LENGTH_LONG).show();
                    finish();
                    Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }
}
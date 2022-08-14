package com.nibemi.controluisrael;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

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

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    Button btn_login, btn_huella;
    TextView txt_username, txt_password;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRefLogin = database.getReference("inicio");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BiometricManager biometricManager = BiometricManager.from(getApplicationContext());
        switch (biometricManager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("DEBUG", "LA APP PUEDE USAR HUELLA COMO LOGIN");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                btn_huella.setVisibility(View.GONE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                btn_huella.setVisibility(View.GONE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                btn_huella.setVisibility(View.GONE);
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(this);

        BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
//                Toast.makeText(getApplicationContext(), "Inicio correctamente con huella", Toast.LENGTH_SHORT).show();
                finish();
                Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
                startActivity(intent);
            }


            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setDescription("Iniciar sesión con huella dactilar")
                .setNegativeButtonText("Cancelar")
                .build();



        txt_username = (TextView) findViewById(R.id.txt_username);
        txt_password = (TextView) findViewById(R.id.txt_password);
        btn_huella = (Button) findViewById(R.id.btn_huella);
        btn_login = (Button) findViewById(R.id.btn_logueo);

        btn_huella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = txt_username.getText().toString();
                String password = txt_password.getText().toString();

                if ((!username.isEmpty()) && (!password.isEmpty())){
                    if (((username.equals("fxzambrano96@gmail.com")) && (password.equals("backinblack316"))) || ((username.equals("megatronica@gmail.com")) && (password.equals("admin")))){
                        finish();
                        Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
                        startActivity(intent);
                    }else {
                        Toast.makeText(getApplicationContext(), "Las credenciales son incorrectas.", Toast.LENGTH_SHORT).show();
                    }
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
package com.example.zivototekrug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Forgot_Password extends AppCompatActivity {
    private EditText editTextEmail;
    private Button reset;
    private ProgressBar progressBar;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editTextEmail = (EditText) findViewById(R.id.email);
        reset = (Button) findViewById(R.id.reset);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }


        });
    }

    private void resetPassword() {
        String email = editTextEmail.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Внесете јa Вашата e-mail адреса.");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Ве молам внесете валидна e-mail адреса.");
            editTextEmail.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(Forgot_Password.this, "Проверете ја вашата е-пошта за да ја ресетирате лозинката.", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(Forgot_Password.this, "Обидете се повторно.", Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}
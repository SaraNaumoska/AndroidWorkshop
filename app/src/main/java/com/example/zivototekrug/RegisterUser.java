package com.example.zivototekrug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterUser extends AppCompatActivity implements View.OnClickListener{
    private TextView banner, registerUser;
    private EditText editTextFullName, editTextPhoneNumber, editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;

    private String stringName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        banner = (TextView) findViewById(R.id.banner);
        banner.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editTextFullName = (EditText) findViewById(R.id.name);
        editTextPhoneNumber = (EditText) findViewById(R.id.phone);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        spinner = (Spinner) findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this, R.array.users, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        stringName = (String) spinner.getSelectedItem().toString();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getBaseContext(), "Избравте: " + parent.getItemAtPosition(position), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.banner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerUser:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String name = editTextFullName.getText().toString().trim();
        String phone = "^[+][0-9]{10,13}$";
        String spin = spinner.getSelectedItem().toString();

        if(name.isEmpty()) {
            editTextFullName.setError("Внесето го Вашето име и презиме.");
            editTextFullName.requestFocus();
            return;
        }
        if(phone.isEmpty()) {
            editTextPhoneNumber.setError("Внесете го Вашиот телефонски број.");
            editTextPhoneNumber.requestFocus();
            return;
        }
//        if(!android.util.Patterns.PHONE.matcher(phone).matches()) {
//            editTextPhoneNumber.setError("Ве молам внесете валиден телефонски број.");
//            editTextPhoneNumber.requestFocus();
//            return;
//        }

        if(email.isEmpty()) {
            editTextEmail.setError("Внесете јa Вашата e-mail адреса.");
            editTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty()) {
            editTextPassword.setError("Внесете ја Вашата лозинка.");
            editTextPassword.requestFocus();
            return;
        }
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Ве молам внесете валидна e-mail адреса.");
            editTextEmail.requestFocus();
            return;
        }
        if(password.length() < 6) {
            editTextPassword.setError("Минималната должина треба да биде 6 карактери.");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            User user = new User(name, email, password, phone, spin, 0, 0);

                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterUser.this, "Лицето е успешно регистрирано.", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                        startActivity(new Intent(RegisterUser.this, MainActivity.class));
                                    }
                                    else {
                                        Toast.makeText(RegisterUser.this, "Регистрацијата е неуспешна, обидете се повторно.", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }
                        else {
                            Toast.makeText(RegisterUser.this, "Регистрацијата е неуспешна, пробајте повторно.", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}
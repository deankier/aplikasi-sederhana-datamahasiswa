package com.aplikasipmo.myapk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button BtnMasuk, BtnPhone;
    TextView BtnDaftar, BtnLupa;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener listener;
    String getEmail, getPassword;
    FirebaseUser user;
    ProgressBar progressBar;

    // Intent
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BtnMasuk = findViewById(R.id.btn_login);
        BtnDaftar = findViewById(R.id.btn_daftar_login);
        email = findViewById(R.id.input_email_login);
        password = findViewById(R.id.input_password_login);
        BtnLupa = findViewById(R.id.btn_lupa_password);
        progressBar = findViewById(R.id.progress_login);
        mAuth = FirebaseAuth.getInstance();
        BtnPhone =findViewById(R.id.btn_w_phone);

        BtnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, PhoneLoginActivity.class));
            }
        });


        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null && user.isEmailVerified()){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    LoginActivity.this.finish();
                }
            }
        };

        BtnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                getEmail = email.getText().toString();
                getPassword = password.getText().toString();

                if (TextUtils.isEmpty(getEmail) || TextUtils.isEmpty(getPassword)){
                    Toast.makeText(LoginActivity.this, "Masukan Email dan Password Dengan benar", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }else {
                    LoginUserAccount();
                }
            }
        });

        BtnLupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, LupaPaswdActivity.class));
            }
        });

        BtnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    private void LoginUserAccount() {
        mAuth.signInWithEmailAndPassword(getEmail, getPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    if (mAuth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                        listener.getClass();
                    }else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                        alert.setTitle("Perikas Email anda Untuk Verifikasi! atau daftar akun jika belum mempunyai akun");
                        alert.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        });
                        alert.create();
                        alert.show();
                    }
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (listener != null){
            mAuth.removeAuthStateListener(listener);
        }
    }

}
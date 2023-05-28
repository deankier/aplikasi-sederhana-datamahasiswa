package com.aplikasipmo.myapk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LupaPaswdActivity extends AppCompatActivity {

    ImageView BtnBack;
    Button BtnKirim;
    FirebaseAuth mAuth;
    ProgressBar LupaProgresBar;
    EditText Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_paswd);

        BtnBack = findViewById(R.id.btn_back_lupa);
        BtnKirim = findViewById(R.id.btn_kirim_lupa);
        Email = findViewById(R.id.input_email_lupa);
        LupaProgresBar = findViewById(R.id.progress_lupa);

        BtnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LupaProgresBar.setVisibility(View.VISIBLE);
                String getEmail = Email.getText().toString();
                if (TextUtils.isEmpty(getEmail)){
                    Toast.makeText(LupaPaswdActivity.this, "Masukan Email dengan benar", Toast.LENGTH_SHORT).show();
                    LupaProgresBar.setVisibility(View.GONE);
                }else {
                    SendResetLink();
                }
            }
        });

        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void SendResetLink() {
        String getEmail = Email.getText().toString();
        mAuth.getInstance().sendPasswordResetEmail(getEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            LupaProgresBar.setVisibility(View.GONE);
                            Toast.makeText(LupaPaswdActivity.this, "Link dikirim ke Email anda", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LupaPaswdActivity.this, LoginActivity.class));
                        }else {
                            LupaProgresBar.setVisibility(View.GONE);
                            Toast.makeText(LupaPaswdActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
package com.aplikasipmo.myapk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    EditText eTextEmail, eTextPassword, eTextPhone;
    Button BtnDaftar;
    FirebaseAuth Auth;
    String getEmail, getPassword, getPhone;
    ProgressBar DaftarProgressBar;
    TextView BtnMasuk, CountryID;
    ImageView BtnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        BtnMasuk = findViewById(R.id.btn_login_daftar);
        BtnBack = findViewById(R.id.btn_back_daftar);
        eTextEmail = findViewById(R.id.input_email_daftar);
        eTextPassword = findViewById(R.id.input_password_daftar);
        BtnDaftar = findViewById(R.id.btn_daftar);
        DaftarProgressBar = findViewById(R.id.progress_daftar);
        Auth = FirebaseAuth.getInstance();
        eTextPassword = findViewById(R.id.input_password_daftar);
        CountryID = findViewById(R.id.country_id);
        eTextPhone = findViewById(R.id.input_phone_daftar);

        BtnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaftarProgressBar.setVisibility(View.VISIBLE);
                CekDataUser();
            }
        });

        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        BtnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
                finishActivity(1104);
                finish();
            }
        });
    }

    private void CekDataUser() {
        getEmail = eTextEmail.getText().toString();
        getPassword = eTextPassword.getText().toString();
        getPhone = eTextPhone.getText().toString();

        if (TextUtils.isEmpty(getEmail) || TextUtils.isEmpty(getPassword) || TextUtils.isEmpty(getPhone)){
            Toast.makeText(SignUpActivity.this, "Data Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            DaftarProgressBar.setVisibility(View.GONE);
        }else {
            if (getPassword.length()<4){
                Toast.makeText(SignUpActivity.this, "Password terlalu pendek", Toast.LENGTH_SHORT).show();
                DaftarProgressBar.setVisibility(View.GONE);
            } else if (getPhone.length()<11) {
                Toast.makeText(this, "Masukan nomor telepon yang valid", Toast.LENGTH_SHORT).show();
                DaftarProgressBar.setVisibility(View.GONE);
            } else {
                CreateUserAccount();
            }
        }
    }

    private void AddPhoneToDatabase() {
        String phone = CountryID.getText().toString() + eTextPhone.getText().toString();
        UserPhone userPhone = new UserPhone(phone);
        FirebaseDatabase.getInstance().getReference("UserPhone").child(phone).setValue(userPhone)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ToastSucces();
                onBackPressed();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void CreateUserAccount() {
        Auth.createUserWithEmailAndPassword(getEmail,getPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Pengguna user = new Pengguna(getEmail,getPassword);
                FirebaseDatabase.getInstance().getReference("Pengguna").setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            DaftarProgressBar.setVisibility(View.GONE);
                            Auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        AddPhoneToDatabase();
                                    } else {
                                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else {
                            DaftarProgressBar.setVisibility(View.GONE);
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        DaftarProgressBar.setVisibility(View.GONE);
                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                DaftarProgressBar.setVisibility(View.GONE);
                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ToastSucces(){
        Toast.makeText(SignUpActivity.this, "Berhasil Mendaftar, Periksa Email Anda Untuk Verifikasi ", Toast.LENGTH_LONG).show();
    }


}
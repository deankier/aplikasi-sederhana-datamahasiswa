package com.aplikasipmo.myapk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {

    TextView CountryID, BtnEmailLogin, BtnDaftar;
    Button BtnMasuk;
    EditText EtPhone;
    String getPhone;
    ImageView BtnBack;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        CountryID = findViewById(R.id.country_id);
        BtnEmailLogin = findViewById(R.id.btn_w_email);
        BtnDaftar = findViewById(R.id.btn_daftar_phone);
        progressBar = findViewById(R.id.progress_phone);

        BtnMasuk = findViewById(R.id.btn_login_phone);

        BtnBack = findViewById(R.id.btn_back_phone);

        EtPhone = findViewById(R.id.input_phone);

        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        BtnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhoneLoginActivity.this, SignUpActivity.class));
                finish();
            }
        });

        BtnEmailLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        BtnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhone = EtPhone.getText().toString();
                progressBar.setVisibility(View.VISIBLE);
                if (getPhone.isEmpty()){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(PhoneLoginActivity.this, "Masukan Nomor Telepon", Toast.LENGTH_SHORT).show();
                } else if (getPhone.length()<11) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(PhoneLoginActivity.this, "Masukan Nomor Yang Valid", Toast.LENGTH_SHORT).show();
                }else {
                    CekNomor();
                }
            }
        });


    }

    private void CekNomor() {
        String phone = CountryID.getText().toString()+EtPhone.getText().toString();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserPhone");
        databaseReference.child(phone).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        SendOtp();
                    }else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(PhoneLoginActivity.this, "Nomor Belum Terdaftar", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(PhoneLoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PhoneLoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void SendOtp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+62"+EtPhone.getText().toString(),
                30, TimeUnit.SECONDS, PhoneLoginActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Toast.makeText(PhoneLoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(PhoneLoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        Toast.makeText(PhoneLoginActivity.this, "Otp Terkirim", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PhoneLoginActivity.this, OtpVerifikasiActivity.class);
                        intent.putExtra("phone", "+62"+EtPhone.getText().toString());
                        intent.putExtra("verificationId", verificationId);
                        startActivity(intent);
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

}
package com.aplikasipmo.myapk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class OtpVerifikasiActivity extends AppCompatActivity {

    TextView TvPhone, BtnResend;
    EditText EtOTP;
    Button BtnKirim;
    ImageView back;
    ProgressBar progressBar;
    String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verifikasi);

        back = findViewById(R.id.btn_back_otp);
        BtnResend = findViewById(R.id.btn_resend_otp);
        EtOTP = findViewById(R.id.input_otp);
        BtnKirim = findViewById(R.id.btn_kirim_otp);
        progressBar = findViewById(R.id.progress_otp);

        TvPhone = findViewById(R.id.nomor_tel_otp);
        TvPhone.setText(getIntent().getStringExtra("phone"));

        verificationId = getIntent().getStringExtra("verificationId");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        BtnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getOtp = EtOTP.getText().toString();
                if (getOtp.isEmpty()&&getOtp.length()<6){
                    Toast.makeText(OtpVerifikasiActivity.this, "Masukan OTP dengan benar", Toast.LENGTH_SHORT).show();
                }else {
                    String OTP = EtOTP.getText().toString();
                    if (verificationId != null){
                        progressBar.setVisibility(View.VISIBLE);
                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                                verificationId, OTP
                        );
                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()){
                                    Intent intent = new Intent(OtpVerifikasiActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(OtpVerifikasiActivity.this, "Kode OTP Salah", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });

        BtnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendOtp();
            }
        });

    }
    private void SendOtp() {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(getIntent().getStringExtra("phone"),
                30, TimeUnit.SECONDS, OtpVerifikasiActivity.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(OtpVerifikasiActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCodeSent(@NonNull String NewVerificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        verificationId = NewVerificationId;
                        Toast.makeText(OtpVerifikasiActivity.this, "OTP Terkirim", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }
}
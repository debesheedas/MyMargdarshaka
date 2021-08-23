package com.example.mymargdarshaka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class AuthLogin extends AppCompatActivity {

    com.google.android.material.textfield.TextInputEditText phoneInput;
    Button getOtpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_login);

        phoneInput = (com.google.android.material.textfield.TextInputEditText) findViewById(R.id.phoneInput);
        final ProgressBar pBar=(ProgressBar) findViewById(R.id.spinner);
        getOtpButton = (Button) findViewById(R.id.getOTPButton);

        Log.e("type: ",getIntent().getStringExtra("type"));

        getOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pBar.setVisibility(View.VISIBLE);
                getOtpButton.setVisibility(View.GONE);

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + phoneInput.getText().toString(),
                        60,
                        TimeUnit.SECONDS,
                        AuthLogin.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                pBar.setVisibility(View.GONE);
                                getOtpButton.setVisibility(View.VISIBLE);
                                Toast.makeText(AuthLogin.this,"Phone number verified",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                pBar.setVisibility(View.GONE);
                                getOtpButton.setVisibility(View.VISIBLE);
                                Toast.makeText(AuthLogin.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                pBar.setVisibility(View.GONE);
                                getOtpButton.setVisibility(View.VISIBLE);
                                Intent i=new Intent(AuthLogin.this,AuthOtp.class);
                                i.putExtra("phone",phoneInput.getText().toString());
                                i.putExtra("otp_orig",s);
                                i.putExtra("type",getIntent().getStringExtra("type"));
                                startActivity(i);
                            }
                        }
                );
            }
        });
    }
}
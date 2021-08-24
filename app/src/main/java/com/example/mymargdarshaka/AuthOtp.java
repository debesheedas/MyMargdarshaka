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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class AuthOtp extends AppCompatActivity {

    String otp_original;

    com.google.android.material.textfield.TextInputEditText otpInput;
    Button verify;
    Button resend;

    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "login";
    private static final String TYPE="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_otp);

        otp_original = getIntent().getStringExtra("otp_orig");
        otpInput = (com.google.android.material.textfield.TextInputEditText) findViewById(R.id.otp_input);
        verify = (Button) findViewById(R.id.getOTPButton);
        resend = (Button) findViewById(R.id.resend_otp);
        final ProgressBar pBar2=(ProgressBar) findViewById(R.id.spinner2);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(otp_original==null){
                    Toast.makeText(AuthOtp.this,"Check Your Internet Connection",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AuthOtp.this,otp_original,Toast.LENGTH_LONG).show();
                }
                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(
                        otp_original,otpInput.getText().toString()
                );
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(TYPE,getIntent().getStringExtra("type"));
                            editor.apply();

                            Log.e("Auth otp type: ",getIntent().getStringExtra("type"));

                            Toast.makeText(AuthOtp.this,"Successful",Toast.LENGTH_SHORT).show();

                            Intent i;
                            if(getIntent().getStringExtra("type").equals("student")) {
                                i = new Intent(AuthOtp.this, AuthSignupStudents1.class);
                            }
                            else{
                                i = new Intent(AuthOtp.this, AuthSignupMentors1.class);
                            }
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                        else{
                            Toast.makeText(AuthOtp.this,"There was a problem in authentication",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        resend.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pBar2.setVisibility(View.VISIBLE);
                resend.setVisibility(View.GONE);

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + getIntent().getStringExtra("phone").toString(),
                        60,
                        TimeUnit.SECONDS,
                        AuthOtp.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                pBar2.setVisibility(View.GONE);
                                resend.setVisibility(View.GONE);
                                Toast.makeText(AuthOtp.this,"Phone number verified",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                pBar2.setVisibility(View.GONE);
                                resend.setVisibility(View.GONE);
                                Toast.makeText(AuthOtp.this,e.getMessage(),Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String str, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                otp_original=str;
                                pBar2.setVisibility(View.GONE);
                                resend.setVisibility(View.GONE);
                            }
                        }
                );

            }
        }));

    }
}
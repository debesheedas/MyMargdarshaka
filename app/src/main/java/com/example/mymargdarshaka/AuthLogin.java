package com.example.mymargdarshaka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import com.google.android.gms.tasks.Tasks;

public class AuthLogin extends AppCompatActivity {

    com.google.android.material.textfield.TextInputEditText phoneInput;
    Button getOtpButton;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    boolean fine=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_login);

        phoneInput = (com.google.android.material.textfield.TextInputEditText) findViewById(R.id.phoneInput);
        final ProgressBar pBar=(ProgressBar) findViewById(R.id.spinner);
        getOtpButton = (Button) findViewById(R.id.getOTPButton);

        phoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length()==10){
                    Log.e("helloheoold: ",editable.toString());
                    pBar.setVisibility(View.VISIBLE);
                    getOtpButton.setVisibility(View.GONE);
                    check(pBar);
                }
            }
        });

        getOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pBar.setVisibility(View.VISIBLE);
                getOtpButton.setVisibility(View.GONE);

                generateOtp(pBar);

            }
        });
    }

    public void check(ProgressBar pBar){
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference("users");

        databaseReference.orderByChild("phone").equalTo(phoneInput.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot child: snapshot.getChildren()){
                    pBar.setVisibility(View.VISIBLE);
                    getOtpButton.setVisibility(View.GONE);
                    Log.e("User key", child.getKey());
                    Log.e("User val", child.getValue().toString());

                    if(getIntent().getStringExtra("type").equals("mentor")){
                        fine=false;
                        Log.e("abc:::::::","hello");
                    }
                    pBar.setVisibility(View.GONE);
                    getOtpButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AuthLogin.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                Intent i = new Intent(AuthLogin.this,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        if(fine){
            databaseReference=firebaseDatabase.getReference("mentors");

            databaseReference.orderByChild("phone").equalTo(phoneInput.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot child: snapshot.getChildren()){
                        pBar.setVisibility(View.VISIBLE);
                        getOtpButton.setVisibility(View.GONE);
                        Log.e("User key", child.getKey());
                        Log.e("User val", child.getValue().toString());
                        if(getIntent().getStringExtra("type").equals("student")){
                            Log.e("xyz:::::::","hello");
                            fine=false;
                        }
                        pBar.setVisibility(View.GONE);
                        getOtpButton.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(AuthLogin.this,error.getMessage().toString(),Toast.LENGTH_LONG).show();
                    Intent i = new Intent(AuthLogin.this,MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
            });
        }
    }

    public void generateOtp(ProgressBar pBar){
        if(fine) {

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
                            Toast.makeText(AuthLogin.this, "Phone number verified", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            pBar.setVisibility(View.GONE);
                            getOtpButton.setVisibility(View.VISIBLE);
                            Toast.makeText(AuthLogin.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            pBar.setVisibility(View.GONE);
                            getOtpButton.setVisibility(View.VISIBLE);
                            Intent i = new Intent(AuthLogin.this, AuthOtp.class);
                            i.putExtra("phone", phoneInput.getText().toString());
                            i.putExtra("otp_orig", s);
                            i.putExtra("type", getIntent().getStringExtra("type"));
                            startActivity(i);
                        }
                    }
            );
        }
        else{
            if(getIntent().getStringExtra("type").equals("student")){
                Toast.makeText(AuthLogin.this,"You have previously signed in as a mentor. You cannot sign in as a student.",Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(AuthLogin.this,"You have previously signed in as a student. You cannot sign in as a mentor.",Toast.LENGTH_LONG).show();
            }
            Intent i = new Intent(AuthLogin.this,MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

}
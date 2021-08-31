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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import com.google.android.gms.tasks.Tasks;

public class AuthLogin extends AppCompatActivity {

    private com.google.android.material.textfield.TextInputEditText phoneInput;
    private ProgressBar pBar;
    private Button getOtpButton;

    private DatabaseReference rootRef;

    private boolean fine = true;
    boolean found=false;
    private String userId;

    boolean testSuccessful=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_login);

        // initializing variables
        phoneInput = (com.google.android.material.textfield.TextInputEditText) findViewById(R.id.phoneInput);
        pBar = (ProgressBar) findViewById(R.id.spinner);
        getOtpButton = (Button) findViewById(R.id.getOTPButton);
        rootRef = FirebaseDatabase.getInstance().getReference();

        phoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(editable.toString().length() == 10){
                    setLoading(true);
                    check();
                }
            }
        });

        getOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLoading(true);
                generateOtp();

            }
        });
    }

    void setLoading(boolean b){
        if(b){
            pBar.setVisibility(View.VISIBLE);
            getOtpButton.setVisibility(View.GONE);
        }else{
            pBar.setVisibility(View.GONE);
            getOtpButton.setVisibility(View.VISIBLE);
        }
    }

    // checks if the phone number is in the database and for the right type of user and sets the "fine" variable.
    public void check(){

        setLoading(true);

        // checking in the users
        DatabaseReference usersRef = rootRef.child("users");
        usersRef.orderByChild("phone").equalTo(phoneInput.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long l=snapshot.getChildrenCount();
                long k=0;
                if(l==0){
                    setLoading(false);
                }
                else {
                    setLoading(true);
                    for (DataSnapshot user : snapshot.getChildren()) {
                        Log.e("User key", user.getKey());

                        if (!getIntent().getStringExtra("userType").equals("student")) {
                            fine = false;
                        }
                        else{
                            found=true;
                            userId = user.getKey();
                        }
                        k++;
                        if(k==l) {
                            setLoading(false);
                        }
                    }
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

            DatabaseReference mentorsRef = rootRef.child("mentors");
            mentorsRef.orderByChild("phone").equalTo(phoneInput.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long l=snapshot.getChildrenCount();
                    long k=0;
                    if(l==0){
                        setLoading(false);
                    }
                    else {
                        setLoading(true);
                        for (DataSnapshot child : snapshot.getChildren()) {
                            setLoading(true);
                            Log.e("User key", child.getKey());
                            if (getIntent().getStringExtra("userType").equals("student")) {
                                fine = false;
                            }
                            else{
                                found=true;
                                MentorDetails mentorDetails = child.getValue(MentorDetails.class);
                                userId = child.getKey();
                                if(mentorDetails.getNoTests()==-1){
                                    testSuccessful=true;
                                }else if(mentorDetails.getNoTests() >= 5){
                                    Toast.makeText(AuthLogin.this, "You are not eligible.", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(AuthLogin.this, MainActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i);
                                }
                                else{
                                    testSuccessful=false;
                                }
                            }
                            k++;
                            if(k==l) {
                                setLoading(false);
                            }
                        }
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

    public void generateOtp(){

        if(fine) {
            //PhoneAuthOptions authOptions = new PhoneAuthOptions();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    "+91" + phoneInput.getText().toString(),
                    60,
                    TimeUnit.SECONDS,
                    AuthLogin.this,
                    new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            //pBar.setVisibility(View.GONE);
                            //getOtpButton.setVisibility(View.VISIBLE);
                            //setLoading(false);
                            //Toast.makeText(AuthLogin.this, "Phone number verified", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            setLoading(false);
                            Toast.makeText(AuthLogin.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            Intent i = new Intent(AuthLogin.this, AuthOtp.class);
                            i.putExtra("userType", getIntent().getStringExtra("userType"));
                            i.putExtra("phone", phoneInput.getText().toString());
                            i.putExtra("verificationId", verificationId);
                            i.putExtra("found",found);
                            if(getIntent().getStringExtra("userType").equals("mentor") && found){
                                if(testSuccessful){
                                    i.putExtra("testSuccessful",true);
                                }
                                else{
                                    i.putExtra("testSuccessful",false);
                                }
                            }
                            if(found){
                                i.putExtra("userId", userId);
                            }
                            startActivity(i);
                        }
                    }
            );

        }else{

            if(getIntent().getStringExtra("userType").equals("student")){
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
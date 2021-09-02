package com.example.mymargdarshaka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class AuthOtp extends AppCompatActivity {

  String verificationId;
  com.google.android.material.textfield.TextInputEditText otpInput;
  Button verify, resend;

  SharedPreferences sharedPreferences;

  private DatabaseReference rootRef;

  private static final String SHARED_PREF_NAME = "login";
  private static final String TYPE = "userType";
  private static final String USER_ID = "userId";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_auth_otp);

    rootRef = FirebaseDatabase.getInstance().getReference();

    verificationId = getIntent().getStringExtra("verificationId");
    Log.e("verificationId: ", verificationId);
    otpInput =
            findViewById(R.id.otp_input);
    verify = findViewById(R.id.verifyOTPButton);
    resend = findViewById(R.id.resend_otp);
    final ProgressBar pBar2 = findViewById(R.id.spinner2);
    sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

    Log.e("found: ", String.valueOf(getIntent().getBooleanExtra("found", false)));

    // functionality for verifying the OTP
    verify.setOnClickListener(
            view -> {
              if (verificationId == null) {
                Toast.makeText(AuthOtp.this, "Check Your Internet Connection", Toast.LENGTH_SHORT)
                    .show();
              }
              String otp = "No OTP";
              if (otpInput.getText() != null ){
                  otp = otpInput.getText().toString();
              }
              PhoneAuthCredential phoneAuthCredential =
                  PhoneAuthProvider.getCredential(verificationId, otp);

              // instantiating firebase auth class required for verification
              FirebaseAuth.getInstance()
                  .signInWithCredential(phoneAuthCredential)
                  .addOnCompleteListener(
                          task -> {
                            if (task.isSuccessful()) {
                              // if the authentication is successful
                              if (getIntent().getBooleanExtra("found", false)) {
                                // if the user is found

                                Intent i1;
                                // if student, routing to MyMentors page
                                if (getIntent().getStringExtra("userType").equals("student")) {

                                  SharedPreferences.Editor editor = sharedPreferences.edit();
                                  editor.putString(TYPE, "student");
                                  editor.putString(USER_ID, getIntent().getStringExtra("userId"));
                                  editor.apply();

                                  i1 = new Intent(AuthOtp.this, MyMentors.class);
                                  i1.putExtra("phone", getIntent().getStringExtra("phone"));
                                  i1.putExtra("studentId", getIntent().getStringExtra("userId"));
                                  i1.putExtra("noMentorsAssignedHere", false);
                                  i1.putExtra("firstTime", false);
                                  i1.setFlags(
                                      Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                  startActivity(i1);
                                  return;
                                } else {            // if mentor, routing to MyStudents page

                                  SharedPreferences.Editor editor = sharedPreferences.edit();
                                  editor.putString(TYPE, "mentor");
                                  editor.putString(USER_ID, getIntent().getStringExtra("userId"));
                                  editor.apply();

                                  if (getIntent().getBooleanExtra("testSuccessful", false)) {
                                    i1 = new Intent(AuthOtp.this, MyStudents.class);
                                    i1.putExtra("phone", getIntent().getStringExtra("phone"));
                                    i1.putExtra("mentorId", getIntent().getStringExtra("userId"));
                                    i1.putExtra("noMentorsAssignedHere", false);
                                    i1.putExtra("firstTime", false);
                                    i1.setFlags(
                                        Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i1);
                                  } else {

                                      // if mentor, updating number of attempts in the database
                                      // and routing to Tests page
                                    rootRef
                                        .child("mentors")
                                        .orderByChild(getIntent().getStringExtra("userId"))
                                        .addListenerForSingleValueEvent(
                                            new ValueEventListener() {
                                              @Override
                                              public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot child : snapshot.getChildren()) {
                                                  MentorDetails details =
                                                      child.getValue(MentorDetails.class);
                                                  int noTests = details.getNoTests();
                                                  Log.e("noTests", String.valueOf(noTests));
                                                  rootRef
                                                      .child("mentors")
                                                      .child(child.getKey())
                                                      .child("noTests")
                                                      .setValue(noTests + 1);
                                                  Intent i2 = new Intent(AuthOtp.this, Test.class);
                                                  i2.putExtra(
                                                      "phone", getIntent().getStringExtra("phone"));
                                                  i2.putExtra(
                                                      "mentorId", getIntent().getStringExtra("userId"));
                                                  i2.putExtra("noMentorsAssignedHere", false);
                                                  i2.putExtra("firstTime", false);
                                                  i2.setFlags(
                                                      Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                          | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                  startActivity(i2);
                                                }
                                              }

                                              @Override
                                              public void onCancelled(@NonNull DatabaseError error) {}
                                            });
                                  }
                                  return;
                                }
                              }
                              // if the user is not found in DB, send him to signup pages

                              Toast.makeText(AuthOtp.this, "Successful", Toast.LENGTH_SHORT).show();

                              Intent i;
                              if (getIntent().getStringExtra("userType").equals("student"))
                                i = new Intent(AuthOtp.this, AuthSignupStudents1.class);
                              else i = new Intent(AuthOtp.this, AuthSignupMentors1.class);

                              i.putExtra("phone", getIntent().getStringExtra("phone"));
                              // clears the previous app stack
                              i.setFlags(
                                  Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                              startActivity(i);
                            } else {
                              Toast.makeText(
                                      AuthOtp.this,
                                      "There was a problem in authentication",
                                      Toast.LENGTH_SHORT)
                                  .show();
                            }
                          });
            });

    // same functionality for resend OTP button
    resend.setOnClickListener(
        (view -> {

          pBar2.setVisibility(View.VISIBLE);
          resend.setVisibility(View.GONE);

          PhoneAuthProvider.getInstance()
              .verifyPhoneNumber(
                  "+91" + getIntent().getStringExtra("phone"),
                  60,
                  TimeUnit.SECONDS,
                  AuthOtp.this,
                  new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                      pBar2.setVisibility(View.GONE);
                      resend.setVisibility(View.GONE);
                      Toast.makeText(AuthOtp.this, "Phone number verified", Toast.LENGTH_SHORT)
                          .show();
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                      pBar2.setVisibility(View.GONE);
                      resend.setVisibility(View.GONE);
                      Toast.makeText(AuthOtp.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCodeSent(
                        @NonNull String s,
                        @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                      verificationId = s;

                      // not allowing the user to click on resend button again
                      pBar2.setVisibility(View.GONE);
                      resend.setVisibility(View.GONE);
                    }
                  });
        }));
  }
}

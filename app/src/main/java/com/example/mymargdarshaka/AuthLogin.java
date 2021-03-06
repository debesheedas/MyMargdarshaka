package com.example.mymargdarshaka;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class AuthLogin extends AppCompatActivity {

  private com.google.android.material.textfield.TextInputEditText phoneInput;
  private ProgressBar pBar;
  private Button getOtpButton;

  private DatabaseReference rootRef;

  private boolean fine = true;
  boolean found = false;
  private String userId;

  boolean testSuccessful = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_auth_login);

    // initializing variables
    phoneInput = findViewById(R.id.phoneInput);
    pBar = findViewById(R.id.spinner);
    getOtpButton = findViewById(R.id.getOTPButton);
    rootRef = FirebaseDatabase.getInstance().getReference();

    phoneInput.addTextChangedListener(
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

          @Override
          public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

          @Override
          public void afterTextChanged(Editable editable) {

            if (editable.toString().length() == 10) {
              setLoading(true);
              check();
            }
          }
        });

    getOtpButton.setOnClickListener(
        view -> {
          setLoading(true);
          generateOtp();
        });
  }

  void setLoading(boolean b) {                   // rendering the spinner
    if (b) {
      pBar.setVisibility(View.VISIBLE);
      getOtpButton.setVisibility(View.GONE);
    } else {
      pBar.setVisibility(View.GONE);
      getOtpButton.setVisibility(View.VISIBLE);
    }
  }

  // checks if the phone number is in the database and for the right type of user and sets the
  // "fine" variable.
  public void check() {

    setLoading(true);

    // checking in the students list
    DatabaseReference usersRef = rootRef.child("users");
    String phone = "No phone";
    if (phoneInput.getText() != null) {
      phone = phoneInput.getText().toString();
    }
    usersRef
        .orderByChild("phone")
        .equalTo(phone)
        .addListenerForSingleValueEvent(
            new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                long l = snapshot.getChildrenCount();
                long k = 0;
                if (l == 0) {
                  setLoading(false);
                } else {
                  setLoading(true);
                  for (DataSnapshot user : snapshot.getChildren()) {
                    Log.e("User key", user.getKey());

                    // if the user was previously signed in as a student, he/she cannot sign up as a mentor
                    if (!getIntent().getStringExtra("userType").equals("student")) {
                      fine = false;
                    } else {
                      found = true;
                      userId = user.getKey();
                    }
                    k++;
                    if (k == l) {
                      setLoading(false);
                    }
                  }
                }
              }

              @Override
              public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AuthLogin.this, error.getMessage(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(AuthLogin.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
              }
            });

    // checking in the mentors list
    if (fine) {

      DatabaseReference mentorsRef = rootRef.child("mentors");
      mentorsRef
          .orderByChild("phone")
          .equalTo(phoneInput.getText().toString())
          .addListenerForSingleValueEvent(
              new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                  long l = snapshot.getChildrenCount();
                  long k = 0;
                  if (l == 0) {
                    setLoading(false);
                  } else {
                    setLoading(true);
                    for (DataSnapshot child : snapshot.getChildren()) {
                      setLoading(true);
                      Log.e("User key", child.getKey());

                      // if the user was previously signed in as a mentor, he/she cannot sign up as a student
                      if (getIntent().getStringExtra("userType").equals("student")) {
                        fine = false;
                      } else {
                        found = true;
                        MentorDetails mentorDetails = child.getValue(MentorDetails.class);
                        userId = child.getKey();

                        // checking if the mentor has passed the test and routing appropriately
                        if (mentorDetails != null && mentorDetails.getNoTests() == -1) {
                          testSuccessful = true;
                        } else if (mentorDetails != null && mentorDetails.getNoTests() >= 5) {
                          Toast.makeText(
                                  AuthLogin.this, "You are not eligible.", Toast.LENGTH_SHORT)
                              .show();
                          Intent i = new Intent(AuthLogin.this, MainActivity.class);
                          i.setFlags(
                              Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                          startActivity(i);
                        } else {
                          testSuccessful = false;
                        }
                      }
                      k++;
                      if (k == l) {
                        setLoading(false);
                      }
                    }
                  }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                  Toast.makeText(AuthLogin.this, error.getMessage(), Toast.LENGTH_LONG)
                      .show();
                  Intent i = new Intent(AuthLogin.this, MainActivity.class);
                  i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                  startActivity(i);
                }
              });
    }
  }

  public void generateOtp() {

    if (fine) {

        // verifying phone number and mobile device and sending a valid OTP
      PhoneAuthProvider.getInstance()
          .verifyPhoneNumber(
              "+91" + phoneInput.getText().toString(),
              60,
              TimeUnit.SECONDS,
              AuthLogin.this,
              new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(
                    @NonNull PhoneAuthCredential phoneAuthCredential) {

                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                  setLoading(false);
                  Toast.makeText(AuthLogin.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCodeSent(
                    @NonNull String verificationId,
                    @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

                    // sending the user to the next page
                  Intent i = new Intent(AuthLogin.this, AuthOtp.class);
                  i.putExtra("userType", getIntent().getStringExtra("userType"));
                  i.putExtra("phone", phoneInput.getText().toString());
                  i.putExtra("verificationId", verificationId);
                  i.putExtra("found", found);
                  if (getIntent().getStringExtra("userType").equals("mentor") && found) {
                    i.putExtra("testSuccessful", testSuccessful);
                  }
                  if (found) {
                    i.putExtra("userId", userId);
                  }
                  startActivity(i);
                }
              });

    } else {

        // displaying appropriate toast message and routing back to the home page
      if (getIntent().getStringExtra("userType").equals("student")) {
        Toast.makeText(
                AuthLogin.this,
                "You have previously signed in as a mentor. You cannot sign in as a student.",
                Toast.LENGTH_LONG)
            .show();
      } else {
        Toast.makeText(
                AuthLogin.this,
                "You have previously signed in as a student. You cannot sign in as a mentor.",
                Toast.LENGTH_LONG)
            .show();
      }
      Intent i = new Intent(AuthLogin.this, MainActivity.class);
      i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
      startActivity(i);
    }
  }
}

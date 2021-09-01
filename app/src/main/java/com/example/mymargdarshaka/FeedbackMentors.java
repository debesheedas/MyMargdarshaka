package com.example.mymargdarshaka;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class FeedbackMentors extends AppCompatActivity {
    private final CollectionReference mColRef = FirebaseFirestore.getInstance().
            collection("mentors_feedback");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_mentors);
        EditText feedbackInput = findViewById(R.id.text_feedback);
        Button feedbackSubmitButton = findViewById(R.id.button_submit);

        feedbackSubmitButton.setOnClickListener(view -> {
            String feedback = feedbackInput.getText().toString();
            HashMap<String, String> data = new HashMap<>();
            data.put("feedback", feedback);
            mColRef.add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    Toast.makeText(FeedbackMentors.this, "Your feedback is recorded", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FeedbackMentors.this, MyMentors.class);
                    startActivity(intent);
                }
            });

        });
    }
}
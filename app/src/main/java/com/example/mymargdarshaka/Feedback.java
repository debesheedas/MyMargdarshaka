package com.example.mymargdarshaka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Feedback extends AppCompatActivity {
    private final CollectionReference mColRef = FirebaseFirestore.getInstance().
            collection("feedbacks");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        EditText feedbackInput = findViewById(R.id.text_feedback);
        Button feedbackSubmitButton = findViewById(R.id.button_submit);

        feedbackSubmitButton.setOnClickListener(view -> {
            String feedback = feedbackInput.getText().toString();
            HashMap<String, String> data = new HashMap<>();
            data.put("feedback", feedback);
            mColRef.add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    Toast.makeText(Feedback.this, "Your feedback is recorded", Toast.LENGTH_SHORT).show();
                    //TODO(@Shreetesh): Get the type of user and navigate to the appropriate page
                    Intent prevIntent = getIntent();
                    String previousActivity = prevIntent.getStringExtra("activity");
                    if (previousActivity.equals("my_mentors")) {
                        Intent intent = new Intent(Feedback.this, MyMentors.class);
                        startActivity(intent);
                    } else if (previousActivity.equals("my_students")) {
                        Intent intent = new Intent(Feedback.this, MyStudents.class);
                        startActivity(intent);
                    }
                }
            });

        });
    }
}
package com.example.mymargdarshaka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Test extends AppCompatActivity {
    public static final int NUM_QUESTIONS = 10;
    public static final int NUM_QUESTIONS_IN_SERVER = 13;
    public static final int CUTOFF_SCORE = 7;

    SharedPreferences sharedPreferences;
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    public static final String QUESTION = "question";
    public static final String ANSWER = "answer";
    public static final String OPTIONS = "options";

    private final CollectionReference mColRef = FirebaseFirestore.getInstance()
            .collection("questions");
    DocumentReference questionDoc;

    RadioGroup radio_group;
    TextView text_question_body;
    String correctResponse;
    String question;
    int currentQuestion;
    int score;
    Integer[] questionIds;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        score = 0;
        currentQuestion = 0;
        HashSet<Integer> uniqueQuestionIds = new HashSet<>();

        Button button_next = findViewById(R.id.button_next);
        radio_group = findViewById(R.id.radio_group);
        text_question_body = findViewById(R.id.text_question_body);

        while (uniqueQuestionIds.size() != NUM_QUESTIONS) {
            int randomNum = ThreadLocalRandom.current().
                    nextInt(1, NUM_QUESTIONS_IN_SERVER + 1);
            uniqueQuestionIds.add(randomNum);
        }

        questionIds = new Integer[uniqueQuestionIds.size()];
        uniqueQuestionIds.toArray(questionIds);

        fetchAndSetQuestion();
        // storing the responses in a HashMap
        Map<String, String> responses = new HashMap<>();

        button_next.setOnClickListener(view -> {
            if (currentQuestion < questionIds.length) {

                // TODO: handle functionality when user selects no option (currently it crashes)
                RadioButton selected_option = findViewById(radio_group.getCheckedRadioButtonId());
                question = text_question_body.getText().toString();
                String option = selected_option.getText().toString();

                if (option.equals(correctResponse)) score++;

                // storing the current response, maybe to show where they went wrong
                responses.put(question, option);

                radio_group.clearCheck();

                // verifying the response
                Toast.makeText(this, "Selected: " + option, Toast.LENGTH_SHORT).show();
                Log.d("Response", "onClick: " + responses);

                currentQuestion++;
                if (currentQuestion < questionIds.length) {
                    fetchAndSetQuestion();
                } else {
                    boolean hasPassed = score >= CUTOFF_SCORE;
                    Log.d("Test Pass Status", hasPassed + "");
                    Toast.makeText(
                            this,
                            "Test Pass Status " + hasPassed,
                            Toast.LENGTH_SHORT
                    ).show();

                    Intent i;
                    if(hasPassed){
                        // this will do the matching for mentor and fills the regStudents field and viceversa for students.
                        rootRef.child("mentors").child(getIntent().getStringExtra("mentorId")).child("noTests").setValue(-1);

                        String newMentorKey = getIntent().getStringExtra("mentorId");
                        // after matching, the mentor will be taken to MyStudents.
                        MentorMatching.match(newMentorKey, Test.this);

                    }else{
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        FirebaseAuth.getInstance().signOut();
                        editor.clear();
                        editor.apply();

                        // increment noTests here
                        rootRef.child("mentors").orderByChild(getIntent().getStringExtra("userId")).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot child:snapshot.getChildren()){
                                    MentorDetails details = child.getValue(MentorDetails.class);
                                    int noTests=details.getNoTests();
                                    rootRef.child("mentors").child(child.getKey()).child("noTests").setValue(noTests+1);

                                    Intent i2 = new Intent(Test.this, MainActivity.class);
                                    i2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(i2);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
        });

    }

    private void fetchAndSetQuestion() {
        questionDoc = mColRef.document(questionIds[currentQuestion] + "");
        questionDoc.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> questionData = documentSnapshot.getData();
                        assert questionData != null;
                        question = (String) questionData.get(QUESTION);

                        @SuppressWarnings("unchecked")
                        ArrayList<String> options = (ArrayList<String>) questionData.get(OPTIONS);

                        text_question_body.setText(question);
                        assert options != null;
                        for (int i = 0; i < radio_group.getChildCount(); i++) {
                            ((RadioButton) radio_group.getChildAt(i)).setText(options.get(i));
                        }
                        correctResponse = (String) questionData.get(ANSWER);
                    }
                }
        ).addOnFailureListener(e -> Log.d("Error", "onCreate: ", e));
    }
}

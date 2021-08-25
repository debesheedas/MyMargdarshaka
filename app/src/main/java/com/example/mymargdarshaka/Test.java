package com.example.mymargdarshaka;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Test extends AppCompatActivity {

//    public static final String QUESTION = "question";
//    private final CollectionReference mColRef = FirebaseFirestore.getInstance().collection("questions");


    RadioGroup radio_group;
    TextView text_question_body;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

//        int randomNum = ThreadLocalRandom.current().nextInt(1, 3 + 1);
//        DocumentReference doc = mColRef.document(randomNum + "");
//        doc.get().addOnSuccessListener(documentSnapshot -> text_question_body.setText(documentSnapshot.getString(QUESTION))).addOnFailureListener(e -> Log.d("Error", "onCreate: ", e));

        // this is the next button
        Button button_next = findViewById(R.id.button_next);

        // storing the responses in a HashMap
        Map<String, String> responses = new HashMap<>();

        // attaching a listener that saves the current response
        // and hopefully moves to the next activity


        // The idea for us must be to query a few random questions from the pool along with their
        // answers and keep track of the total by checking everytime the user clicks next

        // This happens in some sort of for loop

        // Instead of launching another activity, we can just change the contents of the elements
        // currently on the screen after saving the response
        button_next.setOnClickListener(view -> {
            // selecting the radio group
            radio_group = findViewById(R.id.radio_group);
            // the question
            text_question_body = findViewById(R.id.text_question_body);
            // selected option
            RadioButton selected_option = findViewById(radio_group.getCheckedRadioButtonId());
            String question = text_question_body.getText().toString();
            String option = selected_option.getText().toString();

            // storing the current response
            responses.put(question, option);

            // verifying the response
            Toast.makeText(Test.this, "Selected: " + option, Toast.LENGTH_SHORT).show();
            Log.d("Response", "onClick: " + responses);
        });

    }


}

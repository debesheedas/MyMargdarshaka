package com.example.mymargdarshaka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button studentButton,mentorButton;
    SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "login";
    private static final String TYPE="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        studentButton=(Button) findViewById(R.id.studentLoginButton);
        mentorButton=(Button) findViewById(R.id.mentorLoginButton);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);

        String type=sharedPreferences.getString(TYPE,null);

        if(type!=null){
            Toast.makeText(MainActivity.this,"You are already logged in",Toast.LENGTH_SHORT).show();
            Intent i;

            Log.e("type: ",type);

            if(type.equals("student")) {
                i = new Intent(MainActivity.this, MyMentors.class);
            }
            else{
                i = new Intent(MainActivity.this, MyStudents.class);
            }
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }

//        studentButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i=new Intent(MainActivity.this,AuthLogin.class);
//                i.putExtra("type","student");
//                startActivity(i);
//            }
//        });

        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,GuidelinesForStudents.class);
                i.putExtra("type","student");
                startActivity(i);
            }
        });

//        mentorButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i=new Intent(MainActivity.this,AuthLogin.class);
//                i.putExtra("type","mentor");
//                startActivity(i);
//            }
//        });
        mentorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,AuthSignupMentors1.class);
                i.putExtra("type","mentor");
                startActivity(i);
            }
        });
    }
}
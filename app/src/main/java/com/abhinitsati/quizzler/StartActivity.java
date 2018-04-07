package com.abhinitsati.quizzler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button take = findViewById(R.id.takeButton);
        Button add = findViewById(R.id.addButton);

        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigate to the take quiz layout
                // which is the QuizActivity
                Intent intent = new Intent(StartActivity.this, QuizActivity.class);
                startActivity(intent);
                finish();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigate to the add layout
                Intent intent = new Intent(StartActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
    }
}

package com.abhinitsati.quizzler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Random;


public class QuizActivity extends Activity {

    Button mTrueButton;
    Button mFalseButton;
    TextView mQuestionTextView;
    TextView mScoreTextView;
    ProgressBar mProgressBar;
    int mScore = 0;   // current score of the user
    int mQIndex = 0;  // index of the current question
    TrueFalse question;  // actual question from the bank
    TextView mCategories;

    private TrueFalse[] mQuestionBank;
    int NUM_QUESTIONS;
    int PROGRESS_BAR_INCREMENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        // load the questions from the DB
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // get all questions into memory and then choose 10 random questions
                ArrayList<TrueFalse> questions = new ArrayList<>();

                for (DataSnapshot snap: dataSnapshot.getChildren()){

                    for (DataSnapshot snapshot: snap.getChildren()){

                        TrueFalse question = snapshot.getValue(TrueFalse.class);
                        if (question != null)
                            questions.add(question);
                    }
                }
                // pick 10 random questions from this list
                mQuestionBank = getRandomQts(questions);

                // link the layout elements to the java code
                mTrueButton = findViewById(R.id.true_button);
                mFalseButton = findViewById(R.id.false_button);
                mQuestionTextView = findViewById(R.id.question_text_view);
                mScoreTextView = findViewById(R.id.score);
                mProgressBar = findViewById(R.id.progress_bar);
                mCategories = findViewById(R.id.catText);

                // init the number questions and the progress bar
                // increment according to question bank length
                NUM_QUESTIONS = mQuestionBank.length;
                PROGRESS_BAR_INCREMENT = (int) Math.ceil(100.0 / mQuestionBank.length);

                // get the first question
                question = mQuestionBank[mQIndex];
                mQuestionTextView.setText(question.getQuestionText());
                // set the current score to 0 initially
                String newScore = "Score " + mScore + "/" + NUM_QUESTIONS;
                mScoreTextView.setText(newScore);
                // set the categories for the question
                mCategories.setText(question.getCategories());

                // add a common listener for both button taps
                View.OnClickListener myListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // which button did the user press?, figure out using v.getId()
                        checkAnswer(v.getId());
                        // get a new question from the bank and show it on the textview
                        getNewQuestion();
                    }
                };

                mTrueButton.setOnClickListener(myListener);
                mFalseButton.setOnClickListener(myListener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private TrueFalse[] getRandomQts(ArrayList<TrueFalse> questions) {

        // pick 10 rando questions
        TrueFalse []ran_ques = new TrueFalse[10];
        int r = 0;   // index for ran_ques

        // for selecting random numbers
        Random random = new Random();

        HashSet<Integer> unique = new HashSet<>();
        while (unique.size() < 10){

            int i = random.nextInt(questions.size());

            if (!unique.contains(i)) {
                // if unique int found, add that question
                ran_ques[r++] = questions.get(i);
                unique.add(i);
            }
        }

        return ran_ques;
    }

    private void getNewQuestion(){

        // update the progress bar
        mProgressBar.incrementProgressBy(PROGRESS_BAR_INCREMENT);
        // update the score
        String newScore = "Score " + mScore + "/" + NUM_QUESTIONS;
        mScoreTextView.setText(newScore);

        mQIndex = (mQIndex + 1) % NUM_QUESTIONS;  // get the next question index

        if (mQIndex == 0){
            // game over, show points
            // show an alert
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Game Over");
            // the user cannot get rid of the alert by tapping outside it
            // they have to click the button on the alert
            alert.setCancelable(false);
            alert.setMessage("You scored " + mScore + " points!");
            // create a button on the alert
            alert.setPositiveButton("Close App", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();  // close the app
                }
            });
            alert.show();
        }

        // show the next question
        question = mQuestionBank[mQIndex];
        mQuestionTextView.setText(question.getQuestionText());
        // set the new question categories
        mCategories.setText(question.getCategories());
    }

    private void checkAnswer(int buttonID){

        // get the users response to the question
        boolean userAnswer = (buttonID == R.id.true_button);
        int verdict;  // is the response correct or not

        if (userAnswer == question.isqAnswer()) {
            // correct answer
            verdict = R.string.correct_toast;
            mScore++;  // increment score
        }
        else
            verdict = R.string.incorrect_toast;

        // show a toast
        Toast.makeText(getApplicationContext(), verdict,
                Toast.LENGTH_SHORT).show();
    }

}

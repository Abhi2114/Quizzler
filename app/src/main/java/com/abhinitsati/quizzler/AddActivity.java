package com.abhinitsati.quizzler;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddActivity extends AppCompatActivity {

    // define all the views
    private EditText mQuestionText;
    private CheckBox mHistory;
    private CheckBox mTech;
    private CheckBox mSci;
    private CheckBox mPolity;
    private CheckBox mGenKnow;
    private RadioButton mYes, mNo;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // link the views
        mQuestionText = findViewById(R.id.qtsText);
        mHistory = findViewById(R.id.ahCheck);
        mTech = findViewById(R.id.techCheck);
        mSci = findViewById(R.id.sCheck);
        mPolity = findViewById(R.id.polCheck);
        mGenKnow = findViewById(R.id.gkCheck);
        mYes = findViewById(R.id.yesButton);
        mNo = findViewById(R.id.noButton);

        Button submit = findViewById(R.id.submitButton);

        // add a listener to the button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if the user entered everything in a proper format
                // proceed to add the question to the database
                TrueFalse question = getQuestion();
                if (question != null){
                    mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                    // push the question to the database
                    mDatabaseReference.child("questions").push().setValue(question, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                            if (databaseError == null){
                                // operation successful
                                Toast.makeText(getApplicationContext(),
                                        "Question added successfully..",
                                        Toast.LENGTH_SHORT).show();
                            }
                            else{
                                // operation failed
                                Toast.makeText(getApplicationContext(),
                                        "Oops, Something went wrong..",
                                        Toast.LENGTH_SHORT).show();
                                Log.d("Quizzler", databaseError.toString());
                            }
                        }
                    });

                    mQuestionText.setText("");
                }
                else{
                    // show the user what the error was
                    showErrorDialog();
                }
            }
        });
    }

    private TrueFalse getQuestion(){

        String question = mQuestionText.getText().toString();
        boolean hist = mHistory.isChecked();
        boolean tech = mTech.isChecked();
        boolean science = mSci.isChecked();
        boolean politics = mPolity.isChecked();
        boolean gk = mGenKnow.isChecked();
        // check for radio buttons
        boolean yes = mYes.isChecked();
        boolean no = mNo.isChecked();

        // if no options are checked return null
        if ((!hist && !tech && !science && !politics && !gk) || (!yes && !no))
            return null;

        boolean answer = true;
        if (no)
            answer = false;

        String categories = "";
        if (hist)
            categories += "History, ";
        if (tech)
            categories += "Tech, ";
        if (science)
            categories += "Science, ";
        if (politics)
            categories += "Politics, ";
        if (gk)
            categories += "GK";

        return new TrueFalse(question, answer, categories);
    }

    private void showErrorDialog(){

        // show an alert message to the user
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(R.string.valid_details)
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}

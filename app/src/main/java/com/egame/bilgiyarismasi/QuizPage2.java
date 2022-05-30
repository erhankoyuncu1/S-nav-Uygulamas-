package com.egame.bilgiyarismasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuizPage2 extends AppCompatActivity {

    TextView time, correct, wrong;
    TextView question, a, b, c, d;
    Button next, finish;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference().child("QuestionsWrong");

    String quizQuestion;
    String quizAnswerA;
    String quizAnswerB;
    String quizAnswerC;
    String quizAnswerD;
    String quizCorrectAnswer;
    int wrongQquestionCount;
    int questionNumber = 1;

    String userAnswer;
    int userCorrect = 0;
    int userWrong = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_page2);

        question = findViewById(R.id.textViewQuestion1);
        a = findViewById(R.id.textViewA1);
        b = findViewById(R.id.textViewB1);
        c = findViewById(R.id.textViewC1);
        d = findViewById(R.id.textViewD1);

        next = findViewById(R.id.btnSonraki);
        finish = findViewById(R.id.btnBitir);

        game();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                game();
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(QuizPage2.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAnswer = "a";
                if(quizCorrectAnswer.equals(userAnswer))
                {
                    a.setBackgroundColor(Color.GREEN);
                    userCorrect++;
                }
                else
                {
                    a.setBackgroundColor(Color.RED);
                    userWrong++;
                    findAnswer();
                }
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAnswer = "b";
                if(quizCorrectAnswer.equals(userAnswer))
                {
                    b.setBackgroundColor(Color.GREEN);
                    userCorrect++;
                }
                else
                {
                    b.setBackgroundColor(Color.RED);
                    userWrong++;
                    findAnswer();
                }
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAnswer = "c";
                if(quizCorrectAnswer.equals(userAnswer))
                {
                    c.setBackgroundColor(Color.GREEN);
                    userCorrect++;
                }
                else
                {
                    c.setBackgroundColor(Color.RED);
                    userWrong++;
                    findAnswer();
                }
            }
        });

        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userAnswer = "d";
                if(quizCorrectAnswer.equals(userAnswer))
                {
                    d.setBackgroundColor(Color.GREEN);
                    userCorrect++;
                }
                else
                {
                    d.setBackgroundColor(Color.RED);
                    userWrong++;
                    findAnswer();
                }
            }
        });
    }

    public void game()
    {
        a.setBackgroundColor(Color.WHITE);
        b.setBackgroundColor(Color.WHITE);
        c.setBackgroundColor(Color.WHITE);
        d.setBackgroundColor(Color.WHITE);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                wrongQquestionCount = (int) dataSnapshot.getChildrenCount();

                quizQuestion = dataSnapshot.child(String.valueOf(questionNumber)).child("question").getValue().toString();
                quizAnswerA = dataSnapshot.child(String.valueOf(questionNumber)).child("a").getValue().toString();
                quizAnswerB = dataSnapshot.child(String.valueOf(questionNumber)).child("b").getValue().toString();
                quizAnswerC = dataSnapshot.child(String.valueOf(questionNumber)).child("c").getValue().toString();
                quizAnswerD = dataSnapshot.child(String.valueOf(questionNumber)).child("d").getValue().toString();
                quizCorrectAnswer = dataSnapshot.child(String.valueOf(questionNumber)).child("ans").getValue().toString();

                question.setText(quizQuestion);
                a.setText(quizAnswerA);
                b.setText(quizAnswerB);
                c.setText(quizAnswerC);
                d.setText(quizAnswerD);

                if(questionNumber < wrongQquestionCount)
                {
                    questionNumber++;
                }
                else
                {
                    Toast.makeText(QuizPage2.this, "Tüm soruları cevapladın", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(QuizPage2.this, "Sorry, there is a problem", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void findAnswer()
    {
        if(quizCorrectAnswer .equals("a"))
        {
            a.setBackgroundColor(Color.GREEN);
        }
        else if(quizCorrectAnswer .equals("b"))
        {
            b.setBackgroundColor(Color.GREEN);
        }
        else if(quizCorrectAnswer .equals("c"))
        {
            c.setBackgroundColor(Color.GREEN);
        }
        else if(quizCorrectAnswer .equals("d"))
        {
            d.setBackgroundColor(Color.GREEN);
        }
    }
}
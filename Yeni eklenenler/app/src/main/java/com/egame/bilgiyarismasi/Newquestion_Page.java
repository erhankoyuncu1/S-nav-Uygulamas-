package com.egame.bilgiyarismasi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimatedStateListDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Newquestion_Page extends AppCompatActivity {
    EditText question;
    EditText optA;
    EditText optB;
    EditText optC;
    EditText optD;
    EditText answer;
    boolean valid= false;

    Button addQuestion;

    //tablodaki var olan soruların sayısını tutacak
    int newQuestionCount;
    //eklenecek olan sorunun id sini tutacak
    int questionNumber;

    //Firebase datastoredaki tablolara erişim
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //Eklemek istenilen soruların tutulduğu (QuestionsToAdd) tablosuna erişim
    DatabaseReference databaseReference = database.getReference().child("QuestionsToAdd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newquestion__page);

        question = findViewById(R.id.editTextQuestion);
        optA = findViewById(R.id.editTextoptA);
        optB = findViewById(R.id.editTextoptB);
        optC = findViewById(R.id.editTextoptC);
        optD = findViewById(R.id.editTextoptD);
        answer = findViewById(R.id.editTextanswer);

        addQuestion = findViewById(R.id.button_addQuestion);

        //Soru ekleme fonksiyonu çağrılır, edittextler boşaltılır
        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkField(question)&checkField(optA)&checkField(optB)&checkField(optC)&checkField(optD)&checkField(answer))
                   valid = true;
                else valid = false;

                if(valid) {
                    sendQuestion();
                    question.setText("");
                    optA.setText("");
                    optB.setText("");
                    optC.setText("");
                    optD.setText("");
                    answer.setText("");
                }
            }
        });
    }

    //QuestionsToAdd tablosundaki var olan soru sayısı alıyor
    void getQuestionNo()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //soru sayısı tutuluyor
                newQuestionCount = (int) dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(Newquestion_Page.this, "Sorry, there is a problem", Toast.LENGTH_LONG).show();

            }
        });
    }

    public void sendQuestion()
    {
        //Kullanıcıdan veriler alınıyor
        String question_ = question.getText().toString();
        String optA_ = optA.getText().toString();
        String optB_ = optB.getText().toString();
        String optC_ = optC.getText().toString();
        String optD_ = optD.getText().toString();
        String answer_ = answer.getText().toString();

        String questionNumber_;
        getQuestionNo();
        //Soru id'sine var olan soru sayı sayının bir fazlası veriliyor
        questionNumber = newQuestionCount+1;
        questionNumber_ = String.valueOf(questionNumber);

        //QuestionsToAdd tablosuna veriler aktarılıyor
        databaseReference.child(questionNumber_).child("question").setValue(question_);
        databaseReference.child(questionNumber_).child("a").setValue(optA_);
        databaseReference.child(questionNumber_).child("b").setValue(optB_);
        databaseReference.child(questionNumber_).child("c").setValue(optC_);
        databaseReference.child(questionNumber_).child("d").setValue(optD_);
        databaseReference.child(questionNumber_).child("ans").setValue(answer_);
    }
    //Kullanıcıların gireceği verilerin boş olma durumunu kontrol etme fonksiyonu
    public boolean checkField(EditText textField){
        if(textField.getText().toString().isEmpty()){
            textField.setError("Error");
            valid = false;
        }
        else
            valid = true;

        return  valid;
    }
}
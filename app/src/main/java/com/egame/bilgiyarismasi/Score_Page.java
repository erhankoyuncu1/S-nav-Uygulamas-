package com.egame.bilgiyarismasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileOutputStream;
import java.io.IOException;

public class Score_Page extends AppCompatActivity {

    TextView scoreCorrect, scoreWrong,userName;
    Button  exit,olustur;
    private MediaPlayer mediaPlayer;

    String userCorrect;
    String userWrong;
    String userName_;
    String userSurname_;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("Scores");
    DatabaseReference databaseReferenceSecond = database.getReference("Users");

    //geçerli kullanıcı
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    //Kullanıcı analiz dosyası
    String et_dosyaicerik;
    FileOutputStream outputStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score__page);
        //Alkışlama sesi
        mediaPlayer=MediaPlayer.create(Score_Page.this,R.raw.mymusic);
        mediaPlayer.start();

        scoreCorrect = findViewById(R.id.textViewAnswerCorrect);
        scoreWrong = findViewById(R.id.textViewAnswerWrong);
        exit = findViewById(R.id.buttonExıt);

        //Kullanıcının sınavda yaptığı doğru yanlış sayısı tablodan çekilip gösteriliyor
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userUID = user.getUid();
                userCorrect = dataSnapshot.child(userUID).child("correct").getValue().toString();
                userWrong = dataSnapshot.child(userUID).child("wrong").getValue().toString();
                scoreCorrect.setText(userCorrect);
                scoreWrong.setText(userWrong);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void AnalizDosyaOlustur(){
        try{
            outputStream=openFileOutput("/Documents/Analiz.txt", Context.MODE_APPEND);//MODE_PRIVATE ilk önce

        }catch(Exception e){
            System.out.println(e);
        }

         String analiz = userName.toString() + " " + userSurname_.toString() + "\n" +
                "Doğru Sayısı: " + userCorrect.toString() + "Yanlış Sayısı" + userWrong.toString();
        olustur = findViewById(R.id.button_olustur);

        olustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    outputStream.write(analiz.toString().getBytes());
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void kullaniciBilgiAl(){
        databaseReferenceSecond.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userUID = user.getUid();
                userName_ = dataSnapshot.child(userUID).child("name").getValue().toString();
                userSurname_ = dataSnapshot.child(userUID).child("surname").getValue().toString();
                scoreCorrect.setText(userCorrect);
                scoreWrong.setText(userWrong);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
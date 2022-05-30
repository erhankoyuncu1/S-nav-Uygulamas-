package com.egame.bilgiyarismasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//Öğrenci Sayfası
public class MainActivity extends AppCompatActivity {

    TextView signOut;

    Button start;
    Button info;
    Button startWrongQuestions;
    TextView name;
    TextView surname;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference().child("Users");
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    String userName;
    String userSurname;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        name = findViewById(R.id.txtname);
        surname = findViewById(R.id.txtsurname);

        //Kullanıcı bilgileri çekiliyor
        getInformation();

        //Çıkış butonu
        signOut = findViewById(R.id.textViewSignOut);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Geçerli kullanıcı olmaktan çıkar.
                auth.signOut();
                Intent i = new Intent(MainActivity.this, Login_Page.class);
                startActivity(i);
                finish();
            }
        });

        //Sınavı Başlatır!
        start = findViewById(R.id.buttonStart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Quiz_Page.class);
                startActivity(i);
            }
        });

        startWrongQuestions = findViewById(R.id.btnWrongQuestions);
        startWrongQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, QuizPage2.class);
                startActivity(i);
            }
        });

        //Kullanıcı Bilgilerinin verildiği sayfayı açar.
        info = findViewById(R.id.buttonInfo);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Information_Page.class);
                startActivity(i);
            }
        });

        //Kullanıcının konumunu gösterecek sayfayı açar.

    }

    public void getInformation()
    {

        name.setBackgroundColor(Color.WHITE);
        surname.setBackgroundColor(Color.WHITE);

        //Users tablosuna erişim
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Geçerli kullanıcının ID'si alınıyor
                user = auth.getCurrentUser();
                String userID = user.getUid();
                //Geçerli kullanıcının adı ve soryadı çekiliyor
                userName = dataSnapshot.child(String.valueOf(userID)).child("name").getValue().toString();
                userSurname = dataSnapshot.child(String.valueOf(userID)).child("surname").getValue().toString();

                //Ana sayfaya kullanıcın adı ve soyadı veriliyor
                name.setText(userName);
                surname.setText(userSurname);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(MainActivity.this, "Sorry, there is a problem", Toast.LENGTH_LONG).show();

            }
        });
    }
}

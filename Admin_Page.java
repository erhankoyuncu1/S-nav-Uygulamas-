package com.egame.bilgiyarismasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class Admin_Page extends AppCompatActivity {

    Button signOut;
    Button questions;
    Button info;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);

        signOut = findViewById(R.id.buttonexit);
        questions = findViewById(R.id.buttonquestions);
        info = findViewById(R.id.buttoninformation);

        //Çıkış butonu
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Geçerli kullanıcı olmaktan çıkar
                auth.signOut();
                Intent i = new Intent(Admin_Page.this, Login_Page.class);
                startActivity(i);
                finish();
            }
        });

        //Admin bilgileri sayfası
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Admin_Page.this, Information_Page.class);
                startActivity(i);
            }
        });

        //Soru havuzuna eklenmek istenen soruların listesi veren sayfa
        questions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Admin_Page.this,Questions_Page.class);
                startActivity(i);
            }
        });
    }
}
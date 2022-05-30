package com.egame.bilgiyarismasi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ExamManager_Page extends AppCompatActivity {
    Button information;
    Button add_question;
    Button signOut;

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_manager__page);

        information = findViewById(R.id.button_info);
        add_question = findViewById(R.id.button_addquestion);
        signOut = findViewById(R.id.button_signOut);

        //Soru ekleme sayfası
        add_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ExamManager_Page.this, Newquestion_Page.class);
                startActivity(i);
            }
        });

        //Sınav Yöneticisi bilgileri
        information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ExamManager_Page.this, Information_Page.class);
                startActivity(i);
            }
        });
        //çıkış butonu
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //geçerli kullanıcı olmaktan çıkar
                auth.signOut();
                Intent i = new Intent(ExamManager_Page.this, Login_Page.class);
                startActivity(i);
                finish();
            }
        });
    }
}
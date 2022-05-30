package com.egame.bilgiyarismasi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
//Admin eklecek sorulara karar verme erkanı
public class Questions_Page extends AppCompatActivity {
    // QuestionsToAdd tablosundan soruları tek parça halinde gösterecek yapılar
    RecyclerView recyclerView;
    QuestionAdapter questionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions__page);

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //QuestionsToAdd tablosuna erişim
        FirebaseRecyclerOptions<Question> options =
                new FirebaseRecyclerOptions.Builder<Question>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("QuestionsToAdd"), Question.class)
                .build();

        // QuestionsToAdd tablosundan soruları tek parça halinde gösterecek yapılar oluşturuldu
        questionAdapter = new QuestionAdapter(options);
        recyclerView.setAdapter(questionAdapter);
    }

    @Override
    protected void onStart() {
        //Tabloda oluşan herhangi bir değişikliği ekrana aktarma için dinleme yapılıyor
        super.onStart();
        questionAdapter.startListening();
    }

    @Override
    protected void onStop() {
        //Tablo izleme işlemini durdur
        super.onStop();
        questionAdapter.stopListening();
    }
}
package com.egame.bilgiyarismasi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuestionAdapter extends FirebaseRecyclerAdapter<Question,QuestionAdapter.myViewHolder>{

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public QuestionAdapter(@NonNull FirebaseRecyclerOptions<Question> options) {
        super(options);
    }

    //QuestionsToAdd tablosuna erişim
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference().child("QuestionsToAdd");
    //Questions tablosuna erişim. Sınav sorularının çekildiği tablo
    DatabaseReference databaseReferenceSecond = database.getReference();

    //Eklenecek olan sorunun id'si tutulacak
    int questionNumber = 1;
    //Questions tablosunda var olan soru sayısı
    int questionsCount;

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder,final int position, @NonNull Question model) {
        //Sorular modellere aktarılıyor
        holder.question.setText(model.getQuestion());
        holder.a.setText(model.getA());
        holder.b.setText(model.getB());
        holder.c.setText(model.getC());
        holder.d.setText(model.getD());
        holder.ans.setText(model.getAns());

        //Soru ekleme işlemini kabul etme
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Seçilen modeldeki sorunun verileri çekiliyor
                String question_ = model.getQuestion();
                String optA_ = model.getA();
                String optB_ = model.getB();
                String optC_ = model.getC();
                String optD_ = model.getD();
                String answer_ = model.getAns();
                String questionNumber_;
                getQuestionNo();
                //soru id'si Aktarılıyor
                questionNumber = questionsCount+1;
                questionNumber_ = String.valueOf(questionNumber);

                //Kabul edilen soru Questions tablosuna aktarılıyor
                databaseReferenceSecond.child("Questions").child(questionNumber_).child("q").setValue(question_);
                databaseReferenceSecond.child("Questions").child(questionNumber_).child("a").setValue(optA_);
                databaseReferenceSecond.child("Questions").child(questionNumber_).child("b").setValue(optB_);
                databaseReferenceSecond.child("Questions").child(questionNumber_).child("c").setValue(optC_);
                databaseReferenceSecond.child("Questions").child(questionNumber_).child("d").setValue(optD_);
                databaseReferenceSecond.child("Questions").child(questionNumber_).child("answer").setValue(answer_);

                //Aynı soru QuestionsToAdd tablosundan siliniyor
                FirebaseDatabase.getInstance().getReference().child("QuestionsToAdd")
                        .child(getRef(position).getKey()).removeValue();
            }
        });

        //Soru ekleme işlemi ret etme
        holder.btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //soru silme işelemi için soruluyor
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.question.getContext());
                builder.setTitle("Emin misin?");

                //silme işlemi
                builder.setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Soru QuestionsToAdd tablosundan siliniyor
                        FirebaseDatabase.getInstance().getReference().child("QuestionsToAdd")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });
                // Silme işlemi iptal etme
                builder.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.question.getContext(),"İptal edildi", Toast.LENGTH_LONG);
                    }
                });

                builder.show();
            }
        });


    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Soruların gösterileceği view oluşturma
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item,parent,false);
        return new myViewHolder(view);
    }

    //Soruların gösterileceği görünüm elemanları oluşturuluyor
    class myViewHolder extends RecyclerView.ViewHolder{
        TextView question,a,b,c,d,ans;
        Button btnAccept, btnReject;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            question = (TextView) itemView.findViewById(R.id.txtQuestion);
            a = (TextView) itemView.findViewById(R.id.txtoptA);
            b = (TextView) itemView.findViewById(R.id.txtoptB);
            c = (TextView) itemView.findViewById(R.id.txtoptC);
            d = (TextView) itemView.findViewById(R.id.txtoptD);
            ans = (TextView) itemView.findViewById(R.id.txtoptAns);
            btnAccept = (Button) itemView.findViewById(R.id.buttonAccept);
            btnReject = (Button)itemView.findViewById(R.id.buttonReject);
        }
    }

    //Soru sayısı alınıyor
    public void getQuestionNo(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                questionsCount = (int) snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}

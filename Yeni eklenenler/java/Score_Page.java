package com.egame.bilgiyarismasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Documented;

import javax.sql.StatementEvent;

public class Score_Page extends AppCompatActivity {

    private static final int STORAGE_CODE = 1000 ;
    TextView scoreCorrect, scoreWrong,userName;
    Button  exit,olustur;
    private MediaPlayer mediaPlayer;

    String userCorrect;
    String userWrong;
    String userName_;
    String userSurname_;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference("Scores");
    DatabaseReference databaseReferenceSecond = database.getReference();

    //geçerli kullanıcı
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    //Kullanıcı analiz dosyası
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
        olustur = findViewById(R.id.button_olustur);

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

        olustur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED) {
                        //Eğer izin yoksa
                        String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission, STORAGE_CODE);
                    }
                    else{
                        //izin varsa AnalizDosyaOlustur metodunu çağır
                        AnalizDosyaOlustur();
                    }
                }
                else {
                    //sürüm küçükse yine AnalizDosyaOlustur fonk.
                    AnalizDosyaOlustur();
                }
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //Analiz Dosyası oluşturma fonksiyonu
    public void AnalizDosyaOlustur(){
        //Kullanıcı bilgilerini al
        kullaniciBilgiAl();
        //Analiz rapor içeriği
        String analiz = userName_ + " " + userSurname_  +" "+
                "Doğru Sayısı: " + userCorrect + "Yanlış Sayısı: " + userWrong;

        //dosya oluştur
        Document dosya = new Document();
        //dosya adı
        String filename = new String("Rapor");
        //dosya yolu
        String filepath = Environment.getExternalStorageDirectory() + "/" + filename + ".pdf";
        Toast.makeText(this,"Rapor oluşturuldu",Toast.LENGTH_SHORT).show();

        try {
            //dosyaya ulaş
            PdfWriter.getInstance(dosya, new FileOutputStream(filepath));
            //dosyayı aç
            dosya.open();
            //yazdırma işlemi
            dosya.add(new Paragraph("Deneme yazısı"));
            //dosyayı kapat
            dosya.close();
            //pdf dosyasını kayıt etti
            Toast.makeText(this,"oluşturuldu",Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            //Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }


    }

    //Analiz dosyası içi izin alma fonksiyonu
    public void izinAl(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case STORAGE_CODE:{
                //izin alındıysa
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    AnalizDosyaOlustur();
                }
                else{
                    Toast.makeText(this," izin gerekli",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    //Analiz raporuna kullanıcı bilgileri yazdırmada kullanıcak fonksiyon
    public void kullaniciBilgiAl(){
        databaseReferenceSecond.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userUID = user.getUid();
                userName_ = dataSnapshot.child(userUID).child("name").getValue().toString();
                userSurname_ = dataSnapshot.child(userUID).child("surname").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
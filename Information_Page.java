package com.egame.bilgiyarismasi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Information_Page extends AppCompatActivity {

    TextView name;
    TextView surname;
    TextView age;
    TextView phone;
    TextView email;
    TextView usertype;

    String userName;
    String userSurname;
    String userAge;
    String userPhone;
    String userEmail;
    String userType;

    //Kullanıcı verileri için
    FirebaseAuth auth = FirebaseAuth.getInstance();
    //Firebase'den alınacak bilgilerin tablolarından referanslar alınıyor.
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    //Kullanıcılar tablosunun verilerine ulaşır
    DatabaseReference databaseReference = database.getReference().child("Users");

    //Geçerli kullanıcının bilgilerine ulaşmak için
    FirebaseUser user = auth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        name = findViewById(R.id.txtusername);
        surname = findViewById(R.id.txtusersurname);
        age = findViewById(R.id.txtuserage);
        phone = findViewById(R.id.txtuserphone);
        email = findViewById(R.id.txtuseremail);
        usertype = findViewById(R.id.textViewuserType);

        //Kullanıcının bilgilerini alacak fonk.
        getInformation();

    }

    public void getInformation()
    {
        //Kullanıcılar(Users) tablosuna erişim
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Kullanıcın ID bilgisi alınıyor
                user = auth.getCurrentUser();
                String userID = user.getUid();

                userEmail = user.getEmail().toString();

                //Kullanıcının verileri çekiliyor
                userName = dataSnapshot.child(String.valueOf(userID)).child("name").getValue().toString();
                userSurname = dataSnapshot.child(String.valueOf(userID)).child("surname").getValue().toString();
                userAge = dataSnapshot.child(String.valueOf(userID)).child("age").getValue().toString();
                userPhone = dataSnapshot.child(String.valueOf(userID)).child("phoneNumber").getValue().toString();
                userType = dataSnapshot.child(String.valueOf(userID)).child("userType").getValue().toString();

                //Ekrana kullanıcı bilgileri aktarılıyor
                name.setText(userName);
                surname.setText(userSurname);
                age.setText(userAge);
                phone.setText(userPhone);
                email.setText(userEmail);
                if(userType.equals("1"))
                    usertype.setText("Öğrenci");
                else if(userType.equals("2"))
                    usertype.setText("Sınav Yöneticisi");
                else if(userType.equals("3"))
                    usertype.setText("Admin");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(Information_Page.this, "Sorry, there is a problem", Toast.LENGTH_LONG).show();

            }
        });
    }
}
package com.egame.bilgiyarismasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewDebug;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class sign_up_page extends AppCompatActivity {
    EditText mail;
    EditText password;
    EditText userAge;
    EditText userName;
    EditText userSurname;
    EditText phoneNumber;
    Button signUp;
    ProgressBar progressBar;
    Spinner userSpinner;
    int userCount;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();

    DatabaseReference databaseReference = database.getReference().child("Users");
    DatabaseReference databaseReferenceSecond = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);

        mail = findViewById(R.id.editTextSignupMail);
        password = findViewById(R.id.editTextSignupPassword);
        signUp = findViewById(R.id.buttonSignupSign);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        userSpinner= findViewById(R.id.usertypespinnerSignup);
        userAge = findViewById(R.id.editTextAge);
        userName = findViewById(R.id.editTextPersonName);
        userSurname = findViewById(R.id.editTextPersonSurame);
        phoneNumber = findViewById(R.id.editTextphoneNumber);



        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.userType, R.layout.support_simple_spinner_dropdown_item);
        userSpinner.setAdapter(adapter);

        //Kayıt olma butonu
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp.setClickable(false);
                String userEmail = mail.getText().toString();
                String userPassword = password.getText().toString();

                //E-mail ve şifre ile Firebase datasına kayıt
                signUpFirebase(userEmail, userPassword);

            }
        });
    }

    //Firebase ile kayıt fonksiyonu
    public void signUpFirebase(String userEmail, String userPassword)
    {
        progressBar.setVisibility(View.VISIBLE);

        //Kullanıcı oluştur
        auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            sendUserInfo();
                            Toast.makeText(sign_up_page.this, "Hesap oluşturuldu",
                                    Toast.LENGTH_LONG).show();
                            finish();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                        else
                        {
                            Toast.makeText(sign_up_page.this,
                                    "Bir Problem var! Bu isim daha önce kullanılmış olabilir...",
                                    Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            signUp.setClickable(true);
                        }
                    }
                });
    }

    //Kullanıcı bilgilerini Users tablosuna aktarma fonk.
    public void sendUserInfo()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                userCount = (int) dataSnapshot.getChildrenCount();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(sign_up_page.this, "Sorry, there is a problem", Toast.LENGTH_LONG).show();

            }
        });
        user = auth.getCurrentUser();
        String userID = user.getUid();
        String name = userName.getText().toString();
        String surname = userSurname.getText().toString();
        String age = userAge.getText().toString();
        String phoneNo = phoneNumber.getText().toString();
        String role = userSpinner.getSelectedItem().toString();

        int userType = 1;
        if(role.equals("Student"))
            userType = 1;
        else if(role.equals("Exam Manager"))
            userType = 2;
        else if(role.equals("Admin"))
            userType = 3;

        //Kullanıcı bilgileri Users tablosuna aktarılıyor
        databaseReferenceSecond.child("Users").child(userID).child("age").setValue(age);
        databaseReferenceSecond.child("Users").child(userID).child("girilen_gun_sayisi").setValue("0");
        databaseReferenceSecond.child("Users").child(userID).child("name").setValue(name);
        databaseReferenceSecond.child("Users").child(userID).child("surname").setValue(surname);
        databaseReferenceSecond.child("Users").child(userID).child("userType").setValue(String.valueOf(userType));
        databaseReferenceSecond.child("Users").child(userID).child("phoneNumber").setValue(phoneNo);
    }
}
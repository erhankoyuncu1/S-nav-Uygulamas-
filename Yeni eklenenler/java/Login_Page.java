package com.egame.bilgiyarismasi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


//Giriş Sayfası
public class Login_Page extends AppCompatActivity {

    EditText mail;
    EditText password;
    Button SignIn;
    SignInButton SigninGoogle;
    TextView signUp;
    TextView forgotPassword;
    ProgressBar progressBarSignin;
    boolean valid=true;
    String userType = "";


    //Firebase kullancısı
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    //Kullanici Tipi
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference().child("Users");
    //Google kullancısı
    GoogleSignInClient googleSignInClient;

    //Sayfa yükleme fonksiyonu
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__page);

        mail = findViewById(R.id.editTextLoginEmail);
        password = findViewById(R.id.editTextLoginPassword);
        SignIn = findViewById(R.id.buttonLoginSignin);
        SigninGoogle = findViewById(R.id.buttonLoginGoogleSignin);
        signUp = findViewById(R.id.textViewLoginSignUp);
        forgotPassword = findViewById(R.id.textViewLoginForgotPassword);
        progressBarSignin = findViewById(R.id.progressBarSignin);


        //Giriş Butonu
        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMail = mail.getText().toString();
                String userPassword = password.getText().toString();

                checkField(mail);
                checkField(password);

                if(valid){
                    signInWithFirebase(userMail, userPassword);//Firebase ile Eposta şifre doğrulama ve giriş yapma
                }
                else progressBarSignin.setClickable(true);

            }
        });


        //Google ile giriş butonu
        SigninGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGoogle();
            }//google ile giriş fonksiyonu
        });

        //Kayıt oluşturma yazısı
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Login_Page.this, sign_up_page.class);
                startActivity(i);
            }
        });

        //Şifreyi unuttum yazısı
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login_Page.this, ForgotPassword_Page.class);
                startActivity(i);
            }
        });
    }

    //Firebase ile giriş Fonksiyonu
    public void signInWithFirebase(String userMail, String userPassword)
    {
        //Progres barı görünür yap
        progressBarSignin.setVisibility(View.VISIBLE);
        //Giriş butonunu tıklanamaz yap
        SignIn.setClickable(false);
        //Firebase ile Giriş işlemi
        auth.signInWithEmailAndPassword(userMail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            getUserType();
                            Toast.makeText(Login_Page.this, "Giriş başarılı",
                                    Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            Toast.makeText(Login_Page.this, "Giriş başarısız",
                                    Toast.LENGTH_LONG).show();
                            progressBarSignin.setVisibility(View.INVISIBLE);
                            SignIn.setClickable(true);
                        }
                    }
                });

    }

    //Oyun başlangıcı Kontroller
    @Override
    protected void onStart() {
        super.onStart();

        //Kullanıcı çıkış yapmadıysa ana sayfaya git
        FirebaseUser user = auth.getCurrentUser();
        if(user != null)
        {
            getUserType();
            finish();
        }
    }

    //Google ile giriş fonksiyonu
    public void signInGoogle()
    {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        signIn();
    }

    public void signIn()
    {
        Intent singInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(singInIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            firebaseSignInWithGoogle(task);
        }
    }

    //Google ile giriş fonksiyonu
    private void firebaseSignInWithGoogle(Task<GoogleSignInAccount> task)
    {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(Login_Page.this, "Giriş başarılı", Toast.LENGTH_LONG).show();
            Intent i = new Intent(Login_Page.this, MainActivity.class);
            startActivity(i);
            finish();
            firebaseGoogleAccount(account);
        }
        catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(Login_Page.this, "Giriş başarısız", Toast.LENGTH_LONG).show();
        }
    }

    //Google hesabına erişim
    private void firebaseGoogleAccount(GoogleSignInAccount account)
    {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser user = auth.getCurrentUser();
                }
                else{

                }
            }
        });
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

    //Giriş yapan kullancının kullanıcı tipine göre kullanacağı sayfaya yönlendirme
    public void getUserType(){
        //Users tablosundan kullanıcı tipi alınacak
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Geçerli kullanıcının ID'si alınıyor
                user = auth.getCurrentUser();
                String userID = user.getUid();

                //ID 'ye göre kullanıcı tipine erişiliyor
                userType = dataSnapshot.child(String.valueOf(userID)).child("userType").getValue().toString();

                //Kullanıcı tipine göre kullanacağı sayfaya yönlendirme
                //Öğrenci olma durumu
                if(userType.equals("1")){
                    Intent i = new Intent(Login_Page.this, MainActivity.class);
                    startActivity(i);
                    finish();
                    Toast.makeText(Login_Page.this, "Giriş başarılı",
                            Toast.LENGTH_LONG).show();
                }
                //Sınav yönetici olma durumu
                else if(userType.equals("2")){
                    Intent i = new Intent(Login_Page.this, ExamManager_Page.class);
                    startActivity(i);
                    finish();
                    Toast.makeText(Login_Page.this, "Giriş başarılı",
                            Toast.LENGTH_LONG).show();
                }
                //Admin olma durumu
                else if(userType.equals("3")){
                    Intent i = new Intent(Login_Page.this, Admin_Page.class);
                    startActivity(i);
                    finish();
                    Toast.makeText(Login_Page.this, "Giriş başarılı",
                            Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(Login_Page.this, "Sorry, there is a problem", Toast.LENGTH_LONG).show();

            }

        });
    }

}


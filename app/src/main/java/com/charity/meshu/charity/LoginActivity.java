package com.charity.meshu.charity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    TextView textView,createAccount;
    EditText Username,Password;
    private FirebaseAuth mAuth;
    String username,password;
    Button signIn;
    AlphaAnimation buttonClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonClick = new AlphaAnimation(1F, 0.6F);

        textView = (TextView) findViewById(R.id.CreateAccount);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(in);
            }
        });


        Username = (EditText) findViewById(R.id.Username);
        Password = (EditText) findViewById(R.id.Password);
        signIn = (Button) findViewById(R.id.SignIn);
        createAccount = (TextView) findViewById(R.id.CreateAccount);


        mAuth = FirebaseAuth.getInstance();

        signIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                keyboardDown();

                v.startAnimation(buttonClick);
                username = Username.getText().toString();
                password = Password.getText().toString();

                Log.d("testing: ", username);
                Log.d("testing: ", password);


                if(!username.equals("") && username != null && !password.equals("") && password != null) {
                    mAuth.signInWithEmailAndPassword(username, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user != null) {
                                            // Name, email address, and profile photo Url
                                            String name = user.getDisplayName();
                                            String email = user.getEmail();
                                            Uri photoUrl = user.getPhotoUrl();

                                            // Check if user's email is verified
                                            boolean emailVerified = user.isEmailVerified();

                                            // The user's ID, unique to the Firebase project. Do NOT use this value to
                                            // authenticate with your backend server, if you have one. Use
                                            // FirebaseUser.getToken() instead.
                                            String uid = user.getUid();
                                            Toast.makeText(LoginActivity.this, " Welcome to Charity !!",
                                                    Toast.LENGTH_LONG).show();

                                            Intent intent = new Intent(LoginActivity.this, UserHome.class);
                                            finish();

                                            startActivity(intent);

                                        }
                                    } else {
                                        Toast.makeText(LoginActivity.this, " Wrong Email or Password !!",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                }else{
                    keyboardDown();

                    Toast.makeText(LoginActivity.this, " Please Type Something !!",
                            Toast.LENGTH_LONG).show();
                }


            }
        });

        /*
        * button action for create account
        * */
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(createIntent);
                finish();
            }
        });
    }


    public void keyboardDown(){
          /* for hidding the keyboard after click done button */
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}

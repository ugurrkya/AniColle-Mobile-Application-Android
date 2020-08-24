package com.anicolle.ju.anicolle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


public class LoginActivity extends AppCompatActivity {

    EditText userEmailEditText, userPasswordEditText;
    Button loginLayoutBtn,createAccountLayoutBtn;
    private Toolbar toolbar;

    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        userEmailEditText = (EditText)findViewById(R.id.emailLoginEditText);
        userPasswordEditText = (EditText)findViewById(R.id.passwordLoginEditText);
        userPasswordEditText.setTransformationMethod(new PasswordTransformationMethod());
        loginLayoutBtn = (Button) findViewById(R.id.loginButtonMain);
        createAccountLayoutBtn =(Button) findViewById(R.id.createAccountButtonMain);

        mProgressDialog = new ProgressDialog(this);

        //firebase

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //check users

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if( user!=null)
                {
                    Intent moveToHome = new Intent(LoginActivity.this, MainActivity.class);
                    moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(moveToHome);
                }

            }
        };



        mAuth.addAuthStateListener(mAuthListener);

        createAccountLayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterUserActivity.class));

            }
        });


        loginLayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog.setTitle("Logging the user");
                mProgressDialog.setMessage("Please wait...");
                mProgressDialog.show();
                loginUser();

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void loginUser() {


        String userEmail, userPassword;

        userEmail = userEmailEditText.getText().toString().trim();
        userPassword = userPasswordEditText.getText().toString().trim();


        if (!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPassword))
        {

          mAuth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {

              if(task.isSuccessful())
              {
                  mProgressDialog.dismiss();
                  FirebaseUser user = mAuth.getCurrentUser();
                  Intent moveToHome = new Intent(LoginActivity.this, MainActivity.class);
                  moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  startActivity(moveToHome);
              }else
              {
                  Toast.makeText(LoginActivity.this, "Unable to logging user", Toast.LENGTH_LONG).show();
                  mProgressDialog.dismiss();
              }
              }
          });
        }else
        {
            Toast.makeText(LoginActivity.this, "Please enter user email and password", Toast.LENGTH_LONG).show();
            mProgressDialog.dismiss();
        }

    }


}

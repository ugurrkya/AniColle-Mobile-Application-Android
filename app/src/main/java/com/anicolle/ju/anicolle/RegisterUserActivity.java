package com.anicolle.ju.anicolle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterUserActivity extends AppCompatActivity {

    EditText userEmailCreateEditText, userPasswordCreateEditText;
    LinearLayout createAccountBtn;


    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        userEmailCreateEditText = (EditText) findViewById(R.id.emailRegisterEditText);
        userPasswordCreateEditText = (EditText) findViewById(R.id.passwordRegisterEditText);

        createAccountBtn = (LinearLayout) findViewById(R.id.createAccountSubmitBtn);


        mProgressDialog = new ProgressDialog(this);


        mAuth= FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user!=null)
                {

                    Intent moveToHome = new Intent(RegisterUserActivity.this,  UserProfileActivity.class);
                    moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(moveToHome);
                }

            }
        };

            mAuth.addAuthStateListener(mAuthListener);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.setTitle("Create Account");
                mProgressDialog.setMessage("Wait while the account is being created...");
                mProgressDialog.show();
                createUserAccount();

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void createUserAccount() {

        String emailUser, passUser;
        emailUser = userEmailCreateEditText.getText().toString().trim();
        passUser = userPasswordCreateEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(emailUser) && !TextUtils.isEmpty(passUser))
        {
        mAuth.createUserWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful() )
                {

                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(RegisterUserActivity.this, "Account created success",Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();

                    Intent moveToHome = new Intent(RegisterUserActivity.this,  UserProfileActivity.class);
                    moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(moveToHome);

                }else
                {
                    Toast.makeText(RegisterUserActivity.this, "Account creation failed",Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();
                }
            }
        });
        }




}}

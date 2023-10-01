package com.example.keepnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

  LinearLayout topLineaLayout;

  TextView keepNotes;
  CardView cardView;

  EditText editTextEmail;
  EditText editTextPassword;
  Button buttonLogin;
  TextView textViewSignUpBtn;
  ConstraintLayout resgiterLayout;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        topLineaLayout=findViewById(R.id.top_LinearLayout);
        keepNotes=findViewById(R.id.textView5);
        cardView=findViewById(R.id.cardView1);
        resgiterLayout=findViewById(R.id.register_layout);
        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPassword=findViewById(R.id.editTextPassword);
        buttonLogin=findViewById(R.id.buttonLogin);
        textViewSignUpBtn=findViewById(R.id.textViewSignUp_btn);
        progressBar=findViewById(R.id.progressBar2);


        buttonLogin.setOnClickListener((v)-> loginUser());

        textViewSignUpBtn.setOnClickListener(view -> {
             Intent intent=new Intent(LoginActivity.this,SignUpActivity.class);
             startActivity(intent);

        });

        animation();



    }
    void animation(){
        Animation button_Down=AnimationUtils.loadAnimation(this,R.anim.bottom_down_animation);
        topLineaLayout.startAnimation(button_Down);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation fade_In = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.fade_in);
                keepNotes.startAnimation(fade_In);
                cardView.startAnimation(fade_In);
                resgiterLayout.startAnimation(fade_In);
            }



        },  800);
    }


    void loginUser() {

        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

        boolean isValidated = validateData(email, password);
        if (!isValidated) {
            return;
        }
        loginAccoutInFirebase( email, password);


    }

    void loginAccoutInFirebase(String email,String password){
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        changeInProgressBar(true);
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }else{
                        Utility.showToast(LoginActivity.this,"Email is not verified,Please verify email");
                    }
                }else {
                    Utility.showToast(LoginActivity.this,task.getException().getLocalizedMessage());
                }
            }
        });

    }
    void changeInProgressBar(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            buttonLogin.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            buttonLogin.setVisibility(View.VISIBLE);
        }
    }
    boolean validateData(String email,String password){


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Email is Invalid");
            return false;
        }
        if (password.length()<6){
            editTextPassword.setError("Atleast 6 character");
            return  false;
        }
        return  true;
    }
}
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
import android.view.View.OnClickListener;
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

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    LinearLayout topLineaLayout;
    TextView keepNotes;
    CardView cardView;

    EditText editTextName;
    EditText editTextEmail;
    EditText editTextPassword;
     Button buttonSignUP ;
     TextView textViewLoginBtn;
    ConstraintLayout resgiterLayout;
    ProgressBar progressBar;
    FirebaseAuth  firebaseAuth;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        topLineaLayout = findViewById(R.id.top_LinearLayout);
        keepNotes=findViewById(R.id.textView5);
        cardView = findViewById(R.id.cardView1);
        resgiterLayout = findViewById(R.id.register_layout);
        editTextName=findViewById(R.id.editTextName);
        editTextEmail=findViewById(R.id.editTextEmail);
        editTextPassword=findViewById(R.id.editTextPassword);
        progressBar=findViewById(R.id.progressBar);
        buttonSignUP=findViewById(R.id.buttonSignUp);
        textViewLoginBtn=findViewById(R.id.textViewLogin);

        animation();

        buttonSignUP.setOnClickListener(view -> createAccount());

        textViewLoginBtn.setOnClickListener(view -> {
            Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
            startActivity(intent);
        });



    }

    private void animation(){
        Animation button_Down= AnimationUtils.loadAnimation(this,R.anim.bottom_down_animation);
        topLineaLayout.startAnimation(button_Down);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation fade_In = AnimationUtils.loadAnimation(SignUpActivity.this, R.anim.fade_in);
                keepNotes.startAnimation(fade_In);
                cardView.startAnimation(fade_In);
                resgiterLayout.startAnimation(fade_In);
            }

        },  800);
    }

    public void createAccount(){
        String Name=editTextName.getText().toString();
        String email=editTextEmail.getText().toString();
        String password=editTextPassword.getText().toString();

        boolean isValidated=validateData(Name,email,password);
        if (!isValidated){
            return;
        }
        createAccountInFirebase(Name,email,password);

    }
    public void createAccountInFirebase(String name,String email,String pasword){

        changeInProgressBar(true);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email,pasword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                changeInProgressBar(false);
                if (task.isSuccessful()){
                    Utility.showToast(SignUpActivity.this, "Account created sucessfully Check email to verify");
                    firebaseAuth.getCurrentUser().sendEmailVerification();
                    firebaseAuth.signOut();
                    finish();
                }else {
                    //failure occurs
                    Utility.showToast(SignUpActivity.this, task.getException().getLocalizedMessage());
                }

            }
        });
    }
    void changeInProgressBar(boolean inProgress){
        if (inProgress){
            progressBar.setVisibility(View.VISIBLE);
            buttonSignUP.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            buttonSignUP.setVisibility(View.VISIBLE);
        }
    }
    boolean validateData(String name,String email,String password){

        if (name.isEmpty()) {
            editTextName.setError("Enter Your Name");
            return false;
        }
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
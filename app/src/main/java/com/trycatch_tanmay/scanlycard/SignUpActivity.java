package com.trycatch_tanmay.scanlycard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
EditText full_name,Email_id,Passwd,confirm_psswd;
ImageView sign_btn;
TextView back_txt;

//email validator
    String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.com";
    String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$";
    ProgressDialog progressDialog;
    FirebaseAuth  mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        full_name=findViewById(R.id.full_name);
        Email_id=findViewById(R.id.Email_id);
        Passwd=findViewById(R.id.Passwd);
        confirm_psswd=findViewById(R.id.confirm_psswd);
        sign_btn=findViewById(R.id.sign_btn);
        back_txt=findViewById(R.id.back_txt);
        back_txt=findViewById(R.id.back_txt);
        progressDialog=new ProgressDialog(this);
        mAuth= FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();

        getWindow().setStatusBarColor(Integer.parseInt(String.valueOf(getColor(R.color.top_colour))));



        back_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerforAuth();
            }
        });
    }
// this line will take data from user and will store
    private void PerforAuth() {
        String email = Email_id.getText().toString();
        String password = Passwd.getText().toString();
        String confirmpassword = confirm_psswd.getText().toString();

//        here we will check either the input feild are empty or not from user side to validate the data
        if (!email.matches(emailPattern)) {
            Email_id.setError("Enter correct E-mail, Your E-mail should end with .com");
            Email_id.requestFocus();
        }// Password validation
        if (!password.matches(passwordPattern)) {
            Passwd.setError("Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character, and be at least 6 characters long.");
            Passwd.requestFocus();

        }
        // Confirm password validation
        if (!password.equals(confirmpassword)) {
            confirm_psswd.setError("Passwords do not match");
            confirm_psswd.requestFocus();
            return;

        }else {
            progressDialog.setMessage("Please wait while Registration");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(SignUpActivity.this, "Registration successfully", Toast.LENGTH_SHORT).show();

                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(SignUpActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
    private void sendUserToNextActivity() {
        Intent intent = new Intent(SignUpActivity.this, DashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
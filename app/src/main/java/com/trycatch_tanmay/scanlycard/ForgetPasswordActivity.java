package com.trycatch_tanmay.scanlycard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class ForgetPasswordActivity extends AppCompatActivity {
 private ImageView sign_btn;
private TextView email_txt;
private String email;
private FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        getWindow().setStatusBarColor(Integer.parseInt(String.valueOf(getColor(R.color.top_colour))));
        email_txt=findViewById(R.id.email_txt);
        sign_btn=findViewById(R.id.sign_btn);
        auth =FirebaseAuth.getInstance();

        sign_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                make a method of validateData()
                validateData();
            }
        });

    }
    

    private void validateData() {
        email=email_txt.getText().toString();
        if (email.isEmpty()){
            email_txt.setError("Required Email ");
        } else if (!email.matches(emailPattern)) {
            email_txt.setError("Enter correct E-mail, Your E-mail should end with .com");
            
        } else {
//            make a method of forget password
            ForgetPass();
        }
    }

    private void ForgetPass() {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgetPasswordActivity.this, "Checked your email ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgetPasswordActivity.this, LoginActivity.class));
                            finish();
                        }else {
                            Toast.makeText(ForgetPasswordActivity.this, "Error :"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
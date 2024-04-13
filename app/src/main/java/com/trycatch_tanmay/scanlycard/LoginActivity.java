package com.trycatch_tanmay.scanlycard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText edit_txt, password_txt;
    TextView rember_me, forgt_psswd, sign_in;
    CheckBox remember_checkbox;
    Button Btn_login;
    ImageView facebook_image, google_image;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.com";
    String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setStatusBarColor(Integer.parseInt(String.valueOf(getColor(R.color.white))));
        edit_txt = findViewById(R.id.edit_txt);
        password_txt = findViewById(R.id.password_txt);
        rember_me = findViewById(R.id.rember_me);
        forgt_psswd = findViewById(R.id.forgt_psswd);
        remember_checkbox = findViewById(R.id.remember_checkbox);
        Btn_login = findViewById(R.id.Btn_login);
        facebook_image = findViewById(R.id.facebook_image);
        google_image = findViewById(R.id.google_image);
        sign_in = findViewById(R.id.sign_in);


        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        sharedPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        // Check if remember me is checked and retrieve saved email and password
        if (sharedPreferences.getBoolean("isChecked", false)) {
            remember_checkbox.setChecked(true);
            edit_txt.setText(sharedPreferences.getString("email", ""));
            password_txt.setText(sharedPreferences.getString("password", ""));
        }

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        forgt_psswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performlogin();

            }
        });
        google_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, GoogleActivity.class);
                startActivity(intent);

            }
        });
    }

    private void performlogin() {
        String email = edit_txt.getText().toString();
        String password = password_txt.getText().toString();
// Save email and password if remember me is checked
        if (remember_checkbox.isChecked()) {
            editor.putBoolean("isChecked", true);
            editor.putString("email", email);
            editor.putString("password", password);
            editor.apply();
        } else {
            editor.clear();
            editor.apply();
        }
//        here we will check either the input feild are empty or not from user side to validate the data
        if (!email.matches(emailPattern)) {
            edit_txt.setError("Enter correct E-mail, Your E-mail should end with .com");
            edit_txt.requestFocus();
        }// Password validation
        if (!password.matches(passwordPattern)) {
            password_txt.setError("Password must contain at least one uppercase letter, one lowercase letter, one digit, one special character, and be at least 6 characters long.");
            password_txt.requestFocus();

        } else {
            progressDialog.setMessage("Please wait while Login");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(LoginActivity.this, "Login sucessfull", Toast.LENGTH_SHORT).show();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
    private void sendUserToNextActivity() {
        Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
//login id trialapp@gmail.com
// login password Aa@1234

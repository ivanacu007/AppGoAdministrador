package com.appgoadmin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appgoadmin.MainActivity;
import com.appgoadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin, toReg, btnForgot;
    private TextInputEditText edtEmail, edtPass;
    private String email, pass;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private LinearLayout linearBtn;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.btnLogin);
        edtEmail = findViewById(R.id.loginEmail);
        edtPass = findViewById(R.id.loginPass);
        linearBtn = findViewById(R.id.linearBtnLog);
        progressBar = findViewById(R.id.progressBarLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
                validateInputs();
            }
        });
    }

    public void validateInputs() {
        resetError();
        email = edtEmail.getText().toString().trim();
        pass = edtPass.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Introduce una dirección de correo válida");
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            edtPass.setError("Introduce una contraseña");
            return;
        }
        loginUser();
    }

    public void resetError() {
        edtEmail.setError(null);
        edtPass.setError(null);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void loginUser() {
        linearBtn.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Toast.makeText(LoginActivity.this, "Sesión iniciada", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            progressBar.setVisibility(View.GONE);
                            linearBtn.setVisibility(View.VISIBLE);
                            finish();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            linearBtn.setVisibility(View.VISIBLE);
                            // If sign in fails, display a message to the user.
//                            Log.d("ERRM", "signInWithEmail:failure", task.getException());
//
                            Toast.makeText(getApplicationContext(), "Error: Verifica tus datos",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
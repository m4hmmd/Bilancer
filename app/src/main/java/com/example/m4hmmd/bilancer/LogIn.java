package com.example.m4hmmd.bilancer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Log In";
    private static FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private EditText emailField;
    private EditText passField;
    private Button signInButton;
    private TextView forgotPasswordLink;
    private TextView signUpLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        progressDialog = new ProgressDialog(this);
        forgotPasswordLink = (TextView) findViewById(R.id.forgotPasswordLink);
        signUpLink = (TextView) findViewById(R.id.signUpLink);
        firebaseAuth = FirebaseAuth.getInstance();
        signInButton = (Button) findViewById(R.id.signInButton);
        emailField = (EditText) findViewById(R.id.emailField);
        passField = (EditText) findViewById(R.id.passField);
        signInButton.setOnClickListener(this);
        forgotPasswordLink.setOnClickListener(this);
        signUpLink.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void signIn(String email, String password) {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            Log.d(TAG, "!validateForm()");
            return;
        }

        // [START sign_in_with_email]
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LogIn.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                            Intent intent = new Intent(LogIn.this, Dashboard.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        progressDialog.hide();
                        // [END_EXCLUDE]
                    }


                    // [END sign_in_with_email]
                });

    }

    private void signOut() {
        firebaseAuth.signOut();
        updateUI(null);

    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required.");
            valid = false;
        } else {
            emailField.setError(null);
        }

        String password = passField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passField.setError("Required.");
            valid = false;
        } else {
            passField.setError(null);
        }

        return valid;
    }


    private void updateUI(FirebaseUser user) {
        progressDialog.hide();
        /*
        if (user != null) {
            mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,
                    user.getEmail(), user.isEmailVerified()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
            findViewById(R.id.email_password_fields).setVisibility(View.GONE);
            findViewById(R.id.signed_in_buttons).setVisibility(View.VISIBLE);

            findViewById(R.id.verify_email_button).setEnabled(!user.isEmailVerified());
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.signed_in_buttons).setVisibility(View.GONE);
        }*/
    }


    @Override
    public void onClick(View v) {
        if (v == signInButton) {
            signIn(emailField.getText().toString(), passField.getText().toString());


        }
        if (v == signUpLink) {
            Intent intent = new Intent(LogIn.this, EmailPasswordActivity.class);
            startActivity(intent);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }
        if (v == forgotPasswordLink) {
            Intent intent = new Intent(LogIn.this, ForgotPass.class);
            startActivity(intent);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
        progressDialog.hide();

    }

}
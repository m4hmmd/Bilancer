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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Class for registering
 */
public class EmailPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    // UI elements
    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPassword2;
    private EditText editName;
    private EditText editUsername;
    private EditText editSurname;
    private TextView textViewSignin;

    private ProgressDialog progressDialog;

    // Database Elements
    private static FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth firebaseAuth;
    public static FirebaseUser firebaseUser;
    public static FirebaseDatabase database;
    public static DatabaseReference userRef;

    // a Tag for Logs
    public static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password);
        progressDialog = new ProgressDialog(this);
        // Setting up UI
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editName =(EditText) findViewById(R.id.editName);
        editSurname =(EditText) findViewById(R.id.editSurname);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextPassword2 = (EditText) findViewById(R.id.editTextPassword2);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);

        // adding a listener to the button
        buttonRegister.setOnClickListener(this);

        // setting up Database Connection
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Link to SingIn activity
        textViewSignin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmailPasswordActivity.this, LogIn.class);
                startActivity(intent);
            }
        });
    }

    /**
     * The method for registering the user
     * @param email
     * @param password
     */
    private void registerUser(final String email, final String password){
        // Check if all the fields are satisfied and display an error message otherwise
        if (TextUtils.isEmpty(email)){
            Toast.makeText( this, "Please Enter Email: ", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText( this, "Please Enter password: ", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage( "User registering...");
        progressDialog.show();

        Log.d(TAG, "registerUser:" + email);
        // Ask Firebase tp create the User
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(EmailPasswordActivity.this, new OnCompleteListener<AuthResult>() {
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            signIn(email, password);
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            String name = editName.getText().toString().trim();

                            // Write new user
                            writeNewUser(user.getUid(), name, user.getEmail());

                            // Go to MainActivity
                            /*Intent intent = new Intent(EmailPasswordActivity.this, LogIn.class);
                            startActivity(intent);*/
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed",
                                    Toast.LENGTH_SHORT).show();
                            Log.i("registration", "failed");
                        }
                        progressDialog.hide();


                    }


                });


    }



    private void signOut() {
        firebaseAuth.signOut();
        updateUI(null);
    }
    public void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        progressDialog.setMessage("User Signing In...");
        progressDialog.show();

        // [START sign_in_with_email]
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                            Intent intent = new Intent(EmailPasswordActivity.this, Dashboard.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        progressDialog.hide();
                        // [END_EXCLUDE]
                    }


                    // [END sign_in_with_email]
                });

    }
    private boolean validateForm() {
        boolean valid = true;

        String email = editTextEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Required.");
            valid = false;
        } else {
            editTextEmail.setError(null);
        }

        String password = editTextPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Required.");
            valid = false;
        } else {
            editTextPassword.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        progressDialog.hide();
    }



    private String usernameFromEmail(String email) {
        if (email.contains("@")) {
            return email.split("@")[0];
        } else {
            return email;
        }
    }
    private void writeNewUser(String userId, String name, String email) {
        String surname = editSurname.getText().toString().trim();
        String username = usernameFromEmail(email);
        User user = new User(name, surname, username, email);

        userRef = database.getReference("Users").child(userId);
        userRef.setValue(user);
    }

    @Override
    public void onClick(View view){

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String password2 = editTextPassword2.getText().toString().trim();
        if (view == buttonRegister){
            if (password.equals(password2)) {
                registerUser(email, password);
            }
            else {
                Toast.makeText(EmailPasswordActivity.this, "Passwords are not the same" ,Toast.LENGTH_LONG).show();
            }
        }
    }
}

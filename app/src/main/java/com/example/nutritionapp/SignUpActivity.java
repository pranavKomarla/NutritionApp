package com.example.nutritionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    //EditText newEmail, newPassword;
    TextInputEditText newEmail, newPassword;

    private static final String TAG = "EmailPassword";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private static final String TAG2 = "SignUpActivity";

    String tempMessage;

    private DatabaseReference mDatabase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_NutritionApp);
        setContentView(R.layout.activity_sign_up);

        //newEmail = findViewById(R.id.sign_up_email);
        //newPassword = findViewById(R.id.sign_up_password);

        newEmail = findViewById(R.id.textInputEditTextSignUpEmail);
        newPassword = findViewById(R.id.textInputEditTextSignUpPassword);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]


    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();


        if(currentUser != null){
            Log.d("Current User", currentUser.getEmail());
            reload();
        }
    }
    // [END on_start_check_user]


    public void signUpOnClick (View v) {

        createAccount(newEmail.getText().toString(), newPassword.getText().toString());
        

    }

    public void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG,user.getUid() + user.getEmail());
                            updateUI(user);
                            Intent intent = new Intent(SignUpActivity.this, MyRecipes.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        Log.d("In update UI email", user.getEmail());
        Log.d("In update UI uid", user.getUid());

        writeNewUser(user.getUid(), user.getEmail());
    }


    public void writeNewUser(String strUID, String emailAdd) {

        Log.d("In write user Sign Up:", strUID + emailAdd);
        User user = new User(emailAdd, null);


        mDatabase.child("users").child(strUID).setValue(user);
    }

    private void reload() { }



}
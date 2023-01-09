package com.example.nutritionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {

    Button signOutButton;
    Button myRecipesButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_NutritionApp);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        signOutButton = findViewById(R.id.sign_out_button);
        myRecipesButton = findViewById(R.id.my_recipes_on_click);


    }

    public void SignOutOnClick(View v) {

        //check to see who the current user is
        Log.d("SignOutCurrentUser", mAuth.getCurrentUser().getEmail());
        FirebaseAuth.getInstance().signOut();

        // after sign out - check if hte current user is null - if not null then print out the current user
        if (mAuth.getCurrentUser() != null) {
            Log.d("AfterSignOutCurrentUser", mAuth.getCurrentUser().toString());
        }

        else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }





    }

    public void MyRecipesOnClick(View v) {
        Intent intent = new Intent(this, MyRecipes.class);
        startActivity(intent);
    }
}
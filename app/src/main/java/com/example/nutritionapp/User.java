package com.example.nutritionapp;

import com.google.firebase.database.IgnoreExtraProperties;

import org.json.JSONObject;

// [START rtdb_user_class]
@IgnoreExtraProperties
public class User {

    private String username;
    private JSONObject recipes;



    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, JSONObject recipes) {
        this.username = username;
        this.recipes = recipes;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public JSONObject getRecipes() {
        return recipes;
    }

    public void setRecipes(JSONObject recipes) {
        this.recipes = recipes;
    }
}
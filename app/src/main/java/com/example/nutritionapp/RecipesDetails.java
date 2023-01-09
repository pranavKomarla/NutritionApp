package com.example.nutritionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class RecipesDetails extends AppCompatActivity {

    TextView recipeUnused1, recipeUnused2, recipeUsed3;
    ImageView imageView1, imageView2, imageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_NutritionApp);
        setContentView(R.layout.activity_recipes_details);


        recipeUnused1 = findViewById(R.id.recipe_details_unused_1);
        recipeUnused2 = findViewById(R.id.recipe_details_unused_2);
        recipeUsed3 = findViewById(R.id.recipe_details_used_3);
        imageView1 = findViewById(R.id.imageView2);
        imageView2 = findViewById(R.id.imageView3);
        imageView3 = findViewById(R.id.imageView4);






    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();

        String recipeString = intent.getStringExtra(MyRecipes.EXTRA_MESSAGE);

        try {
            JSONObject recipe = new JSONObject((recipeString));
            Log.d("inRecipesDetails", recipe.toString());
            recipeUnused1.setText(recipe.getJSONArray("missedIngredients").getJSONObject(0).getString("name"));
            recipeUnused2.setText(recipe.getJSONArray("missedIngredients").getJSONObject(1).getString("name"));
            recipeUsed3.setText(recipe.getJSONArray("missedIngredients").getJSONObject(2).getString("name"));


            String url1 = recipe.getJSONArray("missedIngredients").getJSONObject(0).getString("image");
            Picasso.with(this).load(url1).into(imageView1);

            String url2 = recipe.getJSONArray("missedIngredients").getJSONObject(1).getString("image");
            Picasso.with(this).load(url2).into(imageView2);

            String url3 = recipe.getJSONArray("missedIngredients").getJSONObject(2).getString("image");
            Picasso.with(this).load(url3).into(imageView3);






        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
package com.example.nutritionapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class MyRecipes extends AppCompatActivity {

    TextView textView; 
    ListView listView;
    Button addRecipes;

    String allRecipes;

    public static final String EXTRA_MESSAGE = "com.example.nutritionapp.MESSAGE";


    ArrayList<JSONObject> arrayList;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_NutritionApp);
        setContentView(R.layout.activity_my_recipes);


        addRecipes = findViewById(R.id.add_recipes_button);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        listView = findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        
        textView = findViewById(R.id.my_recipes_textView);

        String strUID = mAuth.getCurrentUser().getUid();

        Log.d("UID", strUID);

        DatabaseReference recipesReference = mDatabase.child("users").child(strUID).child("recipes");

        //get the recipes from database in the form of a json string
        recipesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                if (snapshot.getValue() != null) {
                    String recipes = snapshot.getValue().toString();

                    JSONObject jsonObject = new JSONObject();

                    allRecipes = recipes;
                    //Log.d("in onDataChange", allRecipes);

                    try {
                        jsonObject= new JSONObject(allRecipes);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        int numberOfRecipesInJSON = jsonObject.getJSONArray("recipes").length();

                        for(int i = 0; i < numberOfRecipesInJSON; i++) {

                                JSONObject individualRecipe = new JSONObject();
                                individualRecipe = jsonObject.getJSONArray("recipes").getJSONObject(i);
                                arrayList.add(individualRecipe);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    Log.d("in MyRecipes", jsonObject.toString());

                    Log.d("first in my recipes", arrayList.get(0).toString());

                    try {
                        textView.setText("YOUR RECIPES");
                        Log.d("name of first recipe",arrayList.get(0).getString("title") );

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                else {

                    textView.setText("You currently do not have any selected recipes");
                }



                ListViewAdapter adapter = new ListViewAdapter(MyRecipes.this, R.layout.adapter_listview, arrayList);
                listView.setAdapter(adapter);





            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        







    }

    public void SignOut(View v) {

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

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("in onStart", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public class ListViewAdapter extends ArrayAdapter<JSONObject> {

        Context mainContext;
        int xml;
        List<JSONObject> list;

        public ListViewAdapter(@NonNull Context context, int resource, @NonNull List<JSONObject> objects) {
            super(context, resource, objects);
            mainContext = context;
            xml = resource;
            list = objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) mainContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View adapterLayout = inflater.inflate(xml, null);

            TextView recipe = adapterLayout.findViewById(R.id.recipe_listView);


            TextView ingredient = adapterLayout.findViewById(R.id.ingredient_listView);


            JSONObject jsonObject = list.get(position);
            try {
                recipe.setText(jsonObject.getString("title"));
                ingredient.setText(jsonObject.getJSONArray("usedIngredients").getJSONObject(0).getString("name"));
                Log.d("in ListViewAdapter", recipe.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    JSONObject recipeClicked = list.get(position);

                    Intent intent = new Intent(MyRecipes.this, RecipesDetails.class);
                    intent.putExtra(EXTRA_MESSAGE, recipeClicked.toString());

                    startActivity(intent);
                }
            });


            return adapterLayout;
        }


    }






    public void AddRecipesOnClick(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
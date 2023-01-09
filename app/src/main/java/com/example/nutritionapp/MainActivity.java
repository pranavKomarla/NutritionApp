package com.example.nutritionapp;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;



    EditText ingredient;
    Button enter;

    TextInputEditText ingredientSelected, numberOfIngredients;
    String apiKey = "a812c418de494c0ba8028d98f7ad030c";

    ArrayList<JSONObject> arrayList;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_NutritionApp);
        setContentView(R.layout.activity_main);


    }

    public void enterOnClick(View v) {
        new JsonReader().execute();


    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("in onStart Main", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        arrayList = new ArrayList<>();

        enter = findViewById(R.id.enter);

        ingredientSelected = findViewById(R.id.textInputEditTextIngredient);

        numberOfIngredients = findViewById(R.id.textInputEditTextCount);

        Log.d("in onResume Main", "onResume");
    }

    private class JsonReader extends AsyncTask<URL, Integer, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected Void doInBackground(URL... urls) {
            int count = urls.length;

            //String counter = numberOfIngredients.getText().toString();

            String line = "";

            String recipeInput = ingredientSelected.getText().toString();

            StringBuilder stringBuilder = new StringBuilder();

            JSONObject JSONRecipe = new JSONObject();
            JSONArray array = new JSONArray();

            URL url = null;
            try {

                url = new URL("https://api.spoonacular.com/recipes/findByIngredients?apiKey=" + apiKey + "&ingredients=" + recipeInput + ",+flour,+sugar&number=" + numberOfIngredients.getText().toString());
                Log.d("URL", url.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            URLConnection con = null;
            try {
                assert url != null;
                con = url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                assert con != null;
                InputStreamReader input = new InputStreamReader(con.getInputStream());
                BufferedReader reader = new BufferedReader(input);

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                //System.out.println("hello" + stringBuilder);

                array = new JSONArray(stringBuilder.toString());

                Log.d("JSONArray info", array.toString());


                for(int i = 0; i < 1; i++)
                {
                    arrayList.add(array.getJSONObject(i));

                }

                JSONRecipe.put("recipes",array);

                //System.out.println("object");
                //Log.d("TAG_INFO", JSONRecipe.toString());

            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.d("TAG_INFO", array.toString());

            String strUID = mAuth.getCurrentUser().getUid();

            User user = new User(mAuth.getCurrentUser().getEmail(), JSONRecipe);
            updateUser(strUID, user, JSONRecipe);
            //mDatabase.child("users").child(mAuth.getCurrentUser().getUid()).setValue(user);

            return null;
        }



        protected void onProgressUpdate(Integer... progress) {
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Intent intent = new Intent(MainActivity.this, MyRecipes.class);

            startActivity(intent);


        }
    }

    private void updateUser(String strUID, User user, JSONObject jsonRecipe) {
        Log.d("In write user Sign Up:", strUID);
        Log.d("in write user Sign up", jsonRecipe.toString());

        mDatabase.child("users").child(strUID).child("recipes").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));

                    if(task.getResult().getValue() == null) {
                        mDatabase.child("users").child(strUID).child("username").setValue(user.getUsername());
                        mDatabase.child("users").child(strUID).child("recipes").setValue(jsonRecipe.toString());
                    }

                    else {

                        try {
                            JSONObject jsonObjectInDatabase = new JSONObject();
                            jsonObjectInDatabase = new JSONObject(String.valueOf(task.getResult().getValue()));

                            for(int i = 0; i < Integer.valueOf(numberOfIngredients.getText().toString()); i ++) {
                                jsonObjectInDatabase.getJSONArray("recipes").put(jsonRecipe.getJSONArray("recipes").getJSONObject(i));
                            }


                            mDatabase.child("users").child(strUID).child("username").setValue(user.getUsername());
                            mDatabase.child("users").child(strUID).child("recipes").setValue(jsonObjectInDatabase.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                }
            }
        });








    }




}
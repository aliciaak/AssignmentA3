package com.example.assignmenta2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.assignmenta2.database.AppDatabase;
import com.example.assignmenta2.database.AsyncTaskDelegate;
import com.example.assignmenta2.database.InsertCategoriesAsyncTask;
import com.example.assignmenta2.database.RetrieveCategoriesAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ActivityCategory extends AppCompatActivity implements AsyncTaskDelegate {

    private final String JSON_URL = "http://jservice.io/api/categories?count=100";

    private JsonArrayRequest request;
    AppDatabase db;
    private RequestQueue requestQueue;
    private List<Category> listAnime;
    private RecyclerView category_recycler;
    AdapterCategory myAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setTitle("Categories");
        db = AppDatabase.getInstance(ActivityCategory.this);

        progressBar = findViewById(R.id.progressbar);
        listAnime = new ArrayList<>();
        category_recycler = findViewById(R.id.category_recycler);

        myAdapter = new AdapterCategory(this, listAnime);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3,LinearLayoutManager.VERTICAL,false);
        category_recycler.setLayoutManager(gridLayoutManager);
        category_recycler.setAdapter(myAdapter);

        RetrieveCategoriesAsyncTask insertCategoriesAsyncTask = new RetrieveCategoriesAsyncTask();
        insertCategoriesAsyncTask.setDatabase(db);
        insertCategoriesAsyncTask.setDelegate(ActivityCategory.this);
        insertCategoriesAsyncTask.execute();

        gsonRequest();
    }

    private void gsonRequest() {

        request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Category categories = new Category();

                        categories.id = jsonObject.getInt("id");

                        categories.title = jsonObject.getString("title");

                        //randomizes the background color of category list items
                        //taken from https://www.tutorialspoint.com/how-to-set-random-background-for-recyclerview-in-android
                        Random rnd = new Random();
                        int currentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256),rnd.nextInt(256));
                        categories.color = currentColor;

                        listAnime.add(categories);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                InsertCategoriesAsyncTask insertCategoriesAsyncTask = new InsertCategoriesAsyncTask();
                insertCategoriesAsyncTask.setDatabase(db);
                insertCategoriesAsyncTask.setDelegate(ActivityCategory.this);

                Category[] categories = listAnime.toArray(new Category[listAnime.size()]);

                insertCategoriesAsyncTask.execute(categories);

                myAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ActivityCategory.this, "Something went wrong." + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue = Volley.newRequestQueue(ActivityCategory.this);
        requestQueue.add(request);
    }


    private void setuprecyclerview() {
        if(myAdapter != null)
            myAdapter.notifyDataSetChanged();
        else {
            myAdapter = new AdapterCategory(ActivityCategory.this, listAnime);
        }

    }

    @Override
    public void handleTaskResult(String result) {
//        Toast.makeText(ActivityCategory.this, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleTaskResultRetrieveCategories(List<Category> result) {
        if (result.size() > 0) {

            listAnime.clear();
            listAnime.addAll(result);
            if (myAdapter != null)
                myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
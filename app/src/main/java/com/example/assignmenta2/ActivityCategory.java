package com.example.assignmenta2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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

public class ActivityCategory extends AppCompatActivity implements AsyncTaskDelegate {

    private final String JSON_URL = "http://jservice.io/api/categories?count=100";

    private JsonArrayRequest request;
    AppDatabase db;
    private RequestQueue requestQueue;
    private List<Category> listAnime;
    private RecyclerView category_recycler;
    AdapterCategory myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        db = AppDatabase.getInstance(ActivityCategory.this);
        listAnime = new ArrayList<>();
        category_recycler = findViewById(R.id.category_recycler);

        RetrieveCategoriesAsyncTask insertBooksAsyncTask = new RetrieveCategoriesAsyncTask();
        insertBooksAsyncTask.setDatabase(db);
        insertBooksAsyncTask.setDelegate(ActivityCategory.this);
        insertBooksAsyncTask.execute();

        gsonRequest();
    }

    private void gsonRequest() {

        request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Category categories = new Category();

                        categories.title = jsonObject.getString("title");

                        listAnime.add(categories);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                InsertCategoriesAsyncTask insertCategoriesAsyncTask = new InsertCategoriesAsyncTask();
                insertCategoriesAsyncTask.setDatabase(db);
                insertCategoriesAsyncTask.setDelegate(ActivityCategory.this);
                insertCategoriesAsyncTask.execute();

                setuprecyclerview(listAnime);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        requestQueue = Volley.newRequestQueue(ActivityCategory.this);
        requestQueue.add(request);
    }


    private void setuprecyclerview(List<Category> listAnime) {

        myAdapter = new AdapterCategory(this, listAnime);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3, LinearLayoutManager.VERTICAL, false);

        category_recycler.setLayoutManager(gridLayoutManager);

        category_recycler.setAdapter(myAdapter);
    }

    @Override
    public void handleTaskResult(String result) {
        Toast.makeText(ActivityCategory.this, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleTaskResultRetrieveCategories(List<Category> result) {
        if (result.size() > 0) {
            if (myAdapter != null)
                myAdapter.notifyDataSetChanged();
        }
    }
}
package com.example.assignmenta2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RandomFactActivity extends AppCompatActivity {
    TextView fact;
    private String JSON_URL = "https://uselessfacts.jsph.pl/random.json?language=en";
    private JsonObjectRequest request;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_randomfact);
        getSupportActionBar().setTitle("Random Facts");
        fact = findViewById(R.id.fact);
        getFactAPI();
    }

    private void getFactAPI() {
        fact.setText(R.string.fetch_fact);
        request = new JsonObjectRequest(JSON_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.length() > 0) {
                    try {
                        fact.setText(response.getString("text"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(RandomFactActivity.this, "Something went wrong, try again later.", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue = Volley.newRequestQueue(RandomFactActivity.this);
        requestQueue.add(request);
    }

    public void onRefresh(View view) {
        getFactAPI();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
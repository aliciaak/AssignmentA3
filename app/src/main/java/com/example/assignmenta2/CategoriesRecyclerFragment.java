package com.example.assignmenta2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoriesRecyclerFragment extends Fragment {

    private RecyclerView recyclerView;

    public CategoriesRecyclerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_categories_recycler, container, false);


        recyclerView = view.findViewById(R.id.rv2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        final CategoriesAdapter categoriesAdapter = new CategoriesAdapter();
        final RequestQueue requestQueue =  Volley.newRequestQueue(getActivity());
        String url = "http://jservice.io/api/categories?count=100";
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            public void onResponse(String response) {
                Gson gson = new Gson();
                Category[] triviaArray = gson.fromJson(response, Category[].class);
                List<Category> triviaList = Arrays.asList(triviaArray);
                categoriesAdapter.setData(triviaList);
                recyclerView.setAdapter(categoriesAdapter);
                requestQueue.stop();
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(),"The request failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                requestQueue.stop();
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener,
                errorListener);

        requestQueue.add(stringRequest);

        return view;
    }
}

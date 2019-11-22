package com.example.assignmenta2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {

    private List<Category> categoryToAdapt;

    public void setData(List<Category> categoryToAdapt) {
        this.categoryToAdapt = categoryToAdapt;
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.category, parent, false);

        CategoriesViewHolder categoriesViewHolder = new CategoriesViewHolder(view);
        return categoriesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        final Category categoryAtPosition = categoryToAdapt.get(position);
        final Context context = holder.view.getContext();
        holder.bind(categoryAtPosition);
    }

    @Override
    public int getItemCount() {
        return categoryToAdapt.size();
    }

    public static class CategoriesViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView categoryText;
        public ConstraintLayout layoutmain;

        public CategoriesViewHolder(View v) {
            super(v);
            view = v;
            categoryText = v.findViewById(R.id.categoryt);
            layoutmain = v.findViewById(R.id.layoutmain);
        }

        public void bind(final Category category) {
            categoryText.setText(category.getTitle());

            //like ActivityCategory, alters background of category to random colors
            // https://www.tutorialspoint.com/how-to-set-random-background-for-recyclerview-in-android
            Random rnd = new Random();
            int currentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            layoutmain.setBackgroundColor(currentColor);
        }
    }

}

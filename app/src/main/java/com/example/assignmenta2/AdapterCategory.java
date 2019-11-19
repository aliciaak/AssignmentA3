package com.example.assignmenta2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.MyViewHolder> {
    private Context mContext;
    private List<Category> categoryData;

    public AdapterCategory(Context mContext, List<Category> categoryData) {
        this.mContext = mContext;
        this.categoryData = categoryData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        view = inflater.inflate(R.layout.category_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.category_title.setText(categoryData.get(position).title);
    }

    @Override
    public int getItemCount()
    {
        return categoryData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView category_title;
        public ImageView category_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            category_title = itemView.findViewById(R.id.category_title);
            category_img = itemView.findViewById(R.id.category_img);
        }
    }
}
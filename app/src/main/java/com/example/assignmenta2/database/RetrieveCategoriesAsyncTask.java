package com.example.assignmenta2.database;

import android.os.AsyncTask;

import com.example.assignmenta2.Category;

import java.util.List;

public class RetrieveCategoriesAsyncTask extends AsyncTask<Category, Integer, List<Category>> {

    private AsyncTaskDelegate delegate;
    private AppDatabase database;

    public void setDelegate(AsyncTaskDelegate delegate) {
        this.delegate = delegate;
    }

    public void setDatabase(AppDatabase database) {
        this.database = database;
    }

    @Override
    protected List<Category> doInBackground(Category... categories) {
        return database.categoryDao().getAll();
    }

    @Override
    protected void onPostExecute(List<Category> result) {
        delegate.handleTaskResultRetrieveCategories(result);
    }
}
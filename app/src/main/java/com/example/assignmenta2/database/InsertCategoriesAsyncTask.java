package com.example.assignmenta2.database;

import android.os.AsyncTask;

import com.example.assignmenta2.Category;


public class InsertCategoriesAsyncTask extends AsyncTask<Category, Integer, String> {

    private AsyncTaskDelegate delegate;

    private AppDatabase database;

    public void setDelegate(AsyncTaskDelegate delegate) {
        this.delegate = delegate;
    }

    public void setDatabase(AppDatabase database) {
        this.database = database;
    }

    @Override
    protected String doInBackground(Category... categories) {
        database.categoryDao().insertAll(categories);
        return "Here are all the categories. Have fun!";
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.handleTaskResult(result);
    }
}
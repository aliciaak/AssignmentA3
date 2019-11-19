package com.example.assignmenta2.database;

import com.example.assignmenta2.Category;

import java.util.List;

public interface AsyncTaskDelegate {
    void handleTaskResult(String result);

    void handleTaskResultRetrieveCategories(List<Category> result);
}
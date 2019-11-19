package com.example.assignmenta2.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.assignmenta2.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    // Data Access Object for quiz categories

    @Query("SELECT * FROM Category")
    List<Category> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Category[] categories);

    @Delete
    void delete(Category category);
}
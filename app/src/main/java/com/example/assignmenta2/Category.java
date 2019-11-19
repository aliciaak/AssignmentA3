package com.example.assignmenta2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class Category implements Serializable {
    @PrimaryKey
    public int id;

    @SerializedName("title")
    public String title;

    public int getId() {
        return id; }

    public void setId(int id) {
        this.id = id; }

    public String getTitle() {
        return title; }

    public void setTitle(String title) {
        this.title = title; }
}
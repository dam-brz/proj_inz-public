package com.projekt.zaliczeniowy.penny_pincher.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "categories", indices = @Index(value = {"category_name"}, unique = true))
public class Category implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="category_id")
    private long categoryId;

    @NonNull
    @ColumnInfo(name="category_name")
    private String categoryName;

    public Category(@NonNull String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    @NonNull
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(@NonNull String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return categoryName;
    }
}

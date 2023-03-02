package com.projekt.zaliczeniowy.penny_pincher.model.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.projekt.zaliczeniowy.penny_pincher.dao.CategoryRepository;
import com.projekt.zaliczeniowy.penny_pincher.model.Category;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {

    private final CategoryRepository categoryRepository;
    private final LiveData<List<Category>> categories;

    public CategoryViewModel(@NonNull Application application) {
        super(application);

        categoryRepository = new CategoryRepository(application);
        categories = categoryRepository.getCategories();
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public void addCategory(Category category) {
        categoryRepository.addCategory(category);
    }

    public void deleteCategory(Category categoryToDelete, long defaultCategoryId) {categoryRepository.deleteCategory(categoryToDelete, defaultCategoryId);}
}

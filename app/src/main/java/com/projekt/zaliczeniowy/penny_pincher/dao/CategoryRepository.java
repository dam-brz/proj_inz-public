package com.projekt.zaliczeniowy.penny_pincher.dao;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.projekt.zaliczeniowy.penny_pincher.model.Category;

import java.util.List;

public class CategoryRepository {

    private final ExpenseDao expenseDao;
    private final LiveData<List<Category>> categories;

    public CategoryRepository(Application application) {
        PennypincherDatabase database = PennypincherDatabase.getDatabase(application);
        expenseDao = database.expenseDao();
        categories = expenseDao.getAllCategories();
    }

    public LiveData<List<Category>> getCategories() {
        return categories;
    }

    public void addCategory(Category category) {
        PennypincherDatabase.EXECUTOR_SERVICE.execute(() -> expenseDao.addCategory(category));
    }
    public void deleteCategory(Category category, long defaultCategoryId) {
        PennypincherDatabase.EXECUTOR_SERVICE.execute(() -> expenseDao.setExpenseDefaultCategory(defaultCategoryId, category.getCategoryId()));
        PennypincherDatabase.EXECUTOR_SERVICE.execute(() -> expenseDao.deleteCategory(category));
    }

}

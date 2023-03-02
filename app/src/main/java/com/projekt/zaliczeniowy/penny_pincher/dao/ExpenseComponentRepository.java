package com.projekt.zaliczeniowy.penny_pincher.dao;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.projekt.zaliczeniowy.penny_pincher.model.Expense;
import com.projekt.zaliczeniowy.penny_pincher.model.ExpenseComponent;

import java.util.List;

public class ExpenseComponentRepository {

    private final ExpenseDao expenseDao;
    private final LiveData<List<ExpenseComponent>> expenseComponents;

    public ExpenseComponentRepository(Application application, long expenseId) {
        PennypincherDatabase database = PennypincherDatabase.getDatabase(application);
        expenseDao = database.expenseDao();
        expenseComponents = expenseDao.getExpenseComponents(expenseId);
    }

    public LiveData<List<ExpenseComponent>> getExpenseComponents() {
        return expenseComponents;
    }

    public void addExpenseComponent(ExpenseComponent expenseComponent) {
        PennypincherDatabase.EXECUTOR_SERVICE.execute(() -> expenseDao.addExpenseComponent(expenseComponent));
    }

    public void deleteExpenseComponent(ExpenseComponent expenseComponent) {
        PennypincherDatabase.EXECUTOR_SERVICE.execute(() -> expenseDao.deleteExpenseComponent(expenseComponent));
    }

    public void updateExpense(Expense expense) {
        PennypincherDatabase.EXECUTOR_SERVICE.execute(() -> expenseDao.updateExpense(expense));
    }
}

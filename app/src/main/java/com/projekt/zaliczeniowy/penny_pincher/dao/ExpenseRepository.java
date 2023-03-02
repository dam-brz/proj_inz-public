package com.projekt.zaliczeniowy.penny_pincher.dao;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.projekt.zaliczeniowy.penny_pincher.model.BarChartModel;
import com.projekt.zaliczeniowy.penny_pincher.model.ExpenseWithCategory;
import com.projekt.zaliczeniowy.penny_pincher.model.Expense;
import com.projekt.zaliczeniowy.penny_pincher.model.PieChartModel;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

public class ExpenseRepository {

    private final ExpenseDao expenseDao;
    private final LiveData<List<ExpenseWithCategory>> expenses;

    public ExpenseRepository(Application application) {
        PennypincherDatabase database = PennypincherDatabase.getDatabase(application);
        expenseDao = database.expenseDao();
        expenses = expenseDao.getExpensesWithCategories();
    }

    public LiveData<List<ExpenseWithCategory>> getExpensesWithCategories() {
        return expenses;
    }

    public void addExpense(Expense expense) {
        PennypincherDatabase.EXECUTOR_SERVICE.execute(() -> expenseDao.addExpense(expense));
    }

    public void deleteExpense(Expense expense) {
        PennypincherDatabase.EXECUTOR_SERVICE.execute(() -> expenseDao.deleteExpense(expense));
    }

    public void setExpenseSettled(long expenseId, boolean isSettled) {
        PennypincherDatabase.EXECUTOR_SERVICE.execute(() -> expenseDao.setExpenseSettled(expenseId, isSettled));
    }

    public Single<Long> add(Expense expense) {
        return expenseDao.add(expense);
    }

    public List<PieChartModel> getTotalExpensesValuesByCategories() {
        return expenseDao.getPieChartModels();
    }

    public List<BarChartModel> getBarChartModels() {
        return expenseDao.getBarCharModels();
    }
}

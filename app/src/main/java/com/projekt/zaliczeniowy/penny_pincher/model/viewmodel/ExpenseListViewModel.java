package com.projekt.zaliczeniowy.penny_pincher.model.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.projekt.zaliczeniowy.penny_pincher.dao.ExpenseRepository;
import com.projekt.zaliczeniowy.penny_pincher.model.ExpenseWithCategory;
import com.projekt.zaliczeniowy.penny_pincher.model.Expense;

import java.util.List;


public class ExpenseListViewModel extends AndroidViewModel {

    private final ExpenseRepository expenseRepository;
    private final LiveData<List<ExpenseWithCategory>> expensesWithCategories;

    public ExpenseListViewModel(@NonNull Application application) {
        super(application);

        expenseRepository = new ExpenseRepository(application);
        expensesWithCategories = expenseRepository.getExpensesWithCategories();
    }

    public LiveData<List<ExpenseWithCategory>> getExpensesWithCategories() {
        return expensesWithCategories;
    }

    public void addExpense(Expense expense) {
        expenseRepository.addExpense(expense);
    }

    public void deleteExpense(Expense expense) {
        expenseRepository.deleteExpense(expense);
    }

    public void setExpenseSettled(long expenseId, boolean isSettled) {
        expenseRepository.setExpenseSettled(expenseId, isSettled);
    }
}

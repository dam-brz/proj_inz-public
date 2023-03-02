package com.projekt.zaliczeniowy.penny_pincher.model.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.projekt.zaliczeniowy.penny_pincher.dao.ExpenseComponentRepository;
import com.projekt.zaliczeniowy.penny_pincher.model.Expense;
import com.projekt.zaliczeniowy.penny_pincher.model.ExpenseComponent;

import java.util.List;

public class ExpenseComponentsListViewModel extends AndroidViewModel {


    private final ExpenseComponentRepository expenseComponentRepository;
    private final LiveData<List<ExpenseComponent>> expenseComponents;

    public ExpenseComponentsListViewModel(@NonNull Application application, long expenseId) {
        super(application);

        expenseComponentRepository = new ExpenseComponentRepository(application, expenseId);
        expenseComponents = expenseComponentRepository.getExpenseComponents();
    }

    public LiveData<List<ExpenseComponent>> getExpenseComponents() {
        return expenseComponents;
    }

    public void addExpenseComponent(ExpenseComponent expenseComponent) {
        expenseComponentRepository.addExpenseComponent(expenseComponent);
    }

    public void deleteExpenseComponent(ExpenseComponent expenseComponent) {
        expenseComponentRepository.deleteExpenseComponent(expenseComponent);
    }

    public void upDateExpense(Expense expense) {
        expenseComponentRepository.updateExpense(expense);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private Application application;
        private long expenseId;

        public Factory(Application application, long expenseId) {
            this.application = application;
            this.expenseId = expenseId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new ExpenseComponentsListViewModel(application, expenseId);
        }
    }


}

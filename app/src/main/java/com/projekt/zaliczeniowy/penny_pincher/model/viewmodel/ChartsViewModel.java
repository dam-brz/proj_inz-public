package com.projekt.zaliczeniowy.penny_pincher.model.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.projekt.zaliczeniowy.penny_pincher.dao.ExpenseRepository;
import com.projekt.zaliczeniowy.penny_pincher.model.BarChartModel;
import com.projekt.zaliczeniowy.penny_pincher.model.Expense;
import com.projekt.zaliczeniowy.penny_pincher.model.ExpenseWithCategory;
import com.projekt.zaliczeniowy.penny_pincher.model.PieChartModel;

import java.util.List;

public class ChartsViewModel extends AndroidViewModel {

    ExpenseRepository expenseRepository;
    List<PieChartModel> pieChartModels;

    public ChartsViewModel(@NonNull Application application) {
        super(application);
        expenseRepository = new ExpenseRepository(application);
        pieChartModels = expenseRepository.getTotalExpensesValuesByCategories();
    }

    public List<PieChartModel> getPieChartModels() {
        return pieChartModels;
    }

    public List<BarChartModel> getBarchartModels() {
        return expenseRepository.getBarChartModels();
    }
}

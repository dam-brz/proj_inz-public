package com.projekt.zaliczeniowy.penny_pincher.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.projekt.zaliczeniowy.penny_pincher.model.BarChartModel;
import com.projekt.zaliczeniowy.penny_pincher.model.Category;
import com.projekt.zaliczeniowy.penny_pincher.model.ExpenseComponent;
import com.projekt.zaliczeniowy.penny_pincher.model.ExpenseWithCategory;
import com.projekt.zaliczeniowy.penny_pincher.model.Expense;
import com.projekt.zaliczeniowy.penny_pincher.model.PieChartModel;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface ExpenseDao {

    @Transaction
    @Query("SELECT * FROM expenses ORDER BY expense_date DESC")
    LiveData<List<ExpenseWithCategory>> getExpensesWithCategories();
    @Transaction
    @Query("SELECT * FROM expenses ORDER BY expense_date DESC")
    List<Expense> getExpenses();
    @Insert()
    void addExpense(Expense expense);
    @Insert()
    Single<Long> add(Expense expense);
    @Query("UPDATE expenses SET settled = :isSettled WHERE expense_id = :expenseId")
    void setExpenseSettled(long expenseId, boolean isSettled);
    @Update
    void updateExpense(Expense expense);
    @Delete
    void deleteExpense(Expense expense);
    @Insert()
    void addCategory(Category category);
    @Query("SELECT * FROM CATEGORIES")
    LiveData<List<Category>> getAllCategories();
    @Delete
    void deleteCategory(Category category);
    @Query("UPDATE expenses SET category_id = :defaultCategoryId WHERE category_id = :categoryToDeleteId")
    void setExpenseDefaultCategory(long defaultCategoryId, long categoryToDeleteId);
    @Query("SELECT * FROM expense_components WHERE expense_id = :expenseId")
    LiveData<List<ExpenseComponent>> getExpenseComponents(long expenseId);
    @Insert()
    void addExpenseComponent(ExpenseComponent expenseComponent);
    @Delete
    void deleteExpenseComponent(ExpenseComponent expenseComponent);
    @Query(" SELECT c.category_name, SUM(e.expense_total_value) " +
            "FROM expenses e " +
            "INNER JOIN categories c ON e.category_id = c.category_id " +
            "WHERE settled = 1 " +
            "GROUP BY category_name")
    List<PieChartModel> getPieChartModels();
    @Query("SELECT expense_date as date, expense_total_value as value " +
            "FROM expenses WHERE settled = 1")
    List<BarChartModel> getBarCharModels();

}

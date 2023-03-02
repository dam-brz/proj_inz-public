package com.projekt.zaliczeniowy.penny_pincher.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

public class ExpenseWithCategory implements Serializable {

    @Embedded private Expense expense;

       @Relation(
            parentColumn = "category_id",
            entityColumn = "category_id"
    )
    private Category category;

    public ExpenseWithCategory(Expense expense, Category category) {
        this.expense = expense;
        this.category = category;
    }

    public Expense getExpense() {
        return expense;
    }

    public void setExpense(Expense expense) {
        this.expense = expense;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

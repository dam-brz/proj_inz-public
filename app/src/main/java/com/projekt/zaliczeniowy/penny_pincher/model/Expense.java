package com.projekt.zaliczeniowy.penny_pincher.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.projekt.zaliczeniowy.penny_pincher.converter.type.TimestampConverter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity(tableName = "expenses")
public class Expense implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="expense_id")
    private long expenseId;

    @NonNull
    @ColumnInfo(name="expense_name")
    private String expenseName;

    @ColumnInfo(name="category_id")
    private long categoryId;

    @NonNull
    @ColumnInfo(name="expense_date")
    @TypeConverters({TimestampConverter.class})
    private Date expenseDate;

    @NonNull
    @ColumnInfo(name="expense_total_value")
    private BigDecimal expenseTotalValue;

    @ColumnInfo(name="settled")
    private boolean isSettled;

    public Expense(@NonNull String expenseName, long categoryId, @NonNull Date expenseDate, @NonNull BigDecimal expenseTotalValue, boolean isSettled) {
        this.expenseName = expenseName;
        this.categoryId = categoryId;
        this.expenseDate = expenseDate;
        this.expenseTotalValue = expenseTotalValue;
        this.isSettled = isSettled;
    }

    public long getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(long expenseId) {
        this.expenseId = expenseId;
    }

    @NonNull
    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(@NonNull String expenseName) {
        this.expenseName = expenseName;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    @NonNull
    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(@NonNull Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    @NonNull
    public BigDecimal getExpenseTotalValue() {
        return expenseTotalValue;
    }

    public void setExpenseTotalValue(@NonNull BigDecimal expenseTotalValue) {
        this.expenseTotalValue = expenseTotalValue;
    }

    public boolean isSettled() {
        return isSettled;
    }

    public void setSettled(boolean settled) {
        isSettled = settled;
    }
}

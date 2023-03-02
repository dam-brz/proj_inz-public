package com.projekt.zaliczeniowy.penny_pincher.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity(tableName = "expense_components")
public class ExpenseComponent implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="component_id")
    private long component_id;

    @ColumnInfo(name="expense_id")
    private long componentExpenseId;

    @NonNull
    @ColumnInfo(name="description")
    private String description;

    @NonNull
    @ColumnInfo(name="amount")
    private BigDecimal amount;

    public ExpenseComponent(long componentExpenseId, @NonNull String description, @NonNull BigDecimal amount) {
        this.component_id = component_id;
        this.componentExpenseId = componentExpenseId;
        this.description = description;
        this.amount = amount;
    }

    public long getComponent_id() {
        return component_id;
    }

    public void setComponent_id(long component_id) {
        this.component_id = component_id;
    }

    public long getComponentExpenseId() {
        return componentExpenseId;
    }

    public void setComponentExpenseId(long componentExpenseId) {
        this.componentExpenseId = componentExpenseId;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(@NonNull BigDecimal amount) {
        this.amount = amount;
    }
}

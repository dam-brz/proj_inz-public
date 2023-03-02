package com.projekt.zaliczeniowy.penny_pincher.model;

import androidx.room.ColumnInfo;

import java.io.Serializable;
import java.math.BigDecimal;

public class PieChartModel implements Serializable {

    @ColumnInfo(name = "category_name")
    private String category;

    @ColumnInfo(name = "SUM(e.expense_total_value)")
    private BigDecimal value;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}

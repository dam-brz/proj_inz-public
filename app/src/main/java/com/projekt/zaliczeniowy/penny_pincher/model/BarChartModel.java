package com.projekt.zaliczeniowy.penny_pincher.model;

import androidx.room.ColumnInfo;

import java.math.BigDecimal;
import java.util.Date;

public class BarChartModel {

    @ColumnInfo(name = "value")
    private BigDecimal totalValue;

    @ColumnInfo(name = "date")
    private Date date;


    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getMonth() {
        return date.getMonth();
    }

    public double getDoubleValue() {
        return totalValue.doubleValue();
    }
}

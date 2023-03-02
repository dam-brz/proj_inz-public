package com.projekt.zaliczeniowy.penny_pincher;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.projekt.zaliczeniowy.penny_pincher.dao.ExpenseRepository;
import com.projekt.zaliczeniowy.penny_pincher.model.BarChartModel;
import com.projekt.zaliczeniowy.penny_pincher.model.Expense;
import com.projekt.zaliczeniowy.penny_pincher.model.ExpenseWithCategory;
import com.projekt.zaliczeniowy.penny_pincher.model.PieChartModel;
import com.projekt.zaliczeniowy.penny_pincher.model.viewmodel.ChartsViewModel;
import com.projekt.zaliczeniowy.penny_pincher.ui.utils.AnimationHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BarChartActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        BarChart barChart = findViewById(R.id.barChart);
        Thread thread = new Thread() {
            @Override
            public void run() {
                ChartsViewModel chartsViewModel = new ChartsViewModel(getApplication());
                Map<Integer, Double> expensesPerMonth = chartsViewModel.getBarchartModels()
                        .stream()
                        .collect(Collectors.groupingBy(BarChartModel::getMonth,
                                Collectors.summingDouble(BarChartModel::getDoubleValue)));

                ArrayList<BarEntry> barEntries = new ArrayList<>();
                expensesPerMonth.forEach((integer, aDouble) -> {
                    barEntries.add(new BarEntry(Float.parseFloat(String.valueOf(integer)),
                            Float.parseFloat(aDouble.toString())));
                });

                BarDataSet barDataSet = new BarDataSet(barEntries, "Miesiąc");
                barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                barDataSet.setValueTextColor(Color.BLACK);
                barDataSet.setValueTextSize(16f);

                XAxis xAxis = barChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setGranularity(1f);
                xAxis.setDrawAxisLine(false);
                xAxis.setDrawGridLines(false);

                YAxis leftAxis = barChart.getAxisLeft();
                leftAxis.setDrawAxisLine(false);

                YAxis rightAxis = barChart.getAxisRight();
                rightAxis.setDrawAxisLine(false);
                barChart.setFitBars(false);
                barChart.setData(new BarData(barDataSet));

            }
        };
        thread.start();
        barChart.animateY(1500);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimationHelper.customType(BarChartActivity.this, "right-to-left");
    }
}
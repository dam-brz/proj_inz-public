package com.projekt.zaliczeniowy.penny_pincher;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.projekt.zaliczeniowy.penny_pincher.model.PieChartModel;
import com.projekt.zaliczeniowy.penny_pincher.model.viewmodel.ChartsViewModel;
import com.projekt.zaliczeniowy.penny_pincher.ui.utils.AnimationHelper;

import java.util.ArrayList;
import java.util.List;

public class PieChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        PieChart pieChart = findViewById(R.id.pieChart);

        ArrayList<PieEntry> data = new ArrayList<>();

        Thread thread = new Thread() {
            @Override
            public void run() {
                ChartsViewModel chartsViewModel = new ChartsViewModel(getApplication());
                List<PieChartModel> pieChartModels = chartsViewModel.getPieChartModels();

                for (PieChartModel model : pieChartModels) {
                    data.add(new PieEntry(model.getValue().floatValue(), model.getCategory()));
                }

                PieDataSet pieDataSet = new PieDataSet(data, "");
                pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                pieDataSet.setValueTextColor(Color.BLACK);
                pieDataSet.setValueTextSize(16f);

                pieChart.setData(new PieData(pieDataSet));
                pieChart.getDescription().setEnabled(false);
                pieChart.setCenterText(getResources().getString(R.string.btn_by_category));
                pieChart.animate();
            }
        };

        thread.start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimationHelper.customType(PieChartActivity.this, "right-to-left");
    }
}
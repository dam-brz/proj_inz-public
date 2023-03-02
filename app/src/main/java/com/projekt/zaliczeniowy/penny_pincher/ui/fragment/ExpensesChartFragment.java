package com.projekt.zaliczeniowy.penny_pincher.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.projekt.zaliczeniowy.penny_pincher.BarChartActivity;
import com.projekt.zaliczeniowy.penny_pincher.PieChartActivity;
import com.projekt.zaliczeniowy.penny_pincher.R;
import com.projekt.zaliczeniowy.penny_pincher.ui.utils.AnimationHelper;


public class ExpensesChartFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_expenses_chart, container, false);

        Button btnBarChart = view.findViewById(R.id.btnBarChart);
        Button btnPieChart = view.findViewById(R.id.btnPieChart);
        btnBarChart.setOnClickListener(view1 -> {
            startActivity(new Intent(view1.getContext(), BarChartActivity.class));
            AnimationHelper.customType(view1.getContext(), "left-to-right");
        });

        btnPieChart.setOnClickListener(view12 -> {
            startActivity(new Intent(view12.getContext(), PieChartActivity.class));
            AnimationHelper.customType(view12.getContext(), "left-to-right");
        });

        return view;
    }
}
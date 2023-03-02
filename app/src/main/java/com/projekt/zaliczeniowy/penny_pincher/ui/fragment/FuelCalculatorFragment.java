package com.projekt.zaliczeniowy.penny_pincher.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.projekt.zaliczeniowy.penny_pincher.MainActivity;
import com.projekt.zaliczeniowy.penny_pincher.R;
import com.projekt.zaliczeniowy.penny_pincher.dao.ExpenseComponentRepository;
import com.projekt.zaliczeniowy.penny_pincher.dao.ExpenseRepository;
import com.projekt.zaliczeniowy.penny_pincher.model.Expense;
import com.projekt.zaliczeniowy.penny_pincher.model.ExpenseComponent;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

public class FuelCalculatorFragment extends Fragment {

    private BigDecimal fuelNeeded;
    private BigDecimal fuelPrice;
    private BigDecimal totalFuelPrice;
    private BigDecimal mileage;
    private BigDecimal tripDistance;
    private TextView tvFuelNeeded;
    private TextView tvTotalFuelPrice;
    private Button btnAddAsExpense;
    private EditText etTripDistance;
    private EditText etFuelPrice;
    private EditText etMileage;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fuel_calculator, container, false);

        etTripDistance = view.findViewById(R.id.etTripDistance);
        etFuelPrice = view.findViewById(R.id.etFuelPrice);
        etMileage = view.findViewById(R.id.etMileage);
        Button btnCalculate = view.findViewById(R.id.btnCalculate);

        tvFuelNeeded = view.findViewById(R.id.tvFuelNeeded);
        tvTotalFuelPrice = view.findViewById(R.id.tvTotalFuelPrice);
        btnAddAsExpense = view.findViewById(R.id.btnAddAsExpense);
        btnAddAsExpense.setVisibility(View.INVISIBLE);

        btnCalculate.setOnClickListener(getCalculateButtonOnClickListener());
        btnAddAsExpense.setOnClickListener(getAddAsExpenseButtonOnClickListener());

        return view;
    }

    private View.OnClickListener getAddAsExpenseButtonOnClickListener() {
        return view -> {
            Thread thread =  new Thread() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void run() {
                    ExpenseRepository expenseRepository = new ExpenseRepository(requireActivity().getApplication());
                    long expenseId = expenseRepository
                            .add(new Expense(getString(R.string.default_expense_name), 2, Date.from(Instant.now()), totalFuelPrice, false))
                            .blockingGet();
                    ExpenseComponent expenseComponent = new ExpenseComponent(expenseId, getString(R.string.default_component_name), totalFuelPrice);
                    ExpenseComponentRepository expenseComponentRepository =
                            new ExpenseComponentRepository(requireActivity().getApplication(), expenseId);
                    expenseComponentRepository
                            .addExpenseComponent(expenseComponent);
                    requireActivity()
                            .runOnUiThread(() -> ((MainActivity) requireActivity()).getViewPager().setCurrentItem(0));
                }
            };
            thread.start();
        };
    }

    @SuppressLint("DefaultLocale")
    private View.OnClickListener getCalculateButtonOnClickListener() {
        return view -> {
            BigDecimal divider = new BigDecimal(String.valueOf(100));

            if (etMileage.getText().toString().isEmpty()) {
                etMileage.setError(getString(R.string.not_empty));
            } else {
                mileage = new BigDecimal(etMileage.getText().toString());
            }

            if (etTripDistance.getText().toString().isEmpty()) {
                etTripDistance.setError(getString(R.string.not_empty));
            } else {
                tripDistance = new BigDecimal(etTripDistance.getText().toString());
            }

            if (etFuelPrice.getText().toString().isEmpty()) {
                etFuelPrice.setError(getString(R.string.not_empty));
            } else {
                fuelPrice = new BigDecimal(etFuelPrice.getText().toString());
            }

            if (mileage != null && tripDistance != null && fuelPrice != null) {
                fuelNeeded  = Calculator.calculateFuelNeeded(mileage, tripDistance, divider);
                totalFuelPrice  = Calculator.totalFuelPrice(fuelNeeded, fuelPrice);
                tvFuelNeeded.setText(String.format(getString(R.string.float_point), fuelNeeded));
                tvTotalFuelPrice.setText(String.format(getString(R.string.float_point), totalFuelPrice));
                btnAddAsExpense.setVisibility(View.VISIBLE);
            }
        };
    }

    private static class Calculator {

        static BigDecimal calculateFuelNeeded(BigDecimal mileage, BigDecimal tripDistance, BigDecimal divider) {
            return mileage
                    .multiply(tripDistance)
                    .divide(divider);
        }

        static BigDecimal totalFuelPrice(BigDecimal fuelNeeded, BigDecimal fuelPrice) {
            return fuelNeeded.multiply(fuelPrice);
        }
    }
}
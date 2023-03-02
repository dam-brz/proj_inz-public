package com.projekt.zaliczeniowy.penny_pincher.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.projekt.zaliczeniowy.penny_pincher.R;
import com.projekt.zaliczeniowy.penny_pincher.ui.fragment.ExpenseListFragment;
import com.projekt.zaliczeniowy.penny_pincher.ui.fragment.ExpensesChartFragment;
import com.projekt.zaliczeniowy.penny_pincher.ui.fragment.FuelCalculatorFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public static final int[] TABS = new int[] {
            R.string.tab_list,
            R.string.tab_fuel_calculator,
            R.string.tab_expense_chart
    };

    public static final int[] ICONS = new int[] {
            R.drawable.ic_expenses_list,
            R.drawable.ic_fuel_calculator,
            R.drawable.ic_expenses_chart
    };

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 1:
                fragment = new FuelCalculatorFragment();
                break;
            case 2:
                fragment = new ExpensesChartFragment();
                break;
            default:
                fragment = new ExpenseListFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return TABS.length;
    }
}

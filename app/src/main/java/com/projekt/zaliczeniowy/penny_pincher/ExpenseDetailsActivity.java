package com.projekt.zaliczeniowy.penny_pincher;

import static com.projekt.zaliczeniowy.penny_pincher.ui.fragment.ExpenseListFragment.SPINNER_HINT_ID;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.projekt.zaliczeniowy.penny_pincher.model.Category;
import com.projekt.zaliczeniowy.penny_pincher.model.Expense;
import com.projekt.zaliczeniowy.penny_pincher.model.ExpenseComponent;
import com.projekt.zaliczeniowy.penny_pincher.model.ExpenseWithCategory;
import com.projekt.zaliczeniowy.penny_pincher.model.viewmodel.CategoryViewModel;
import com.projekt.zaliczeniowy.penny_pincher.model.viewmodel.ExpenseComponentsListViewModel;
import com.projekt.zaliczeniowy.penny_pincher.ui.adapter.CategorySpinnerAdapter;
import com.projekt.zaliczeniowy.penny_pincher.ui.adapter.ExpenseComponentsListAdapter;
import com.projekt.zaliczeniowy.penny_pincher.ui.utils.AnimationHelper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ExpenseDetailsActivity extends AppCompatActivity {

    private ExpenseWithCategory expenseWithCategory;
    private TextView tvExpenseCost;
    private TextView tvExpenseName;
    private TextView tvCategory;
    private TextView tvExpenseDate;
    private ExpenseComponentsListAdapter expenseComponentsListAdapter;
    private ExpenseComponentsListViewModel expenseComponentViewModel;
    private Category category;
    private Date expenseDate;

    @SuppressLint({"SimpleDateFormat", "DefaultLocale"})
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_details);
        Intent intent = getIntent();
        expenseWithCategory = (ExpenseWithCategory) intent.getSerializableExtra("expense");
        category = expenseWithCategory.getCategory();
        expenseDate = expenseWithCategory.getExpense().getExpenseDate();

        tvCategory = findViewById(R.id.tvEditCategoryName);
        tvExpenseName = findViewById(R.id.tvEditExpenseName);
        tvExpenseDate = findViewById(R.id.tvEditExpenseDate);
        tvExpenseCost = findViewById(R.id.tvEditExpenseCost);

        tvCategory.setText(expenseWithCategory.getCategory().getCategoryName());
        tvExpenseName.setText(expenseWithCategory.getExpense().getExpenseName());
        tvExpenseDate.setText(new SimpleDateFormat(getResources().getString(R.string.default_date_format)).format(expenseWithCategory.getExpense().getExpenseDate()));
        tvExpenseCost.setText(String.format(getString(R.string.float_point), expenseWithCategory.getExpense().getExpenseTotalValue()));

        RecyclerView rvExpenseComponentsList = findViewById(R.id.rvExpenseComponents);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        expenseComponentsListAdapter = new ExpenseComponentsListAdapter();
        rvExpenseComponentsList.setLayoutManager(layoutManager);
        rvExpenseComponentsList.setAdapter(expenseComponentsListAdapter);

        expenseComponentViewModel = new ViewModelProvider(this, new ExpenseComponentsListViewModel.Factory(getApplication(), expenseWithCategory.getExpense().getExpenseId())).get(ExpenseComponentsListViewModel.class);
        expenseComponentViewModel.getExpenseComponents().observe(this, expenseComponents -> {
            expenseComponentsListAdapter.setExpenseComponents(expenseComponents);
            expenseComponentsListAdapter.setExpenseComponentsListViewModel(expenseComponentViewModel);
            tvExpenseCost.setText(String.format(getString(R.string.float_point),expenseComponentsListAdapter.getExpenseTotalAmount()));
        });

        CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        ImageButton ibEditExpense = findViewById(R.id.ibEditExpense);
        ibEditExpense.setOnClickListener(getIbEditExpenseOnClickListener(categoryViewModel));

        FloatingActionButton fabAddExpense = findViewById(R.id.fabAddExpenseComponent);
        fabAddExpense.setOnClickListener(getFabAddExpenseOnClickListener(expenseComponentViewModel));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AnimationHelper.customType(ExpenseDetailsActivity.this, "right-to-left");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private View.OnClickListener getFabAddExpenseOnClickListener(ExpenseComponentsListViewModel expenseComponentsListViewModel) {
        return view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            ViewGroup viewGroup = view.findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_add_expense_component, viewGroup, false);

            AlertDialog addExpenseComponentDialog = builder.setView(dialogView).create();
            addExpenseComponentDialog.show();

            EditText etExpenseComponentName = dialogView.findViewById(R.id.etExpenseComponentName);
            EditText etExpenseComponentAmount = dialogView.findViewById(R.id.etExpenseComponentAmount);
            Button btnAddExpenseComponent = dialogView.findViewById(R.id.btnAddExpenseComponent);
            Button btnCancel = dialogView.findViewById(R.id.btnCancel);


            btnAddExpenseComponent.setOnClickListener(getAddExpenseComponentDialogBtnAddOnClickListener(etExpenseComponentName, etExpenseComponentAmount, expenseComponentsListViewModel,addExpenseComponentDialog));

            btnCancel.setOnClickListener(getDialogBtnCancelOnClickListener(addExpenseComponentDialog));
        };
    }

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private View.OnClickListener getAddExpenseComponentDialogBtnAddOnClickListener(EditText etExpenseComponentName, EditText etExpenseComponentAmount, ExpenseComponentsListViewModel expenseComponentsListViewModel, AlertDialog addExpenseComponentDialog) {
        return view -> {
            String componentName = etExpenseComponentName.getText().toString();
            String componentValue = etExpenseComponentAmount.getText().toString();
            if (componentName.isEmpty()) {
                etExpenseComponentName.setError(getString(R.string.not_empty));
            }
            if (componentValue.isEmpty()) {
                etExpenseComponentAmount.setError(getString(R.string.not_empty));
            }

            if (!componentName.isEmpty() && !componentValue.isEmpty()) {
                if (componentName.length() > 15) {
                    etExpenseComponentName.setError(getString(R.string.name_limit));
                } else {

                    ExpenseComponent expense = new ExpenseComponent(expenseWithCategory.getExpense().getExpenseId(), componentName, new BigDecimal(componentValue));
                    expenseComponentsListViewModel.addExpenseComponent(expense);
                    tvExpenseCost.setText(String.format(getString(R.string.float_point),expenseComponentsListAdapter.getExpenseTotalAmount()));
                    addExpenseComponentDialog.dismiss();
                }
            }
        };
    }

    private View.OnClickListener getDialogBtnCancelOnClickListener(AlertDialog dialog) {
        return view -> dialog.dismiss();
    }

    private View.OnClickListener getIbEditExpenseOnClickListener(CategoryViewModel categoryViewModel) {
        return view -> {
            ViewGroup viewGroup = view.findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_edit_expense, viewGroup, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            AlertDialog editExpenseDialog = builder.setView(dialogView).create();
            Spinner sprCategory = dialogView.findViewById(R.id.sprCategory);
            EditText etExpenseName = dialogView.findViewById(R.id.etCategoryName);
            CalendarView cvExpenseDate = dialogView.findViewById(R.id.cvExpenseDate);
            Button btnSaveChanges = dialogView.findViewById(R.id.btnAdd);
            Button btnCancel = dialogView.findViewById(R.id.btnCancel);

            categoryViewModel.getCategories().observe(ExpenseDetailsActivity.this, categories -> {
                sprCategory.setAdapter(new CategorySpinnerAdapter(view.getContext(), R.layout.spinner_item, categories));
                sprCategory.setOnItemSelectedListener(getSpinnerOnItemSelectedListener());
                sprCategory.setSelection((int) category.getCategoryId() - 1);
            });
            etExpenseName.setText(expenseWithCategory.getExpense().getExpenseName());
            cvExpenseDate.setDate(expenseWithCategory.getExpense().getExpenseDate().getTime());
            editExpenseDialog.show();

            Calendar calendar = Calendar.getInstance();
            cvExpenseDate.setOnDateChangeListener((calendarView, i, i1, i2) -> {
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                calendar.set(Calendar.YEAR, i);
                expenseDate = calendar.getTime();
            });

            btnSaveChanges.setOnClickListener(getEditExpenseDialogBtnSaveChangesOnClickListener(etExpenseName, expenseComponentViewModel, expenseWithCategory.getExpense(), editExpenseDialog));
            btnCancel.setOnClickListener(getDialogBtnCancelOnClickListener(editExpenseDialog));


        };
    }

    private AdapterView.OnItemSelectedListener getSpinnerOnItemSelectedListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tvSpinnerText  = (TextView) view;
                category = (Category) adapterView.getSelectedItem();
                if (category.getCategoryId() == SPINNER_HINT_ID) {
                    tvSpinnerText.setError("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };
    }

    @SuppressLint("SimpleDateFormat")
    private View.OnClickListener getEditExpenseDialogBtnSaveChangesOnClickListener(EditText etExpenseName, ExpenseComponentsListViewModel expenseComponentsListViewModel, Expense expense, AlertDialog dialog) {
        return view -> {
            String expenseName = etExpenseName.getText().toString();
            if (expenseName.isEmpty()) {
                etExpenseName.setError(getString(R.string.not_empty));
            } else {
                if (expenseName.length() > 15) {
                    etExpenseName.setError(getString(R.string.name_limit));
                } else {
                    if (category.getCategoryId() != SPINNER_HINT_ID) {
                        expense.setExpenseName(expenseName);
                        expense.setExpenseDate(expenseDate);
                        expense.setCategoryId(category.getCategoryId());
                        expenseComponentsListViewModel.upDateExpense(expense);

                        tvExpenseName.setText(expenseName);
                        tvExpenseDate.setText(new SimpleDateFormat(getString(R.string.default_date_format)).format(expenseDate));
                        tvCategory.setText(category.getCategoryName());

                        dialog.hide();
                    }
                }
            }
        };
    }
}
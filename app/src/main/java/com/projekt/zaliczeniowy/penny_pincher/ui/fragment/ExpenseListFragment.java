package com.projekt.zaliczeniowy.penny_pincher.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.projekt.zaliczeniowy.penny_pincher.R;
import com.projekt.zaliczeniowy.penny_pincher.model.Category;
import com.projekt.zaliczeniowy.penny_pincher.model.Expense;
import com.projekt.zaliczeniowy.penny_pincher.model.viewmodel.CategoryViewModel;
import com.projekt.zaliczeniowy.penny_pincher.model.viewmodel.ExpenseListViewModel;
import com.projekt.zaliczeniowy.penny_pincher.ui.adapter.CategorySpinnerAdapter;
import com.projekt.zaliczeniowy.penny_pincher.ui.adapter.ExpenseListAdapter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

public class ExpenseListFragment extends Fragment {

    public static final int SPINNER_HINT_ID = 1;
    private Category category;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_list, container, false);
        TextView tvEmptyListInfo = view.findViewById(R.id.tvEmptyListInfo);

        RecyclerView rvExpenseList = view.findViewById(R.id.rvExpenseList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        ExpenseListAdapter expenseListAdapter = new ExpenseListAdapter();
        rvExpenseList.setLayoutManager(layoutManager);
        rvExpenseList.setAdapter(expenseListAdapter);

        ExpenseListViewModel expenseViewModel = new ViewModelProvider(this).get(ExpenseListViewModel.class);
        expenseViewModel.getExpensesWithCategories().observe(getViewLifecycleOwner(), expenses -> {
            expenseListAdapter.setExpensesWithCategories(expenses);
            expenseListAdapter.setExpenseListViewModel(expenseViewModel);
            setEmptyInfoTextVisibility(tvEmptyListInfo, expenseListAdapter);
        });
        CategoryViewModel categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        FloatingActionButton fabAddExpense = view.findViewById(R.id.fabAddExpense);
        fabAddExpense.setOnClickListener(getFabAddExpenseOnClickListener(categoryViewModel, expenseViewModel));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(getItemTouchHelperSimpleCallback(expenseViewModel, expenseListAdapter));
        itemTouchHelper.attachToRecyclerView(rvExpenseList);

        return view;
    }

    private void setEmptyInfoTextVisibility(TextView emptyInfoTextView, ExpenseListAdapter expenseListAdapter) {
        if (expenseListAdapter.getItemCount() == 0) {
            emptyInfoTextView.setVisibility(View.VISIBLE);
        } else {
            emptyInfoTextView.setVisibility(View.INVISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private View.OnClickListener getFabAddExpenseOnClickListener(CategoryViewModel categoryViewModel, ExpenseListViewModel expenseListViewModel) {
        return view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            ViewGroup viewGroup = view.findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_add_expense, viewGroup, false);

            AlertDialog addExpenseDialog = builder.setView(dialogView).create();
            addExpenseDialog.show();

            EditText etExpenseName = dialogView.findViewById(R.id.etCategoryName);
            Button btnAddExpense = dialogView.findViewById(R.id.btnAdd);
            Button tbnCancel = dialogView.findViewById(R.id.btnCancel);
            Spinner sprCategory = dialogView.findViewById(R.id.sprCategory);

            categoryViewModel.getCategories().observe(getViewLifecycleOwner(), categories -> {
                sprCategory.setAdapter(new CategorySpinnerAdapter(requireContext(), R.layout.spinner_item, categories));
                sprCategory.setOnItemSelectedListener(getSpinnerOnItemSelectedListener());
            });

            btnAddExpense.setOnClickListener(getAddExpenseDialogBtnAddOnClickListener(etExpenseName, expenseListViewModel, addExpenseDialog));

            tbnCancel.setOnClickListener(getAddExpenseDialogBtnCancelOnClickListener(addExpenseDialog));
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private View.OnClickListener getAddExpenseDialogBtnAddOnClickListener(EditText etExpenseName,
                                                                          ExpenseListViewModel expenseListViewModel,
                                                                          AlertDialog addExpenseDialog) {
        return view -> {
            String expenseName = etExpenseName.getText().toString();
            if (expenseName.isEmpty()) {
                etExpenseName.setError("Fill expense name Field!");
            } else {
                if (expenseName.length() > 15) {
                    etExpenseName.setError("Max 15 characters");
                } else {
                    if (category.getCategoryId() != SPINNER_HINT_ID) {
                        expenseListViewModel.addExpense(new Expense(
                                expenseName,
                                category.getCategoryId(),
                                Date.from(Instant.now()),
                                new BigDecimal("0.00"),
                                false)
                        );
                        addExpenseDialog.hide();
                    }
                }
            }
        };
    }

    private View.OnClickListener getAddExpenseDialogBtnCancelOnClickListener(AlertDialog addExpenseDialog) {
        return view -> addExpenseDialog.hide();
    }

    private ItemTouchHelper.SimpleCallback getItemTouchHelperSimpleCallback(ExpenseListViewModel expenseListViewModel, ExpenseListAdapter expenseListAdapter) {
        return new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog
                        .Builder(getActivity())
                        .setMessage(R.string.dialog_delete_expense_message)
                        .setPositiveButton(R.string.btn_yes, getDeleteConfirmationDialogBtnYesOnClickListener(expenseListViewModel, expenseListAdapter, viewHolder))
                        .setNegativeButton(R.string.btn_cancel, getDeleteConfirmationDialogBtnCancelOnClickListener(expenseListAdapter))
                        .create()
                        .show();
            }
        };
    }

    private DialogInterface.OnClickListener getDeleteConfirmationDialogBtnYesOnClickListener(ExpenseListViewModel expenseListViewModel,
                                                                                             ExpenseListAdapter expenseListAdapter,
                                                                                             RecyclerView.ViewHolder viewHolder) {
        return (dialog, id) -> expenseListViewModel
                .deleteExpense(expenseListAdapter
                        .getExpensesWithCategories()
                        .get(viewHolder.getAdapterPosition())
                        .getExpense());
    }

    @SuppressLint("NotifyDataSetChanged")
    private DialogInterface.OnClickListener getDeleteConfirmationDialogBtnCancelOnClickListener(ExpenseListAdapter expenseListAdapter) {
        return (dialogInterface, i) -> expenseListAdapter.notifyDataSetChanged();
    }
}
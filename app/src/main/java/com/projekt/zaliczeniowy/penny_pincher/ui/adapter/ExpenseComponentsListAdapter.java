package com.projekt.zaliczeniowy.penny_pincher.ui.adapter;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.projekt.zaliczeniowy.penny_pincher.R;
import com.projekt.zaliczeniowy.penny_pincher.model.ExpenseComponent;
import com.projekt.zaliczeniowy.penny_pincher.model.viewmodel.ExpenseComponentsListViewModel;

import java.math.BigDecimal;
import java.util.List;

public class ExpenseComponentsListAdapter extends RecyclerView.Adapter<ExpenseComponentsListAdapter.ExpenseComponentsListVewHolder> {

    private List<ExpenseComponent> expenseComponents;
    private ExpenseComponentsListViewModel expenseComponentsListViewModel;

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.N)
    public double getExpenseTotalAmount() {
        double totalAmount = this.getExpenseComponents()
                .stream()
                .map(ExpenseComponent::getAmount)
                .mapToDouble(BigDecimal::doubleValue)
                .sum();
        return totalAmount;
    }

    public List<ExpenseComponent> getExpenseComponents() {
        return expenseComponents;
    }

    public static class ExpenseComponentsListVewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ExpenseComponent expenseComponent;
        private final TextView expenseComponentName;
        private final TextView expenseComponentAmount;
        private final ExpenseComponentsListViewModel expenseComponentsListViewModel;
        private final ImageButton ibDeleteComponent;

        public ExpenseComponentsListVewHolder(@NonNull View itemView, ExpenseComponentsListViewModel expenseComponentsListViewModel) {
            super(itemView);
            this.expenseComponentName = itemView.findViewById(R.id.tvExpenseComponentName);
            this.expenseComponentAmount = itemView.findViewById(R.id.tvExpenseComponentAmount);
            this.expenseComponentsListViewModel = expenseComponentsListViewModel;
            this.ibDeleteComponent = itemView.findViewById(R.id.ibDeleteComponent);
            
            ibDeleteComponent.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            new AlertDialog
                    .Builder(view.getContext())
                    .setMessage(R.string.dialog_delete_expense_message)
                    .setPositiveButton(R.string.btn_yes, getDeleteConfirmationDialogBtnYesOnClickListener(this.expenseComponentsListViewModel))
                    .setNegativeButton(R.string.btn_cancel, getDeleteConfirmationDialogBtnCancelOnClickListener())
                    .create()
                    .show();
        }

        private DialogInterface.OnClickListener getDeleteConfirmationDialogBtnYesOnClickListener(ExpenseComponentsListViewModel expenseComponentsListViewModel) {
            return (dialog, id) -> expenseComponentsListViewModel
                    .deleteExpenseComponent(this.expenseComponent);
        }

        @SuppressLint("NotifyDataSetChanged")
        private DialogInterface.OnClickListener getDeleteConfirmationDialogBtnCancelOnClickListener() {
            return (dialogInterface, i) -> dialogInterface.dismiss();
        }
    }

    @NonNull
    @Override
    public ExpenseComponentsListVewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_component_list_item, parent, false);
        return new ExpenseComponentsListAdapter.ExpenseComponentsListVewHolder(itemView, this.expenseComponentsListViewModel);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ExpenseComponentsListVewHolder holder, int position) {
        ExpenseComponent expenseComponent = expenseComponents.get(position);
        holder.expenseComponent = expenseComponent;
        holder.expenseComponentName.setText(expenseComponent.getDescription());
        holder.expenseComponentAmount.setText(String.format("%.2f", expenseComponent.getAmount()));
    }

    @Override
    public int getItemCount() {
        return expenseComponents == null ? 0 : expenseComponents.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setExpenseComponents(List<ExpenseComponent> expenseComponents) {
        this.expenseComponents = expenseComponents;
        notifyDataSetChanged();
    }

    public void setExpenseComponentsListViewModel(ExpenseComponentsListViewModel expenseComponentsListViewModel) {
        this.expenseComponentsListViewModel = expenseComponentsListViewModel;
    }
}

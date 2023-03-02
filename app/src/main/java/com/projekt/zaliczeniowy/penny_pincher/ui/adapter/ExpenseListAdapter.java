package com.projekt.zaliczeniowy.penny_pincher.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.projekt.zaliczeniowy.penny_pincher.ExpenseDetailsActivity;
import com.projekt.zaliczeniowy.penny_pincher.R;
import com.projekt.zaliczeniowy.penny_pincher.model.ExpenseWithCategory;
import com.projekt.zaliczeniowy.penny_pincher.model.Expense;
import com.projekt.zaliczeniowy.penny_pincher.model.viewmodel.ExpenseListViewModel;
import com.projekt.zaliczeniowy.penny_pincher.ui.utils.AnimationHelper;

import java.text.SimpleDateFormat;
import java.util.List;

public class ExpenseListAdapter extends RecyclerView.Adapter<ExpenseListAdapter.ExpenseListVewHolder> {

    private List<ExpenseWithCategory> expensesWithCategories;
    private ExpenseListViewModel expenseListViewModel;

    public List<ExpenseWithCategory> getExpensesWithCategories() {
        return expensesWithCategories;
    }

    public static class ExpenseListVewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final CardView expenseListItemCard;
        private final TextView expenseNameText;
        private final TextView expenseDate;
        private final TextView expenseTotalValue;
        private final TextView expenseCategoryName;
        private final ExpenseListViewModel expenseListViewModel;
        private long expenseId;
        private boolean isExpenseSettled;
        private ExpenseWithCategory expenseWithCategory;

        @SuppressLint("ExpenseListVewHolder")
        public ExpenseListVewHolder(@NonNull View itemView, ExpenseListViewModel expenseListViewModel) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            this.expenseListItemCard = itemView.findViewById(R.id.cvExpenseListItemCard);
            this.expenseNameText = itemView.findViewById(R.id.tvExpenseName);
            this.expenseDate = itemView.findViewById(R.id.tvExpenseDate);
            this.expenseTotalValue = itemView.findViewById(R.id.tvExpenseCost);
            this.expenseCategoryName = itemView.findViewById(R.id.tvCategoryName);
            this.expenseListViewModel = expenseListViewModel;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ExpenseDetailsActivity.class);
            intent.putExtra("expense", expenseWithCategory);
            v.getContext().startActivity(intent);
            AnimationHelper.customType(v.getContext(), "left-to-right");
        }

        @SuppressLint("ResourceAsColor")
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public boolean onLongClick(View v) {
            this.expenseListViewModel.setExpenseSettled(expenseId, !isExpenseSettled);
            if (!isExpenseSettled) {
                Snackbar.make(v, R.string.expense_was_settled, Snackbar.ANIMATION_MODE_SLIDE).show();
            } else {
                Snackbar.make(v, R.string.expense_wasnt_settled, Snackbar.ANIMATION_MODE_SLIDE).show();
            }
            return false;
        }
    }

    @NonNull
    @Override
    public ExpenseListVewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_list_item, parent, false);
        return new ExpenseListVewHolder(itemView, this.expenseListViewModel);
    }

    @SuppressLint({"NotifyDataSetChanged", "DefaultLocale"})
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ExpenseListVewHolder holder, int position) {
        if (expensesWithCategories != null) {
            holder.expenseWithCategory = this.expensesWithCategories.get(position);
            Expense expense = this.expensesWithCategories.get(position).getExpense();
            @SuppressLint("SimpleDateFormat") String expenseDate =
                    new SimpleDateFormat("EEE, dd MMMM yyyy").format(expense.getExpenseDate());
            holder.expenseNameText.setText(expense.getExpenseName());
            if (!expense.isSettled())
                holder.expenseListItemCard.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.red_light));
            else
                holder.expenseListItemCard.setCardBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));

            holder.expenseDate.setText(expenseDate);
            holder.expenseTotalValue.setText(String.format("%.2f", expense.getExpenseTotalValue()));
            holder.expenseCategoryName.setText(this.expensesWithCategories.get(position).getCategory().getCategoryName());
            holder.isExpenseSettled = expense.isSettled();
            holder.expenseId = expense.getExpenseId();
        }
    }


    @Override
    public int getItemCount() {
        return expensesWithCategories == null ? 0 : expensesWithCategories.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setExpensesWithCategories(List<ExpenseWithCategory> expenseWithCategories) {
        this.expensesWithCategories = expenseWithCategories;
        notifyDataSetChanged();
    }

    public void setExpenseListViewModel(ExpenseListViewModel expenseListViewModel) {
        this.expenseListViewModel = expenseListViewModel;
    }
}

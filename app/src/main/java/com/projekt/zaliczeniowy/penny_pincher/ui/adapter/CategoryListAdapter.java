package com.projekt.zaliczeniowy.penny_pincher.ui.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.projekt.zaliczeniowy.penny_pincher.R;
import com.projekt.zaliczeniowy.penny_pincher.model.Category;
import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {

    private List<Category> categories;

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView categoryNameText;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            this.categoryNameText = itemView.findViewById(R.id.tvCategoryName);
        }
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item, parent, false);
        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        String categoryName = category.getCategoryName();
        holder.categoryNameText.setText(categoryName);
    }

    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }


    @SuppressLint("NotifyDataSetChanged")
    public void setCategories(List<Category> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public List<Category> getCategories() {
        return categories;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean isCategoryExists(String categoryName) {
        long count = categories
                .stream()
                .map(Category::getCategoryName)
                .filter(s -> s.equals(categoryName))
                .count();
        return count != 0;
    }
}

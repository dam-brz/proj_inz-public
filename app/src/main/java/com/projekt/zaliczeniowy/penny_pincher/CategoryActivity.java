package com.projekt.zaliczeniowy.penny_pincher;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.projekt.zaliczeniowy.penny_pincher.model.Category;
import com.projekt.zaliczeniowy.penny_pincher.model.viewmodel.CategoryViewModel;
import com.projekt.zaliczeniowy.penny_pincher.ui.adapter.CategoryListAdapter;
import com.projekt.zaliczeniowy.penny_pincher.ui.utils.AnimationHelper;

public class CategoryActivity extends AppCompatActivity {
    public static final int CATEGORY_OTHER_POSITION = 0;
    private CategoryViewModel categoryViewModel;
    private CategoryListAdapter categoryListAdapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        RecyclerView rvCategories = findViewById(R.id.rvCategories);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        categoryListAdapter = new CategoryListAdapter();
        rvCategories.setLayoutManager(layoutManager);
        rvCategories.setAdapter(categoryListAdapter);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getCategories().observe(this, categories -> {
            if (!categories.isEmpty()) {
                categories.remove(0);
                categoryListAdapter.setCategories(categories);
            }
        });

        FloatingActionButton fabAddCategory = findViewById(R.id.fabAddCategory);
        fabAddCategory.setOnClickListener(getFabCategoryOnClickListener());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(getItemTouchHelperSimpleCallback());
        itemTouchHelper.attachToRecyclerView(rvCategories);

    }

    public void onBackPressed() {
        super.onBackPressed();
        AnimationHelper.customType(CategoryActivity.this, "right-to-left");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private View.OnClickListener getFabCategoryOnClickListener() {
        return view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            ViewGroup viewGroup = view.findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_add_category, viewGroup, false);

            AlertDialog dialog = builder.setView(dialogView).create();
            dialog.show();

            EditText etCategoryName = dialogView.findViewById(R.id.etCategoryName);
            Button btnAdd = dialogView.findViewById(R.id.btnAdd);
            Button btnCancel = dialogView.findViewById(R.id.btnCancel);


            btnAdd.setOnClickListener(getDialogBtnAddOnClickListener(etCategoryName, dialog));

            btnCancel.setOnClickListener(getDialogBtnCancelOnClickListener(dialog));
        };
    }

    private View.OnClickListener getDialogBtnCancelOnClickListener(AlertDialog dialog) {
        return view -> dialog.dismiss();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private View.OnClickListener getDialogBtnAddOnClickListener(EditText etCategoryName, Dialog dialog) {
        return view -> {
            String categoryName = etCategoryName.getText().toString();
            if (categoryName.isEmpty()) {
                etCategoryName.setError(getResources().getString(R.string.not_empty));
            } else {
                if (categoryName.length() > 15) {
                    etCategoryName.setError(getResources().getString(R.string.name_limit));
                } else {
                    if (!categoryListAdapter.isCategoryExists(categoryName)) {
                        categoryViewModel.addCategory(new Category(categoryName));
                        dialog.dismiss();
                    } else {
                        etCategoryName.setError(getResources().getString(R.string.category_exists));
                    }
                }
            }
        };
    }
    private ItemTouchHelper.SimpleCallback getItemTouchHelperSimpleCallback() {
        return new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                if (viewHolder.getAdapterPosition() == 0) {
                    Snackbar.make(viewHolder.itemView, R.string.default_category_error, Snackbar.ANIMATION_MODE_SLIDE).show();
                    categoryListAdapter.notifyDataSetChanged();
                } else {
                    new AlertDialog
                            .Builder(CategoryActivity.this)
                            .setMessage(R.string.dialog_delete_category_message)
                            .setPositiveButton(R.string.btn_yes, getBtnYesOnClickListener(categoryViewModel, categoryListAdapter, viewHolder))
                            .setNegativeButton(R.string.btn_cancel, getBtnCancelOnClickListener())
                            .create()
                            .show();
                }
            }
        };
    }

    private DialogInterface.OnClickListener getBtnYesOnClickListener(CategoryViewModel categoryViewModel, CategoryListAdapter categoryListAdapter, RecyclerView.ViewHolder viewHolder) {
        Category categoryDefault = categoryListAdapter.getCategories().get(CATEGORY_OTHER_POSITION);
        Category categoryToDelete = categoryListAdapter.getCategories().get(viewHolder.getAdapterPosition());
        return (dialog, id) -> categoryViewModel
                .deleteCategory(categoryToDelete, categoryDefault.getCategoryId());
    }

    @SuppressLint("NotifyDataSetChanged")
    private DialogInterface.OnClickListener getBtnCancelOnClickListener() {
        return (dialogInterface, i) -> categoryListAdapter.notifyDataSetChanged();
    }

}
package com.example.trackexpence.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trackexpence.Models.Category;
import com.example.trackexpence.R;
import com.example.trackexpence.databinding.SampleCategoryItemBinding;

import java.util.ArrayList;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {



    Context context;
    ArrayList<Category> categories;

    public interface CategoryClickListner{
        void onCategotyClick(Category category);
    }

    CategoryClickListner categoryClickListner;
    public CategoryAdapter(Context context,ArrayList<Category> categories,CategoryClickListner categoryClickListner){
        this.context = context;
        this.categories = categories;
        this.categoryClickListner = categoryClickListner;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.sample_category_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int i) {
        Category category = categories.get(i);
        categoryViewHolder.binding.categoryText.setText(category.getCategoryName());
        categoryViewHolder.binding.categoryIcon.setImageResource(category.getCategoryImage());
        categoryViewHolder.itemView.setOnClickListener(c -> {
            categoryClickListner.onCategotyClick(category);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        SampleCategoryItemBinding binding;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SampleCategoryItemBinding.bind(itemView);
        }
    }
}

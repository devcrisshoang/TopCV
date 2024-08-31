package com.example.topcv.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.R;
import com.example.topcv.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context mContext;
    private List<Category> category_list;

    public CategoryAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<Category> listCategory){
        this.category_list = listCategory;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = category_list.get(position);
        if (category == null) {
            return;
        }
        holder.category_name.setText(category.getCategory_name());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        holder.recyclerview_jobs.setLayoutManager(linearLayoutManager);

        JobsAdapter jobsAdapter = new JobsAdapter();
        jobsAdapter.setData(category.getJobs_list());

        holder.recyclerview_jobs.setAdapter(jobsAdapter);
    }

    @Override
    public int getItemCount() {
        if (category_list != null)
            return category_list.size();
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView category_name;
        private RecyclerView recyclerview_jobs;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            category_name = itemView.findViewById(R.id.category_name);
            recyclerview_jobs = itemView.findViewById(R.id.recyclerview_jobs);
        }
    }
}

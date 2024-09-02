// CategoryAdapter.java
package com.example.topcv.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.CompanyInformationsActivity;
import com.example.topcv.R;

import com.example.topcv.SeeAllActivity;
import com.example.topcv.model.Category;
import com.example.topcv.model.Jobs;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private Context mContext;
    private List<Category> category_list;

    private OnItemClickListener onItemClickListener;

    public CategoryAdapter(Context context, OnItemClickListener listener) {
        this.mContext = context;
        this.onItemClickListener = listener;
    }

    public void setData(List<Category> listCategory) {
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

        // Xác định hướng của RecyclerView
        int orientation = position == 0 ? RecyclerView.VERTICAL : RecyclerView.HORIZONTAL;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, orientation, false);
        holder.recyclerview_jobs.setLayoutManager(linearLayoutManager);

        JobsAdapter jobsAdapter = new JobsAdapter(mContext);
        jobsAdapter.setData(category.getJobs_list());

        holder.recyclerview_jobs.setAdapter(jobsAdapter);

        holder.see_all.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, SeeAllActivity.class);
            intent.putExtra("CATEGORY_NAME", category.getCategory_name());
            intent.putParcelableArrayListExtra("JOBS_LIST", new ArrayList<>(category.getJobs_list()));
            mContext.startActivity(intent);
        });

        // Set onClickListener for job items
        jobsAdapter.setOnItemClickListener(job -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(job);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (category_list != null) {
            return category_list.size();
        }
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView category_name;
        private RecyclerView recyclerview_jobs;
        private Button see_all;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            category_name = itemView.findViewById(R.id.category_name);
            recyclerview_jobs = itemView.findViewById(R.id.recyclerview_jobs);
            see_all = itemView.findViewById(R.id.see_all);
        }
    }

    // Define the interface for the item click listener
    public interface OnItemClickListener {
        void onItemClick(Jobs job);
    }
}

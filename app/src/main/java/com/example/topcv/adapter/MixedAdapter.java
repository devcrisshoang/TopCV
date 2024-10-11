package com.example.topcv.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.R;
import com.example.topcv.SeeAllActivity;
import com.example.topcv.model.Category;
import com.example.topcv.model.Company;
import com.example.topcv.model.Jobs;

import java.util.ArrayList;
import java.util.List;

public class MixedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_COMPANY = 0;
    private static final int VIEW_TYPE_CATEGORY = 1;
    private List<Object> mixedList;
    private OnItemClickListener onItemClickListener;

    public MixedAdapter(List<Object> mixedList, OnItemClickListener listener) {
        this.mixedList = mixedList;
        this.onItemClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (mixedList.get(position) instanceof Company) {
            return VIEW_TYPE_COMPANY;
        } else if (mixedList.get(position) instanceof Category) {
            return VIEW_TYPE_CATEGORY;
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_CATEGORY) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_category, parent, false);
            return new CategoryViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CategoryViewHolder) {
            Category category = (Category) mixedList.get(position);
            ((CategoryViewHolder) holder).bind(category);
        }
    }

    @Override
    public int getItemCount() {
        return mixedList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryName;
        private RecyclerView recyclerViewJobs;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.category_name);
            recyclerViewJobs = itemView.findViewById(R.id.recyclerview_jobs);
        }

        public void bind(Category category) {
            categoryName.setText(category.getCategory_name());

            // Check the position to determine orientation
            int position = getAdapterPosition();
            boolean isFirstItem = position == 0;

            RecyclerView.LayoutManager layoutManager;
            JobsAdapter jobsAdapter = new JobsAdapter(itemView.getContext());

            if (isFirstItem) {
                // Set up RecyclerView cho job đầu tiên theo chiều dọc
                layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false);
                if (category.getJobs_list().size() > 0) {
                    jobsAdapter.setData(category.getJobs_list().subList(0, Math.min(2, category.getJobs_list().size()))); // Hiển thị hai job đầu tiên
                }
            } else {
                // Set up RecyclerView cho các job còn lại theo chiều ngang
                layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
                if (category.getJobs_list().size() > 2) {
                    jobsAdapter.setData(category.getJobs_list().subList(2, category.getJobs_list().size())); // Hiển thị các job còn lại
                }
            }

            recyclerViewJobs.setLayoutManager(layoutManager);
            recyclerViewJobs.setAdapter(jobsAdapter);

            itemView.findViewById(R.id.see_all).setOnClickListener(view -> {
                Intent intent = new Intent(itemView.getContext(), SeeAllActivity.class);
                intent.putExtra("CATEGORY_NAME", category.getCategory_name());
                intent.putParcelableArrayListExtra("JOBS_LIST", new ArrayList<>(category.getJobs_list()));
                itemView.getContext().startActivity(intent);
            });

            jobsAdapter.setOnItemClickListener(job -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(job);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Jobs job);
    }
}
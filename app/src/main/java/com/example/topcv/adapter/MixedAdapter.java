package com.example.topcv.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.CompanyInformationsActivity;
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
        if (viewType == VIEW_TYPE_COMPANY) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_company, parent, false);
            return new CompanyViewHolder(view);
        } else if (viewType == VIEW_TYPE_CATEGORY) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_category, parent, false);
            return new CategoryViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CompanyViewHolder) {
            Company company = (Company) mixedList.get(position);
            ((CompanyViewHolder) holder).bind(company);
        } else if (holder instanceof CategoryViewHolder) {
            Category category = (Category) mixedList.get(position);
            ((CategoryViewHolder) holder).bind(category);
        }
    }

    @Override
    public int getItemCount() {
        return mixedList.size();
    }

    class CompanyViewHolder extends RecyclerView.ViewHolder {
        private ImageView companyLogo;
        private TextView companyName;
        private TextView companyIndustry;
        private TextView companyRank;

        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            companyLogo = itemView.findViewById(R.id.companyLogo);
            companyName = itemView.findViewById(R.id.companyName);
            companyIndustry = itemView.findViewById(R.id.companyIndustry);
            companyRank = itemView.findViewById(R.id.companyBadge);
        }

        public void bind(Company company) {
            companyLogo.setImageResource(company.getLogo());
            companyName.setText(company.getName());
            companyIndustry.setText(company.getIndustry());
            companyRank.setText(company.getBadge());
        }
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

                // Set up RecyclerView cho job đầu tiên theo chiều dọc
                }
                // Set up RecyclerView cho các job còn lại theo chiều ngang
            }
            recyclerViewJobs.setAdapter(jobsAdapter);
                }
        }

    public interface OnItemClickListener {
        void onItemClick(Jobs job);
    }
}

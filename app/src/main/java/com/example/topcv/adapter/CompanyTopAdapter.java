package com.example.topcv.adapter;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.topcv.R;
import com.example.topcv.model.Company;
import java.util.List;

public class CompanyTopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;
    private List<Company> mListCompany;
    private boolean isLoadingAdd;

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Company company);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Company> list){
        this.mListCompany = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (mListCompany != null && position == mListCompany.size() - 1 && isLoadingAdd) {
            return TYPE_LOADING;
        }
        return TYPE_ITEM;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (TYPE_ITEM == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_company_top, parent, false);
            return new CompanyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM){
            Company company = mListCompany.get(position);
            CompanyViewHolder companyViewHolder = (CompanyViewHolder) holder;
            if(company == null){
                return;
            }

            companyViewHolder.company_logo.setImageResource(R.drawable.workplace_ic);

            companyViewHolder.company_name.setText(company.getName());
            companyViewHolder.company_field.setText(company.getField());
            holder.itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(company);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mListCompany != null){
            return mListCompany.size();
        }
        return 0;
    }

    public static class CompanyViewHolder extends RecyclerView.ViewHolder{
        private final ImageView company_logo;
        private final TextView company_field;
        private final TextView company_name;
        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            company_logo = itemView.findViewById(R.id.company_logo);
            company_field = itemView.findViewById(R.id.company_field);
            company_name = itemView.findViewById(R.id.company_name);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            ProgressBar progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }

    public void addFooterLoading() {
        isLoadingAdd = true;
        mListCompany.add(new Company("","","","","",false));
    }

    public void removeFooterLoading() {
        isLoadingAdd = false;

        int position = mListCompany.size() - 1;
        Company company = mListCompany.get(position);
        if (company != null) {
            mListCompany.remove(position);
            notifyItemRemoved(position);
        }
    }
}


package com.example.topcv.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.R;
import com.example.topcv.model.Company;

import java.util.List;

public class CompanyTopAdapter extends RecyclerView.Adapter<CompanyTopAdapter.CompanyViewHolder>{
    private List<Company> mListCompany;
    public void setData(List<Company> list){
        this.mListCompany = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_company_top,parent,false);
        return new CompanyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {
        Company company = mListCompany.get(position);
        if(company == null){
            return;
        }
        String imageId = company.getImage();
        if (imageId != null && !imageId.isEmpty()) {
            try {
                holder.company_logo.setImageResource(Integer.parseInt(imageId));
            } catch (NumberFormatException e) {
                // Xử lý khi imageId không phải là một số nguyên hợp lệ
                holder.company_logo.setImageResource(R.drawable.fpt_ic); // Gán một ảnh mặc định
            }
        } else {
            // Gán một ảnh mặc định nếu imageId là null hoặc rỗng
            holder.company_logo.setImageResource(R.drawable.fpt_ic);
        }
        holder.company_name.setText(company.getName());
        holder.company_field.setText(company.getField());
    }

    @Override
    public int getItemCount() {
        if (mListCompany != null){
            return mListCompany.size();
        }
        return 0;
    }

    public class CompanyViewHolder extends RecyclerView.ViewHolder{
        private ImageView company_logo;
        private TextView company_field;
        private TextView company_name;
        public CompanyViewHolder(@NonNull View itemView) {
            super(itemView);
            company_logo = itemView.findViewById(R.id.company_logo);
            company_field = itemView.findViewById(R.id.company_field);
            company_name = itemView.findViewById(R.id.company_name);
        }
    }
}


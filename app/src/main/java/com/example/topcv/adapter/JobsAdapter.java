// JobsAdapter.java
package com.example.topcv.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.R;
import com.example.topcv.model.Jobs;

import java.util.List;

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.JobViewHolder> {

    private List<Jobs> mJobs;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public JobsAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<Jobs> jobs) {
        this.mJobs = jobs;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_works, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Jobs job = mJobs.get(position);
        if (job == null) {
            return;
        }
        holder.companyLogo.setImageResource(job.getImageId());
        holder.jobName.setText(job.getJobName());
        holder.companyName.setText(job.getCompanyName());
        holder.companyLocation.setText(job.getLocation());
        holder.jobExperience.setText(job.getExperience());
        holder.salary.setText(job.getSalary());
        if (job.isCheck()) {
            holder.checked.setVisibility(View.VISIBLE); // Hiển thị nếu true
        } else {
            holder.checked.setVisibility(View.GONE); // Ẩn nếu false
        }

        // Set onClickListener for job item
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(job);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mJobs != null) {
            return mJobs.size();
        }
        return 0;
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        private ImageView companyLogo;
        private TextView jobName;
        private TextView companyName;
        private TextView companyLocation;
        private TextView jobExperience;
        private TextView salary;
        private ImageView checked;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            companyLogo = itemView.findViewById(R.id.company_logo);
            jobName = itemView.findViewById(R.id.jobName);
            companyName = itemView.findViewById(R.id.company_name);
            companyLocation = itemView.findViewById(R.id.companyLocation);
            jobExperience = itemView.findViewById(R.id.jobExperience);
            salary = itemView.findViewById(R.id.salary);
            checked = itemView.findViewById(R.id.checked);
        }
    }

    // Define the interface for the item click listener
    public interface OnItemClickListener {
        void onItemClick(Jobs job);
    }
}

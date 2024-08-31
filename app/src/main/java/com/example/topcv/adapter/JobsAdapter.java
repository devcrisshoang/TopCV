package com.example.topcv.adapter;

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

public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.JobViewHolder>{

    private List<Jobs> mJobs;
    public void setData(List<Jobs> jobs){
        this.mJobs = jobs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_works,parent,false);
        return new JobViewHolder(view);
    }

    @Override
    public int getItemCount() {
        if (mJobs != null) {
            return mJobs.size();
        }
        return 0;
    }
    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Jobs jobs = mJobs.get(position);
        if(jobs == null){
            return;
        }
        holder.company_logo.setImageResource(jobs.getImageId());
        holder.jobName.setText(jobs.getJobName());
        holder.company_name.setText(jobs.getCompanyName());
        holder.companyLocation.setText(jobs.getLocation());
        holder.jobExperience.setText(jobs.getExperience());
        holder.salary.setText(jobs.getSalary());
        if (jobs.isCheck()) {
            holder.checked.setVisibility(View.VISIBLE); // Hiển thị nếu true
        } else {
            holder.checked.setVisibility(View.GONE); // Ẩn nếu false
        }
    }

    public class JobViewHolder extends RecyclerView.ViewHolder {

        private ImageView company_logo;
        private TextView jobName;
        private TextView company_name;
        private TextView companyLocation;
        private TextView jobExperience;
        private TextView salary;
        private ImageView checked;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            company_logo = itemView.findViewById(R.id.company_logo);
            jobName = itemView.findViewById(R.id.jobName);
            company_name = itemView.findViewById(R.id.company_name);
            companyLocation = itemView.findViewById(R.id.companyLocation);
            jobExperience = itemView.findViewById(R.id.jobExperience);
            salary = itemView.findViewById(R.id.salary);
            checked = itemView.findViewById(R.id.checked);
        }
    }
}

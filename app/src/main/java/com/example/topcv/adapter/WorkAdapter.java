package com.example.topcv.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.R;
import com.example.topcv.model.Job;

import java.util.List;

public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.WorkViewHolder>{
    private List<Job> mListWork;
    public void setData(List<Job> list){
        this.mListWork = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public WorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_works,parent,false);
        return new WorkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkViewHolder holder, int position) {
        Job job = mListWork.get(position);
        if(job == null){
            return;
        }
        String imageId = job.getImageId();
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

        holder.job_name.setText(job.getJobName());
        holder.company_name.setText(job.getCompanyName());
        holder.company_location.setText(job.getLocation());
        holder.salary.setText(job.getSalary());
        holder.job_experience.setText(job.getExperience());
        holder.time_remaining.setText(job.getApplicationDate());
    }

    @Override
    public int getItemCount() {
        if (mListWork != null){
            return mListWork.size();
        }
        return 0;
    }

    public class WorkViewHolder extends RecyclerView.ViewHolder{
        private ImageView company_logo;
        private TextView job_name;
        private TextView company_name;
        private TextView company_location;
        private TextView salary;
        private TextView job_experience;
        private TextView time_remaining;
        public WorkViewHolder(@NonNull View itemView) {
            super(itemView);
            company_logo = itemView.findViewById(R.id.company_logo);
            job_name = itemView.findViewById(R.id.job_name);
            company_name = itemView.findViewById(R.id.company_name);
            company_location = itemView.findViewById(R.id.company_location);
            salary = itemView.findViewById(R.id.salary);
            job_experience = itemView.findViewById(R.id.job_experience);
            time_remaining = itemView.findViewById(R.id.time_remaining);
        }
    }
}

package com.example.topcv.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.R;
import com.example.topcv.model.Job;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WorkAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;
    private List<Job> mListWork;
    private boolean isLoadingAdd;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Job job);
    }

    @Override
    public int getItemViewType(int position) {
        if (mListWork != null && position == mListWork.size() - 1 && isLoadingAdd) {
            return TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setData(List<Job> list) {
        this.mListWork = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (TYPE_ITEM == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_works, parent, false);
            return new WorkViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM) {
            Job job = mListWork.get(position);
            WorkViewHolder workViewHolder = (WorkViewHolder) holder;
            if (job == null) {
                return;
            }

            holder.itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(job);
                }
            });

            // Set image
            String imageId = job.getImageId();
            if (imageId != null && !imageId.isEmpty()) {
                try {
                    workViewHolder.company_logo.setImageResource(Integer.parseInt(imageId));
                } catch (NumberFormatException e) {
                    workViewHolder.company_logo.setImageResource(R.drawable.fpt_ic); // Default image
                }
            } else {
                workViewHolder.company_logo.setImageResource(R.drawable.fpt_ic); // Default image
            }

            // Calculate time remaining for application
            String applicationDateStr = job.getApplicationDate();
            if (applicationDateStr != null && applicationDateStr.length() >= 10) {
                LocalDate applicationDate = LocalDate.parse(applicationDateStr.substring(0, 10));
                LocalDate deadlineDate = applicationDate.plusDays(30); // 30-day deadline

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate today = LocalDate.now();
                Period period = Period.between(today, deadlineDate);
                int daysRemaining = period.getDays();
                if (daysRemaining < 0) {
                    workViewHolder.time_remaining.setText("Đã quá hạn nộp");
                } else {
                    workViewHolder.time_remaining.setText("Còn " + daysRemaining + " ngày để nộp");
                }
            } else {
                workViewHolder.time_remaining.setText("Ngày nộp không hợp lệ");
            }

            workViewHolder.job_name.setText(job.getJobName());
            workViewHolder.company_name.setText(job.getCompanyName());
            workViewHolder.company_location.setText(job.getLocation());
            workViewHolder.salary.setText(String.valueOf(job.getSalary()));
            workViewHolder.job_experience.setText(job.getExperience());
        }
    }

    @Override
    public int getItemCount() {
        if (mListWork != null) {
            return mListWork.size();
        }
        return 0;
    }

    public class WorkViewHolder extends RecyclerView.ViewHolder {
        public ImageView company_logo;
        public TextView job_name, company_name, company_location, salary, job_experience, time_remaining;

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

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }

    public void addFooterLoading() {
        isLoadingAdd = true;
        mListWork.add(new Job("","","","",0));
    }

    public void removeFooterLoading() {
        isLoadingAdd = false;

        int position = mListWork.size() - 1;
        Job job = mListWork.get(position);
        if (job != null) {
            mListWork.remove(position);
            notifyItemRemoved(position);
        }
    }
}

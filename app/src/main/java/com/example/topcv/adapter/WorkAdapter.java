package com.example.topcv.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.topcv.R;
import com.example.topcv.api.ApiCompanyService;
import com.example.topcv.api.ApiRecruiterService;
import com.example.topcv.model.Job;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

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

    @SuppressLint("NotifyDataSetChanged")
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

    @SuppressLint("CheckResult")
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

            String applicationDateStr = job.getApplicationDate();
            if (applicationDateStr != null && applicationDateStr.length() >= 10) {
                LocalDate applicationDate = LocalDate.parse(applicationDateStr.substring(0, 10));
                LocalDate deadlineDate = applicationDate.plusDays(30);
                LocalDate today = LocalDate.now();
                Period period = Period.between(today, deadlineDate);
                int daysRemaining = period.getDays();
                if (daysRemaining < 0) {
                    workViewHolder.time_remaining.setText("Overdue");
                } else {
                    workViewHolder.time_remaining.setText("Remaining " + daysRemaining + " days");
                }
            } else {
                workViewHolder.time_remaining.setText("Invalid submission date");
            }

            ApiRecruiterService.ApiRecruiterService.getRecruiterById(job.getRecruiterId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            recruiter -> {
                                if (recruiter != null) {
                                    ApiCompanyService.ApiCompanyService.getCompanyByRecruiterId(recruiter.getId())
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(response -> {
                                                if (!response.isChecked()){
                                                    workViewHolder.checked.setVisibility(View.GONE);
                                                }

                                            }, throwable -> Log.e("API Error", "Error fetching applicant: " + throwable.getMessage()));
                                }
                            },
                            throwable -> Log.e("AccountFragment", "Error fetching recruiter: " + throwable.getMessage())
                    );

            workViewHolder.job_name.setText(job.getJobName());
            workViewHolder.company_name.setText(job.getCompanyName());
            workViewHolder.company_location.setText(job.getLocation());
            workViewHolder.salary.setText("$"+ job.getSalary());
            workViewHolder.job_experience.setText(job.getExperience());
            workViewHolder.company_logo.setImageResource(R.drawable.job_ic);
            workViewHolder.company_logo.setColorFilter(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        if (mListWork != null) {
            return mListWork.size();
        }
        return 0;
    }

    public static class WorkViewHolder extends RecyclerView.ViewHolder {

        public ImageView company_logo;
        private final ImageView checked;
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
            checked = itemView.findViewById(R.id.checked);
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

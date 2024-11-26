package com.example.topcv.adapter;

import android.annotation.SuppressLint;
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
import java.util.List;

public class TheBestJobAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;

    private List<Job> mListWork;

    private boolean isLoadingAdd;

    private WorkAdapter.OnItemClickListener onItemClickListener;

    @Override
    public int getItemViewType(int position) {
        if (mListWork != null && position == mListWork.size() - 1 && isLoadingAdd) {
            return TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    public void setOnItemClickListener(WorkAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Job> list){
        this.mListWork = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (TYPE_ITEM == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_the_best_job, parent, false);
            return new TheBestJobViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM){
            Job job = mListWork.get(position);
            TheBestJobViewHolder workViewHolder = (TheBestJobViewHolder) holder;
            if(job == null){
                return;
            }
            holder.itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(job);
                }
            });
            String imageId = job.getImageId();
            if (imageId != null && !imageId.isEmpty()) {
                try {
                    workViewHolder.company_logo.setImageResource(Integer.parseInt(imageId));
                } catch (NumberFormatException e) {

                    workViewHolder.company_logo.setImageResource(R.drawable.fpt_ic);
                }
            } else {
                workViewHolder.company_logo.setImageResource(R.drawable.fpt_ic);
            }
            workViewHolder.position_name.setText(job.getJobName());
            workViewHolder.company_name.setText(job.getCompanyName());
            workViewHolder.working_place.setText(job.getLocation());
            workViewHolder.salary.setText(String.valueOf(job.getSalary()));
        }
    }

    @Override
    public int getItemCount() {
        if (mListWork != null){
            return mListWork.size();
        }
        return 0;
    }

    public static class TheBestJobViewHolder extends RecyclerView.ViewHolder{
        private final ImageView company_logo;
        private final TextView position_name;
        private final TextView company_name;
        private final TextView working_place;
        private final TextView salary;
        public TheBestJobViewHolder(@NonNull View itemView) {
            super(itemView);
            company_logo = itemView.findViewById(R.id.company_logo);
            position_name = itemView.findViewById(R.id.position_name);
            company_name = itemView.findViewById(R.id.company_name);
            working_place = itemView.findViewById(R.id.working_place);
            salary = itemView.findViewById(R.id.salary);
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
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

public class TheBestJobAdapter extends RecyclerView.Adapter<TheBestJobAdapter.TheBestJobViewHolder>{
    private List<Job> mListWork;
    public interface OnItemClickListener {
        void onItemClick(Job job);
    }

    private WorkAdapter.OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(WorkAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public void setData(List<Job> list){
        this.mListWork = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public TheBestJobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_the_best_job,parent,false);
        return new TheBestJobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TheBestJobViewHolder holder, int position) {
        Job job = mListWork.get(position);
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
                holder.company_logo.setImageResource(Integer.parseInt(imageId));
            } catch (NumberFormatException e) {
                // Xử lý nếu imageId không phải là số nguyên hợp lệ
                holder.company_logo.setImageResource(R.drawable.fpt_ic); // Gán một ảnh mặc định
            }
        } else {
            // Gán một ảnh mặc định nếu imageId là null hoặc rỗng
            holder.company_logo.setImageResource(R.drawable.fpt_ic);
        }
        holder.position_name.setText(job.getJobName());
        holder.company_name.setText(job.getCompanyName());
        holder.working_place.setText(job.getLocation());
        holder.salary.setText(job.getSalary());
    }

    @Override
    public int getItemCount() {
        if (mListWork != null){
            return mListWork.size();
        }
        return 0;
    }

    public class TheBestJobViewHolder extends RecyclerView.ViewHolder{
        private ImageView company_logo;
        private TextView position_name;
        private TextView company_name;
        private TextView working_place;
        private TextView salary;
        public TheBestJobViewHolder(@NonNull View itemView) {
            super(itemView);
            company_logo = itemView.findViewById(R.id.company_logo);
            position_name = itemView.findViewById(R.id.position_name);
            company_name = itemView.findViewById(R.id.company_name);
            working_place = itemView.findViewById(R.id.working_place);
            salary = itemView.findViewById(R.id.salary);
        }
    }
}
package com.example.topcv.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.topcv.ResumeActivity;
import com.example.topcv.EditResumeActivity;
import com.example.topcv.R;
import com.example.topcv.model.Resume;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private List<Resume> mAppItems;
    private Context mContext;
    private int id_User;

    public ProfileAdapter(Context context, List<Resume> appItems, int id_User) {
        mAppItems = appItems;
        mContext = context;
        id_User = id_User;
    }

    public interface OnItemClickListener {
        void onItemClick(Resume resume);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profile_avatar;
        public TextView position;
        public TextView name;
        public ImageButton edit_cv;

        public ViewHolder(View v) {
            super(v);
            profile_avatar = v.findViewById(R.id.profile_avatar);
            position = v.findViewById(R.id.position);
            name = v.findViewById(R.id.name);
            edit_cv = v.findViewById(R.id.edit_cv);
        }
    }

    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profiles, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Resume resume = mAppItems.get(position);

        String applicantName = resume.getApplicant_name();
        String jobApplying = resume.getJob_applying();

        holder.name.setText(applicantName != null && !applicantName.isEmpty() ? applicantName : "Error");
        holder.position.setText(jobApplying != null && !jobApplying.isEmpty() ? jobApplying : "Error");

        holder.profile_avatar.setImageResource(R.drawable.account_ic);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ResumeActivity.class);
            intent.putExtra("resume_id", resume.getId());
            mContext.startActivity(intent);
        });
        holder.edit_cv.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, EditResumeActivity.class);
            intent.putExtra("resume_edit", resume.getId());
            intent.putExtra("id_User",id_User);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mAppItems.size();
    }
}

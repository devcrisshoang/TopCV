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

import com.example.topcv.CompanyInformationsActivity;
import com.example.topcv.CvActivity;
import com.example.topcv.EditCvActivity;
import com.example.topcv.R;
import com.example.topcv.model.Resume;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private List<Resume> mAppItems;
    private Context mContext;

    // Constructor, cần truyền Context và danh sách CV vào
    public ProfileAdapter(Context context, List<Resume> appItems) {
        mAppItems = appItems;
        mContext = context;
    }

    // Interface để xử lý sự kiện click
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

        // Kiểm tra và gán giá trị cho TextView
        String applicantName = resume.getApplicant_name();
        String jobApplying = resume.getJob_applying();

        Log.d("ProfileAdapter", "Applicant Name: " + applicantName);
        Log.d("ProfileAdapter", "Job Applying: " + jobApplying);

        holder.name.setText(applicantName != null && !applicantName.isEmpty() ? applicantName : "Error");
        holder.position.setText(jobApplying != null && !jobApplying.isEmpty() ? jobApplying : "Error");

        // Xử lý hình ảnh
        String imageUri = resume.getImage();
        if (imageUri != null && !imageUri.isEmpty()) {
            holder.profile_avatar.setImageURI(Uri.parse(imageUri));
        } else {
            holder.profile_avatar.setImageResource(R.drawable.account_ic); // Thay thế với hình ảnh mặc định nếu không có
        }

        // Đặt sự kiện click cho mỗi item
        holder.itemView.setOnClickListener(v -> {
            // Tạo Intent để mở CvActivity
            Intent intent = new Intent(mContext, CvActivity.class);
            // Truyền resume_id của đối tượng Resume sang CvActivity
            intent.putExtra("resume_id", resume.getId());
            mContext.startActivity(intent);
        });
        holder.edit_cv.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, EditCvActivity.class);
            intent.putExtra("resume_edit", resume.getId());
            mContext.startActivity(intent);
        });
    }



    @Override
    public int getItemCount() {
        return mAppItems.size();
    }
}

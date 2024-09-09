package com.example.topcv.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.CvActivity;
import com.example.topcv.R;
import com.example.topcv.model.CV;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private List<CV> mAppItems;
    private Context mContext;

    // Constructor, cần truyền Context vào
    public ProfileAdapter(Context context, List<CV> appItems) {
        mAppItems = appItems;
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView company_logo;
        public TextView position;
        public TextView name;

        public ViewHolder(View v) {
            super(v);
            company_logo = v.findViewById(R.id.company_logo);
            position = v.findViewById(R.id.position);
            name = v.findViewById(R.id.name);
        }
    }

    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profiles, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CV item = mAppItems.get(position);
        holder.company_logo.setImageResource(item.getImage());
        holder.position.setText(item.getPosition());
        holder.name.setText(item.getName());

        // Đặt sự kiện click cho mỗi item
        holder.itemView.setOnClickListener(v -> {
            // Khi click vào item, hiển thị Toast hoặc mở Activity
            Toast.makeText(mContext, "Clicked on: " + item.getName(), Toast.LENGTH_SHORT).show();

            // Tạo intent để mở ViewCvActivity và truyền dữ liệu
            Intent intent = new Intent(mContext, CvActivity.class);
            intent.putExtra("cv_name", item.getName());
            intent.putExtra("cv_position", item.getPosition());
            intent.putExtra("cv_image", item.getImage()); // Nếu muốn truyền hình ảnh
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mAppItems.size();
    }
}

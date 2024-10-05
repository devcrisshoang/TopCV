package com.example.topcv.adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.CvActivity;
import com.example.topcv.LoginActivity;
import com.example.topcv.MainActivity;
import com.example.topcv.R;
import com.example.topcv.model.CV;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private List<CV> mAppItems;
    private Context mContext;
    private OnItemClickListener mListener;

    // Constructor, cần truyền Context và danh sách CV vào
    public ProfileAdapter(Context context, List<CV> appItems) {
        mAppItems = appItems;
        mContext = context;
    }

    // Interface để xử lý sự kiện click
    public interface OnItemClickListener {
        void onItemClick(CV cv);
    }

    // Phương thức để thiết lập listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView profile_avatar;
        public TextView position;
        public TextView name;

        public ViewHolder(View v) {
            super(v);
            profile_avatar = v.findViewById(R.id.profile_avatar);
            position = v.findViewById(R.id.position);
            name = v.findViewById(R.id.name);
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
        CV item = mAppItems.get(position);
        holder.profile_avatar.setImageResource(item.getImage());
        holder.position.setText(item.getPosition());
        holder.name.setText(item.getName());

        // Đặt sự kiện click cho mỗi item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, CvActivity.class);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mAppItems.size();
    }
}

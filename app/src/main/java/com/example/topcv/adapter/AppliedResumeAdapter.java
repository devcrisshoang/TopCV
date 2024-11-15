package com.example.topcv.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.topcv.R;
import com.example.topcv.model.Resume;

import java.util.List;

public class AppliedResumeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;
    private List<Resume> mListResume;
    private boolean isLoadingAdd;
    private OnItemClickListener onItemClickListener;
    private int selectedPosition = -1; // Lưu trữ vị trí của RadioButton được chọn

    public interface OnItemClickListener {
        void onItemClick(Resume resume);
    }

    @Override
    public int getItemViewType(int position) {
        if (mListResume != null && position == mListResume.size() - 1 && isLoadingAdd) {
            return TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setData(List<Resume> list) {
        this.mListResume = list;
        selectedPosition = 0; // Set item đầu tiên được chọn
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (TYPE_ITEM == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applied_resume, parent, false);
            return new WorkViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM) {
            Resume resume = mListResume.get(holder.getAdapterPosition()); // Get current position dynamically
            WorkViewHolder workViewHolder = (WorkViewHolder) holder;
            if (resume == null) {
                return;
            }

            holder.itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(resume);
                }
            });

            String image = resume.getImage();

            // Sử dụng Glide để tải ảnh từ URI
            Glide.with(workViewHolder.profile_avatar.getContext())
                    .load(image)  // image là URI của ảnh
                    .into(workViewHolder.profile_avatar);
            workViewHolder.position.setText(resume.getJob_applying());
            workViewHolder.name.setText(resume.getApplicant_name());

            // Thiết lập trạng thái của RadioButton
            workViewHolder.radio_button.setChecked(holder.getAdapterPosition() == selectedPosition);

            // Xử lý khi người dùng chọn một RadioButton
            workViewHolder.radio_button.setOnClickListener(v -> {
                int adapterPosition = holder.getAdapterPosition(); // Get current position dynamically

                // Cập nhật vị trí của RadioButton được chọn
                if (selectedPosition != adapterPosition) {
                    selectedPosition = adapterPosition;

                    // Gọi notifyDataSetChanged() để cập nhật tất cả các item, giúp hủy chọn các RadioButton khác
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mListResume != null) {
            return mListResume.size();
        }
        return 0;
    }

    public class WorkViewHolder extends RecyclerView.ViewHolder {
        public ImageView profile_avatar;
        public TextView position, name;
        public RadioButton radio_button;

        public WorkViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_avatar = itemView.findViewById(R.id.profile_avatar);
            position = itemView.findViewById(R.id.position);
            name = itemView.findViewById(R.id.name);
            radio_button = itemView.findViewById(R.id.radio_button);
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
        mListResume.add(new Resume("","","","","","","","","","",0));
    }

    public void removeFooterLoading() {
        isLoadingAdd = false;

        if (mListResume != null && mListResume.size() > 0) {
            int position = mListResume.size() - 1;

            Resume resume = mListResume.get(position);
            // Kiểm tra nếu phần tử cuối là loading footer, có thể dựa vào các thuộc tính của Resume
            if (resume != null && resume.getId() == 0) {  // Ví dụ sử dụng `id == 0` để đánh dấu footer
                mListResume.remove(position);
                notifyItemRemoved(position);
            }
        }
    }
}

package com.example.topcv.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.R;

import java.util.List;

public class WorkSearchAdapter extends RecyclerView.Adapter<WorkSearchAdapter.KeywordViewHolder> {
    private List<String> mKeywords; // Danh sách từ khóa tìm kiếm gần đây
    private OnKeywordClickListener onKeywordClickListener;

    public interface OnKeywordClickListener {
        void onKeywordClick(String keyword);
    }

    public void setOnKeywordClickListener(OnKeywordClickListener listener) {
        this.onKeywordClickListener = listener;
    }

    public void setKeywords(List<String> keywords) {
        this.mKeywords = keywords;
        notifyDataSetChanged(); // Cập nhật lại giao diện
    }

    @NonNull
    @Override
    public KeywordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_search, parent, false);
        return new KeywordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KeywordViewHolder holder, int position) {
        String keyword = mKeywords.get(position);
        if (keyword == null) {
            return;
        }

        holder.keywordText.setText(keyword); // Gán từ khóa vào TextView

        // Xử lý sự kiện click
        holder.itemView.setOnClickListener(v -> {
            if (onKeywordClickListener != null) {
                onKeywordClickListener.onKeywordClick(keyword);
            }
        });

        // Cập nhật số lượng công việc vào number_of_job_edittext
        String[] parts = keyword.split(" - "); // Tách từ khóa và số lượng nếu có
    }

    @Override
    public int getItemCount() {
        if (mKeywords != null) {
            return mKeywords.size();
        }
        return 0; // Trả về 0 nếu danh sách không có từ khóa
    }

    public static class KeywordViewHolder extends RecyclerView.ViewHolder {
        private TextView keywordText;

        public KeywordViewHolder(@NonNull View itemView) {
            super(itemView);
            keywordText = itemView.findViewById(R.id.recent_search_edittext); // Lấy TextView từ item_keyword.xml
        }
    }

    // Phương thức cập nhật số lượng công việc cho từng từ khóa
    public void updateKeywordCount(String keyword, int count) {
        int position = mKeywords.indexOf(keyword);
        if (position != -1) {
            String updatedKeywordText;
            // Cập nhật số lượng công việc cho từ khóa tương ứng
            if(count == 1){
                updatedKeywordText = keyword + " - " + count + " Result"; // Ví dụ: "Software - 3 kết quả"
            }
            else {
                updatedKeywordText = keyword + " - " + count + " Results"; // Ví dụ: "Software - 3 kết quả"
            }
            mKeywords.set(position, updatedKeywordText);
            notifyItemChanged(position); // Cập nhật giao diện chỉ cho vị trí đó
        }
    }
}

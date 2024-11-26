package com.example.topcv.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.topcv.R;
import java.util.List;

public class WorkSearchAdapter extends RecyclerView.Adapter<WorkSearchAdapter.KeywordViewHolder> {

    private List<String> mKeywords;

    private OnKeywordClickListener onKeywordClickListener;

    public interface OnKeywordClickListener {
        void onKeywordClick(String keyword);
    }

    public void setOnKeywordClickListener(OnKeywordClickListener listener) {
        this.onKeywordClickListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setKeywords(List<String> keywords) {
        this.mKeywords = keywords;
        notifyDataSetChanged();
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

        holder.keywordText.setText(keyword);

        holder.itemView.setOnClickListener(v -> {
            if (onKeywordClickListener != null) {
                onKeywordClickListener.onKeywordClick(keyword);
            }
        });

        String[] parts = keyword.split(" - ");
    }

    @Override
    public int getItemCount() {
        if (mKeywords != null) {
            return mKeywords.size();
        }
        return 0;
    }

    public static class KeywordViewHolder extends RecyclerView.ViewHolder {
        private final TextView keywordText;

        public KeywordViewHolder(@NonNull View itemView) {
            super(itemView);
            keywordText = itemView.findViewById(R.id.recent_search_edittext);
        }
    }

    public void updateKeywordCount(String keyword, int count) {
        int position = mKeywords.indexOf(keyword);
        if (position != -1) {
            String updatedKeywordText;
            if(count == 1){
                updatedKeywordText = keyword + " - " + count + " Result";
            }
            else {
                updatedKeywordText = keyword + " - " + count + " Results";
            }
            mKeywords.set(position, updatedKeywordText);
            notifyItemChanged(position);
        }
    }
}

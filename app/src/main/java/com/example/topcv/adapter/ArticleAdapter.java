package com.example.topcv.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;  // Thêm Glide để load ảnh từ URL
import com.example.topcv.R;
import com.example.topcv.model.Article;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
    private List<Article> mListArticle;
    private OnItemClickListener onItemClickListener;

    // Interface xử lý sự kiện click vào item
    public interface OnItemClickListener {
        void onItemClick(Article article);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setData(List<Article> list) {
        this.mListArticle = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = mListArticle.get(position);
        if (article == null) {
            return;
        }

        // Xử lý hình ảnh: Kiểm tra nếu imageId là một URL hoặc là ID của ảnh
        String imageId = article.getImage();
        if (imageId != null && !imageId.isEmpty()) {
            try {
                // Nếu imageId là ID của một ảnh (int), dùng setImageResource
                holder.article_image.setImageResource(Integer.parseInt(imageId));
            } catch (NumberFormatException e) {
                // Nếu imageId không phải là ID mà là URL, dùng Glide để load ảnh
                Glide.with(holder.itemView.getContext())
                        .load(imageId)
                        .placeholder(R.drawable.fpt_ic)  // Ảnh mặc định khi loading
                        .error(R.drawable.fpt_ic)        // Ảnh mặc định khi lỗi
                        .into(holder.article_image);
            }
        } else {
            holder.article_image.setImageResource(R.drawable.fpt_ic); // Ảnh mặc định khi không có imageId
        }

        // Gán dữ liệu cho các TextView
        holder.article_content.setText(article.getContent());
        holder.article_name.setText(article.getName());

        // Thiết lập sự kiện click vào item
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(article);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mListArticle != null) ? mListArticle.size() : 0;
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        private ImageView article_image;
        private TextView article_content;
        private TextView article_name;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            article_image = itemView.findViewById(R.id.article_image);
            article_content = itemView.findViewById(R.id.article_content);
            article_name = itemView.findViewById(R.id.article_name);
        }
    }
}

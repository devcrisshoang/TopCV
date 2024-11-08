package com.example.topcv.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;  // Thêm Glide để load ảnh từ URL
import com.example.topcv.R;
import com.example.topcv.model.Article;
import com.example.topcv.model.Company;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;
    private List<Article> mListArticle;
    private OnItemClickListener onItemClickListener;
    private boolean isLoadingAdd;
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

    @Override
    public int getItemViewType(int position) {
        if (mListArticle != null && position == mListArticle.size() - 1 && isLoadingAdd) {
            return TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (TYPE_ITEM == viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
            return new ArticleViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_ITEM){
            Article article = mListArticle.get(position);
            if (article == null) {
                return;
            }

            // Xử lý hình ảnh: Kiểm tra nếu imageId là một URL hoặc là ID của ảnh
            String imageId = article.getImage();
            ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;
            if (imageId != null && !imageId.isEmpty()) {
                try {
                    // Nếu imageId là ID của một ảnh (int), dùng setImageResource
                    articleViewHolder.article_image.setImageResource(Integer.parseInt(imageId));
                } catch (NumberFormatException e) {
                    // Nếu imageId không phải là ID mà là URL, dùng Glide để load ảnh
                    Glide.with(holder.itemView.getContext())
                            .load(imageId)
                            .placeholder(R.drawable.fpt_ic)  // Ảnh mặc định khi loading
                            .error(R.drawable.fpt_ic)        // Ảnh mặc định khi lỗi
                            .into(articleViewHolder.article_image);
                }
            } else {
                articleViewHolder.article_image.setImageResource(R.drawable.fpt_ic); // Ảnh mặc định khi không có imageId
            }

            // Gán dữ liệu cho các TextView
            articleViewHolder.article_content.setText(article.getContent());
            articleViewHolder.article_name.setText(article.getName());

            // Thiết lập sự kiện click vào item
            holder.itemView.setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(article);
                }
            });
        }
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
    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progress_bar);
        }
    }

    public void addFooterLoading() {
        isLoadingAdd = true;
        mListArticle.add(new Article("","","",""));
    }

    public void removeFooterLoading() {
        isLoadingAdd = false;

        int position = mListArticle.size() - 1;
        Article article = mListArticle.get(position);
        if (article != null) {
            mListArticle.remove(position);
            notifyItemRemoved(position);
        }
    }
}

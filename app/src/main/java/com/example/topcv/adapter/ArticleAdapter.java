package com.example.topcv.adapter;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.topcv.R;
import com.example.topcv.model.Article;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;
    private List<Article> mListArticle;
    private OnItemClickListener onItemClickListener;
    private boolean isLoadingAdd;

    public interface OnItemClickListener {
        void onItemClick(Article article);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
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
            ArticleViewHolder articleViewHolder = (ArticleViewHolder) holder;

            articleViewHolder.article_image.setImageResource(R.drawable.copywriting_ic);


            articleViewHolder.article_content.setText(article.getContent());
            articleViewHolder.article_name.setText(article.getName());

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
        private final ImageView article_image;
        private final TextView article_content;
        private final TextView article_name;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            article_image = itemView.findViewById(R.id.article_image);
            article_content = itemView.findViewById(R.id.article_content);
            article_name = itemView.findViewById(R.id.article_name);
        }
    }
    public static class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            ProgressBar progressBar = itemView.findViewById(R.id.progress_bar);
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

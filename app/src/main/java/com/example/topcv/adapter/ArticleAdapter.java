package com.example.topcv.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.topcv.R;
import com.example.topcv.model.Article;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>{
    private List<Article> mListArticle;
    public void setData(List<Article> list){
        this.mListArticle = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article,parent,false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = mListArticle.get(position);
        if(article == null){
            return;
        }
        holder.article_image.setImageResource(article.getImage());
        holder.article_content.setText(article.getContent());
        holder.article_name.setText(article.getName());
    }

    @Override
    public int getItemCount() {
        if (mListArticle != null){
            return mListArticle.size();
        }
        return 0;
    }

    public class ArticleViewHolder extends RecyclerView.ViewHolder{
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



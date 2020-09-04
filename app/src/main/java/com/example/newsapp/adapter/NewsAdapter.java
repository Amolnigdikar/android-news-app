package com.example.newsapp.adapter;

import android.content.Context;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.R;
import com.example.newsapp.model.NewsResponse;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private Context context;
    private List<NewsResponse.Article> articles;

    public NewsAdapter(Context context, List<NewsResponse.Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item_row_layout, null));
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsAdapter.ViewHolder holder, int position) {
        holder.newsTitle.setText(articles.get(position).getTitle());
        holder.newsDescription.setText(articles.get(position).getDescription());
        holder.source.setText(articles.get(position).getSource().getName());
        holder.publishedAt.setText(articles.get(position).getPublishedAt().substring(0,articles.get(position).getPublishedAt().indexOf("T")));

        Picasso.with(context).load(articles.get(position).getUrlToImage()).placeholder(R.drawable.placeholder).into(holder.newsImage);

        holder.cardDownArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.expandedView.getVisibility() == View.VISIBLE) {
                    holder.expandedView.setVisibility(View.GONE);
                    holder.cardDownArrow.setRotation(0f);
                } else {
                    holder.cardDownArrow.setRotation(180f);
                    holder.expandedView.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitle, newsDescription, source, publishedAt;
        ImageView newsImage, cardDownArrow;
        RelativeLayout expandedView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.news_title);
            newsImage = itemView.findViewById(R.id.news_image);
            newsDescription = itemView.findViewById(R.id.news_description);
            cardDownArrow = itemView.findViewById(R.id.card_down);
            expandedView = itemView.findViewById(R.id.expanded_view);
            source = itemView.findViewById(R.id.source);
            publishedAt = itemView.findViewById(R.id.published_at);
        }
    }

    public void updateNews(List<NewsResponse.Article> articles) {
        this.articles = articles;
        notifyDataSetChanged();
    }
}

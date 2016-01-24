package com.example.cos.testappmed.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cos.testappmed.R;
import com.example.cos.testappmed.model.News;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsListAdapter extends ArrayAdapter<News> {
    private List<News> newsList;
    private ImageView img;
    private TextView title;
    private TextView date;

    public NewsListAdapter(Context context, List<News> newsList) {
        super(context, R.layout.news_item, newsList);

        this.newsList = newsList;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.news_item, parent, false);
        }
        News news = newsList.get(position);

        img = (ImageView)view.findViewById(R.id.imgNews);
        title = (TextView)view.findViewById(R.id.tvTitle);
        date = (TextView)view.findViewById(R.id.tvCreatedAt);

        Picasso.with(getContext()).load(news.getImageURL()).into(img);
        title.setText(news.getTitle());
        date.setText(news.getCreatedAt());

        return view;
    }

}

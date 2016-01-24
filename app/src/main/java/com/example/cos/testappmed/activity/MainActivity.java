package com.example.cos.testappmed.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.cos.testappmed.R;
import com.example.cos.testappmed.adapter.NewsListAdapter;
import com.example.cos.testappmed.model.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView tvPage;
    private ArrayList<News> newsList = new ArrayList<>();
    private int page, pageCount;
    private NewsListAdapter adapter;
    private ListView lvNews;
    //Кол-во записей на странице
    private final int COUNT_REC_IN_PAGE = 5;
    final String PAGE = "page",
        PAGE_COUNT = "page_count",
        ID = "id",
        CREATED_AT = "created_at",
        IMG = "image",
        DATA = "data",
        TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvPage = (TextView) findViewById(R.id.tvPage);
        lvNews = (ListView)findViewById(R.id.lvNews);
        lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, CurrentNewsActivity.class);
                intent.putExtra(ID,newsList.get(position).getId());
                intent.putExtra(CREATED_AT, newsList.get(position).getCreatedAt());
                intent.putExtra(IMG, newsList.get(position).getImageURL());
                startActivity(intent);
            }
        });
        page = 1;

        final ImageButton btnLeft = (ImageButton)findViewById(R.id.btnLeft);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page != 1) {
                    page--;
                    getNews();
                }

            }
        });

        final ImageButton btnRight = (ImageButton)findViewById(R.id.btnRight);
        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page != pageCount) {
                    page++;
                    getNews();
                }
            }
        });

        getNews();

        adapter = new NewsListAdapter(this, newsList);
        lvNews.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PAGE, page);
        outState.putInt(PAGE_COUNT, pageCount);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        page = savedInstanceState.getInt(PAGE);
        pageCount = savedInstanceState.getInt(PAGE_COUNT);
        getNews();
    }

    private void getNews() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://medsolutions.uxp.ru/api/v1/news?page=" + page + "&limit=" +
                COUNT_REC_IN_PAGE + "&order_by=created_at&order=desc";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray newsJSON = response.getJSONArray(DATA);

                            installPage(newsJSON);

                            newsList.clear();
                            AddDataInList(newsJSON);

                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tvPage.setText(error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("API-KEY", "secret_key");
                return headers;
            }
        };

        queue.add(jsObjRequest);
    }

    private void AddDataInList(JSONArray newsJSON) throws JSONException {
        for (int i = 0; i < newsJSON.length(); i++) {
            int id = newsJSON.getJSONObject(i).getInt(ID);
            String title = newsJSON.getJSONObject(i).getString(TITLE);
            String imageUrl = newsJSON.getJSONObject(i).getString(IMG);
            String createdAt = newsJSON.getJSONObject(i).getString(CREATED_AT);

            News news = new News(id, title, imageUrl, createdAt);
            newsList.add(news);
        }
    }

    private void installPage(JSONArray newsJSON) throws JSONException {
        if (page == 1) {
            pageCount = (int)Math.ceil(newsJSON.getJSONObject(0).getDouble(ID) / COUNT_REC_IN_PAGE);
        }
        tvPage.setText(page + " из " + pageCount);
    }


}

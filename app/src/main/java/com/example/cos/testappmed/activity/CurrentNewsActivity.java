package com.example.cos.testappmed.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CurrentNewsActivity extends AppCompatActivity {

    final String ID = "id",
        CREATED_AT = "created_at",
        TITLE = "title",
        IMG = "image",
        SPOTLIGHT = "spotlight",
        DATA = "data",
        TEXT = "text",
        SOURCE = "source";
    private ArrayList<News> spotlightList = new ArrayList<>();
    private News currNews;
    private TextView tvText, tvCreatedAt, tvSource;
    private ImageView imgCurrNews;
    private ListView lvSpotlight;
    private NewsListAdapter adapter;
    private Fragment dataFragment, spotlightFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataFragment = getFragmentManager().findFragmentById(R.id.fragmentData);
        tvText = (TextView)dataFragment.getView().findViewById(R.id.tvCurrText);
        tvCreatedAt = (TextView)dataFragment.getView().findViewById(R.id.tvCurrCreatedAt);
        tvSource = (TextView)dataFragment.getView().findViewById(R.id.tvCurrSource);
        imgCurrNews = (ImageView)dataFragment.getView().findViewById(R.id.imgCurrNews);

        spotlightFragment = getFragmentManager().findFragmentById(R.id.fragmentSpotlight);
        lvSpotlight = (ListView)spotlightFragment.getView().findViewById(R.id.lvSpotlight);
        adapter = new NewsListAdapter(CurrentNewsActivity.this, spotlightList);
        lvSpotlight.setAdapter(adapter);


        Intent intent = getIntent();

        currNews = new News(intent.getIntExtra(ID, 0));
        currNews.setCreatedAt(intent.getStringExtra(CREATED_AT));
        currNews.setImageURL(intent.getStringExtra(IMG));

        getNews();
    }

    private void getNews() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://medsolutions.uxp.ru/api/v1/news/" + currNews.getId();

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray dataJSON = response.getJSONArray(DATA);

                            currNews.setText(dataJSON.getJSONObject(0).getString(TEXT));
                            currNews.setSource(dataJSON.getJSONObject(0).getString(SOURCE));

                            Picasso.with(CurrentNewsActivity.this)
                                    .load(currNews.getImageURL())
                                    .into(imgCurrNews);
                            tvText.setText(currNews.getText());
                            tvCreatedAt.setText(currNews.getCreatedAt());
                            tvSource.setText(currNews.getSource());


                            JSONArray spotlightJSON = response.getJSONArray(SPOTLIGHT);
                            spotlightList.clear();

                            AddDataInList(spotlightJSON);


                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tvText.setText(error.toString());
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
            spotlightList.add(news);
        }
    }

}

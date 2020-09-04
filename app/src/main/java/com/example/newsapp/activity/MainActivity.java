package com.example.newsapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.newsapp.R;
import com.example.newsapp.adapter.CategoryAdapter;
import com.example.newsapp.adapter.NewsAdapter;
import com.example.newsapp.model.NewsResponse;
import com.example.newsapp.network.NewsApiService;
import com.example.newsapp.network.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.ItemClickListener {

    private RecyclerView recyclerView,categoryRecyclerView;
    private NewsAdapter newsAdapter;
    private ProgressDialog progressDialog;
    private NewsApiService service;
    private List<String> categories;
    private List<Boolean> categoriesStatus;
    private CategoryAdapter categoryAdapter;
    private List<NewsResponse.Article> articles=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.news_recycler_view);
        categoryRecyclerView=findViewById(R.id.category_recycler_view);

        prepareCategoryList();

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        service= RetrofitClientInstance
                .getRetrofitInstance()
                .create(NewsApiService.class);
        Call<NewsResponse> newsResponseCall=service.getAllNews();
        newsResponseCall.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                progressDialog.dismiss();
                Log.d("Response:{}",response.message());
                if(response.body()!=null){
                    prepareNewsList(response.body());
                }else{
                    progressDialog.dismiss();
                }

            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void prepareCategoryList() {
        categories=new ArrayList<>();
        categoriesStatus=new ArrayList<>();
        categories.add("business");
        categories.add("entertainment");
        categories.add("general");
        categories.add("health");
        categories.add("science");
        categories.add("sports");
        categories.add("technology");

        categoriesStatus.add(false);
        categoriesStatus.add(false);
        categoriesStatus.add(false);
        categoriesStatus.add(false);
        categoriesStatus.add(false);
        categoriesStatus.add(false);
        categoriesStatus.add(false);
        categoryAdapter=new CategoryAdapter(getApplicationContext(),categories,this);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        categoryRecyclerView.setAdapter(categoryAdapter);


    }

    private void prepareNewsList(NewsResponse body) {
        articles=body.getArticles();
        newsAdapter=new NewsAdapter(getApplicationContext(),articles);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(newsAdapter);
    }

    @Override
    public void onItemClicked(int position) {
        progressDialog.show();
        service.getCategoryNews("in",categories.get(position),getString(R.string.API_KEY)).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                progressDialog.dismiss();
                newsAdapter.updateNews(response.body().getArticles());

            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
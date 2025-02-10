package com.example.winterapplication;

import static java.util.Locale.filter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DirectorActivity extends AppCompatActivity {

    private TextView backTextView;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    List<DirectorNews.Articles> newsDtoList = new ArrayList<>();

    private SearchView searchView;
    List<DirectorNews.Articles> filteredList = new ArrayList<>();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_director);

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        mRecyclerView = findViewById(R.id.dt_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mAdapter = new NewsAdapter(newsDtoList);
        mRecyclerView.setAdapter(mAdapter);

        fetchDataFromApi();

        initView();
        initClick();

        // 为 ImageView 设置点击事件
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    Intent intent1 = new Intent(DirectorActivity.this, MainActivity.class);
                    startActivity(intent1);
                    return true;
                } else if (item.getItemId() == R.id.navigation_structure) {
                    Intent intent2 = new Intent(DirectorActivity.this, StructActivity.class);
                    startActivity(intent2);
                    return true;
                } else if (item.getItemId() == R.id.navigation_director) {
                    return true;
                } else if (item.getItemId() == R.id.navigation_mine) {
                    Intent intent3 = new Intent(DirectorActivity.this, MineActivity.class);
                    startActivity(intent3);
                    return true;
                }
                return false;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.navigation_director);
    }


    private void filter(String text) {
        filteredList.clear();
        if (TextUtils.isEmpty(text)) {
            filteredList.addAll(newsDtoList);
        } else {
            for (DirectorNews.Articles item : newsDtoList) {
                if (item.getTitle().contains(text)) {
                    filteredList.add(item);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void fetchDataFromApi() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://www.wanandroid.com/navi/json")
                .get()
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    handleResponse(result);
                } else {

                }
            }
        });
    }

    private void handleResponse(String result) {
        Gson gson = new Gson();

            DirectorNews directorNews = gson.fromJson(result, DirectorNews.class);
            if (directorNews != null && directorNews.getData() != null) {
                for (DirectorNews.Data data : directorNews.getData()) {
                    newsDtoList.addAll(data.getArticles());
                    filteredList.addAll(data.getArticles());
                }
                runOnUiThread(() -> mAdapter.notifyDataSetChanged());
            }
    }

    private void initView() {
        backTextView = findViewById(R.id.net_back);
    }

    private void initClick() {
        if (backTextView != null) {
            backTextView.setOnClickListener(v -> {
                Intent intent = new Intent(DirectorActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });
        } else {

        }
    }

    private class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
        private  List<DirectorNews.Articles> articleList;

        public NewsAdapter(List<DirectorNews.Articles> articleList) {
            this.articleList = articleList;
        }

        @NonNull
        @Override
        public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_director, parent, false);
            return new NewsViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
            DirectorNews.Articles article = filteredList.get(position);
            holder.name.setText(article.getTitle());
            holder.niceDate.setText(article.getLink());
            holder.itemView.setOnClickListener(view -> {
                Intent intent=new Intent(DirectorActivity.this,DirectorContentActivity.class);
                intent.putExtra("link",article.title);
                intent.putExtra("author",article.author);
                startActivity(intent);






            });

        }

        @Override
        public int getItemCount() {
            return filteredList.size();
        }

         class NewsViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            TextView niceDate;


            public NewsViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.rv2textView2);
                niceDate = itemView.findViewById(R.id.rv2textView4);
            }
        }
    }

    public static class DirectorNews {
        private int errorCode;
        private String errorMsg;
        private List<Data> data;

        public int getErrorCode() {

            return errorCode;
        }

        public String getErrorMsg() {

            return errorMsg;
        }

        public List<Data> getData() {

            return data;
        }

        public static class Data {
            private int cid;
            private String name;
            private List<Articles> articles;

            public int getCid() {
                return cid;
            }

            public String getName() {
                return name;
            }

            public List<Articles> getArticles() {
                return articles;
            }
        }

        public static class Articles {
            private boolean adminAdd;
            private String apkLink;
            private int audit;
            private String author;
            private boolean canEdit;
            private int chapterId;
            private String chapterName;
            private boolean collect;
            private int courseId;
            private String desc;
            private String descMd;
            private String envelopePic;
            private boolean fresh;
            private String host;
            private int id;
            private boolean isAdminAdd;
            private String link;
             String niceDate;
            private String niceShareDate;
            private String origin;
            private String prefix;
            private String projectLink;
            private long publishTime; // 之前已修改过的字段
            private int realSuperChapterId;
            private int selfVisible;
            // 将 shareDate 类型从 int 改为 long
            private long shareDate;
            private String shareUser;
            private int superChapterId;
            private String superChapterName;
            private String title;
            private int type;
            private int userId;
            private int visible;
            private int zan;
            private List<Tag> tags;

           public  String getLink(){

               return link;
           }
            public String getTitle() {

               return title;
            }

            public String getNiceDate() {

               return niceDate;
            }

            public int getZan() {

               return zan;
            }

            public long getPublishTime() {
                return publishTime;
            }

            public long getShareDate() {

               return shareDate;
            }

            public static class Tag {
                private int cid;
                private String name;

                public int getCid() {

                    return cid;
                }

                public String getName() {
                    return name;
                }
            }
        }
    }
}
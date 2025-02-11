package com.example.winterapplication;

import static java.util.Locale.filter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class StructArticle extends AppCompatActivity {
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    List<WanAndroidArticleResponse.Data.Article> articleList = new ArrayList<>();
    private TextView BackTextView;

    private SearchView searchView;

    private FloatingActionButton returnTop;

    List<WanAndroidArticleResponse.Data.Article> filteredList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_structarticle);

        mRecyclerView = findViewById(R.id.re);
        fetchDataFromApi(); // 发起网络请求

        searchView=findViewById(R.id.searchView);
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
        returnTop=findViewById(R.id.fab);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //获取到第一个item的显示的下标  不等于0表示第一个item处于不可见状态 说明列表没有滑动到顶部 显示回到顶部按钮
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 判断是否滚动超过一屏
                    if (firstVisibleItemPosition == 0) {
                        returnTop.hide();
                    } else {
                        //显示回到顶部按钮
                        returnTop.show();
                        returnTop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                recyclerView.scrollToPosition(0);
                            }
                        });

                    }//获取RecyclerView滑动时候的状态
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {//拖动中
                    returnTop.hide();
                }
            }
        });

        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter); // 将数据通过适配器绑在 RV 上
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // 线性布局
        initView();
        initClick();
    }

    private  void filter(String text){
        filteredList.clear();
        if (TextUtils.isEmpty(text)){
            filteredList.addAll(articleList);
        }else {
            for (WanAndroidArticleResponse.Data.Article item:articleList){
                if (item.title.contains(text)){
                    filteredList.add(item);
                }
            }
        }
       mAdapter.notifyDataSetChanged();


    }

    private void fetchDataFromApi() {
        OkHttpClient okHttpClient = new OkHttpClient(); // 创建一个 OkHttpClient 实例，用于执行网络请求
        Request request = new Request.Builder()
                .url("https://www.wanandroid.com/article/list/0/json?cid=60")
                .get()
                .build(); // 使用 Request.Builder 构建一个 GET 请求
        okHttpClient.newCall(request).enqueue(new Callback() { // 将请求加入请求队列，使用 enqueue 方法异步执行请求，并添加一个回调函数，当请求成功时调用 onResponse 方法，失败时调用 onFailure 方法
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string(); // 从响应中获取响应体的内容并存储在 result 字符串中
                handleResponse(result); // 调用 handleResponse 方法处理响应数据
            }


            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("StructArticle", "网络请求失败: " + e.getMessage());
            }
        });
    }


    private void handleResponse(String result) {
        Gson gson = new Gson();
        WanAndroidArticleResponse response = gson.fromJson(result, WanAndroidArticleResponse.class);
        if (response!= null && response.data!= null) {
            articleList.addAll(response.data.datas);
            filteredList.addAll(response.data.datas);
            runOnUiThread(() -> mAdapter.notifyDataSetChanged());
        }
    }


    class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> { // 自定义的 RecyclerView 适配器类，用于将 newsDTOList 中的数据绑定到 RecyclerView 的列表项上


        @Override
        public Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { // 创建 ViewHolder 的方法，使用 getLayoutInflater().inflate 方法将 news_item_layout 布局文件转换为视图，并创建 MyViewHolder 实例
            View view = getLayoutInflater().inflate(R.layout.news_item_sa, parent, false);
            return new Adapter.MyViewHolder(view);
        }


        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            WanAndroidArticleResponse.Data.Article article = filteredList.get(position);
            holder.title.setText(article.title);
            holder.niceDate.setText(article.niceDate);
            holder.zan.setText(String.valueOf(article.zan));
            holder.itemView.setOnClickListener(view -> {
                Intent intent=new Intent(StructArticle.this,StructContentActivity.class);
                intent.putExtra("link",article.link);
                intent.putExtra("title",article.title);
                intent.putExtra("author",article.shareUser);
                intent.putExtra("niceData",article.niceDate);
                startActivity(intent);

            });
        }


        @Override
        public int getItemCount() {
            return filteredList.size();
        } // 返回 newsDTOList 的大小，用于确定 RecyclerView 列表项的数量


        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView title;

            TextView niceDate;
            TextView zan;


            MyViewHolder(View itemView) {
                super(itemView);
               title=itemView.findViewById(R.id.rv2textView2);
                niceDate = itemView.findViewById(R.id.rv2textView3);
                zan = itemView.findViewById(R.id.rv2textView4);
            }
        }
    }


    private void initClick() {
        if (BackTextView!= null) {
            BackTextView.setOnClickListener(v -> {
                Intent intent = new Intent(StructArticle.this, StructActivity.class);
                // 启动该意图，实现页面跳转到首页面
                startActivity(intent);
                finish();
            });
        } else {
            Log.e("StructArticle", "BackTextView is null");
        }
    }


    private void initView() {
        BackTextView = findViewById(R.id.net_back);
    }
}


class StructArticleNew {
    Data data;
    private int errorCode;
    private String errorMsg;


    static class Data {
        private int curPage;
        List<Article> datas;
        private int offset;
        private boolean over;
        private int pageCount;
        private int size;
        private int total;


        static class Article {
            private String apkLink;
            private int audit;
           String author;
            private boolean canEdit;
            private int chapterId;
             String chapterName;
            private boolean collect;
            private int courseId;
            private String desc;
            private String descMd;
            private String envelopePic;
            private boolean fresh;
            private int id;
             String link;
            private String niceDate;
            private String niceShareDate;
            private String origin;
            private String prefix;
            private String projectLink;
            private long publishTime;
            private int realSuperChapterId;
            private int selfVisible;
            private long shareDate;
            private String shareUser;
            private int superChapterId;
            private String superChapterName;
            private List<Tag> tags;
            private String title;
            private int type;
            private int userId;
            private int visible;
             int zan;


            static class Tag {
                private String name;
                private String url;
            }
        }
    }
}
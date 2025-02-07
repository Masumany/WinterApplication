package com.example.winterapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SquareActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;

    private SearchView searchView;
    RecyclerView.Adapter mAdapter;
    List<SquareNew.Datas> data = new ArrayList<SquareNew.Datas>();
    private TextView BackTextView;

    List<SquareNew.Datas> filteredList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square);

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

        mAdapter = new Adapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initVIew();
        initClick();

    }
    private  void filter(String text){
        filteredList.clear();
        if (TextUtils.isEmpty(text)){
            filteredList.addAll(data);
        }else {
            for (SquareNew.Datas item:data){
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
                .url("https://wanandroid.com/user_article/list/0/json")
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
                Log.e("SquareActivity", "网络请求失败: " + e.getMessage());
            }
        });

    }

    private void handleResponse(String result) {
        Gson gson = new Gson();
        SquareNew response = gson.fromJson(result, SquareNew.class);
        if (response != null && response.data != null) {
            data.addAll(response.data.datas);
            filteredList.addAll(data);
            runOnUiThread(() -> mAdapter.notifyDataSetChanged());
        }
    }

    class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
        @Override
        public Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { // 创建 ViewHolder 的方法，使用 getLayoutInflater().inflate 方法将 news_item_layout 布局文件转换为视图，并创建 MyViewHolder 实例
            View view = getLayoutInflater().inflate(R.layout.news_item_squ, parent, false);
            return new Adapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull Adapter.MyViewHolder holder, int position) {
            SquareNew.Datas datas=filteredList.get(position);
            holder.title.setText(datas.title);
            holder.niceDate.setText(datas.niceDate);
            holder.itemView.setOnClickListener(view -> {
                Intent intent=new Intent(SquareActivity.this,SquareContentActivity.class);
                intent.putExtra("link",datas.link);
                intent.putExtra("title",datas.title);
                intent.putExtra("niceData",datas.niceDate);
                startActivity(intent);

            });

        }

        @Override
        public int getItemCount() {
            return filteredList.size();
        }


        class MyViewHolder extends RecyclerView.ViewHolder{

            TextView title;
            TextView niceDate;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                title=itemView.findViewById(R.id.rv2textView2);
                niceDate = itemView.findViewById(R.id.rv2textView3);
            }
        }


    }
    private void initClick() {
        if (BackTextView!= null) {
            BackTextView.setOnClickListener(v -> {
                Intent intent = new Intent(SquareActivity.this, MineActivity.class);
                // 启动该意图，实现页面跳转到首页面
                startActivity(intent);
                finish();
            });
        } else {
            Log.e("StructArticle", "BackTextView is null");
        }
    }

    private void initVIew() {
        BackTextView = findViewById(R.id.net_back);
    }
}

class SquareNews{
    private  int errorCode;
    private  String errorMsg;
    SquareNew.Data data;

    static class Data{
        private   int curPage;
        List<SquareNew.Datas> datas;
        private int offset;
        private boolean over;
        private int pageCount;
        private int size;
        private int total;
    }

    static class Datas{
        private boolean adminAdd;
        private String apkLink;
        private int audit;
        private String author;
        private boolean canEdit;
        private int chapterId;
        private String chapterName;
        private  boolean collect;
        private  int courseId;
        private String desc;
        private String descMd;
        private String envelopePic;

        private  boolean fresh;
        private String host;
        private  int id;
        private  boolean isAdminAdd;

        private String link;
        String niceDate;
        private String niceShareData;
        private String origin;
        private String prefix;
        private String projectLink;
        private long publishTime;
        private int realSuperChapterId;
        private int selfVisible;
        private long shareDate;
        private  String shareUser;
        private int superChapterId;
        private  String superChapterName;
        String title;
        private int type;
        private int userId;
        private int visible;
        private int zan;

    }


}